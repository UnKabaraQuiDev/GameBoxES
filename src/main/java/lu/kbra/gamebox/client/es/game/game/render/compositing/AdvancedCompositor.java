package lu.kbra.gamebox.client.es.game.game.render.compositing;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.composition.Framebuffer;
import lu.kbra.gamebox.client.es.engine.graph.composition.FramebufferAttachment;
import lu.kbra.gamebox.client.es.engine.graph.composition.PassRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.composition.RenderBuffer;
import lu.kbra.gamebox.client.es.engine.graph.composition.RenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.DataType;
import lu.kbra.gamebox.client.es.engine.utils.consts.FrameBufferAttachment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelFormat;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelInternalFormat;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.game.game.render.shaders.BackgroundShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.BrightnessFilterShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.ScaleShader;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;

public class AdvancedCompositor implements Cleanupable {

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh("PASS_SCREEN", null, new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL40.GL_ELEMENT_ARRAY_BUFFER), new Vec2fAttribArray("uv", 1, 1, new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Vector4f background = GlobalConsts.BG;

	protected LinkedList<String> layers = new LinkedList<>();
	protected LinkedList<String> passes = new LinkedList<>();

	protected Framebuffer framebuffer, lastFramebuffer;
	protected SingleTexture depth, color0;

	protected Vector2i resolution = new Vector2i(0, 0);
	protected int samples = 1;

	private Material backGroundMaterial, highLightsMaterial, scaleMaterial;

	private boolean genTextures() {
		if (depth != null && depth.isValid())
			depth.cleanup();
		if (color0 != null && color0.isValid())
			color0.cleanup();

		framebuffer.clearAttachments();

		depth = new SingleTexture("depth", resolution.x, resolution.y);
		if (samples > 1) {
			depth.setTextureType(TextureType.TXT2DMS);
		} else {
			depth.setTextureType(TextureType.TXT2D);
		}
		depth.setSampleCount(samples);
		depth.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT32F);
		depth.setFormat(TexelFormat.DEPTH);
		depth.setDataType(DataType.FLOAT);
		depth.setup();

		color0 = new SingleTexture("color", resolution.x, resolution.y);
		if (samples > 1) {
			color0.setTextureType(TextureType.TXT2DMS);
		} else {
			color0.setTextureType(TextureType.TXT2D);
		}
		color0.setSampleCount(samples);
		color0.setInternalFormat(TexelInternalFormat.RGBA);
		color0.setFormat(TexelFormat.RGBA);
		color0.setDataType(DataType.UBYTE);
		color0.setup();

		if (!framebuffer.attachTexture(FrameBufferAttachment.DEPTH, 0, depth))
			return false;

		if (!framebuffer.attachTexture(FrameBufferAttachment.COLOR_FIRST, 0, color0))
			return false;

		return true;
	}

	public void render(GameEngine engine) {
		if (highLightsMaterial == null) {
			highLightsMaterial = engine.getCache().loadOrGetMaterial(BrightnessFilterShader.BrightnessFilterMaterial.NAME, BrightnessFilterShader.BrightnessFilterMaterial.class, (float) 0.49801f);
		}
		if (scaleMaterial == null) {
			scaleMaterial = engine.getCache().loadOrGetMaterial(ScaleShader.ScaleMaterial.NAME, ScaleShader.ScaleMaterial.class, (int) 1920, (int) 1080);
		}
		if (backGroundMaterial == null) {
			backGroundMaterial = engine.getCache().loadOrGetMaterial(BackgroundShader.BackgroundMaterial.NAME, BackgroundShader.BackgroundMaterial.class,
					engine.getCache().loadOrGetSingleTexture("compositeBG", "./resources/textures/ui/defaultBG.png", TextureFilter.LINEAR));
		}

		if (framebuffer == null) {
			framebuffer = engine.getCache().loadFramebuffer(this.getClass().getName() + "#" + hashCode());
		}

		framebuffer.bind();
		lastFramebuffer = framebuffer;

		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();

		boolean needRegen = !resolution.equals(width, height);

		if (needRegen) {
			resolution = new Vector2i(width, height);
			if (!genTextures())
				throw new RuntimeException("Error while generating textures and framebuffer.");
			GL40.glViewport(0, 0, width, height);
		}

		// color0.bind();

		if (!framebuffer.isComplete()) {
			GlobalLogger.log(Level.SEVERE, "Framebuffer not complete: " + framebuffer.getError() + ", w:" + width + " h:" + height);
			return;
		}

		GL40.glEnable(GL40.GL_DEPTH_TEST);
		PDRUtils.checkGlError("Enable(DEPTH_TEST)");

		framebuffer.bind();

		GL40.glClearColor(background.x, background.y, background.z, background.w);
		PDRUtils.checkGlError("ClearColor(" + background + ")");
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
		PDRUtils.checkGlError("Clear(COLOR | DEPTH)");
		
		/*Framebuffer bg = genFBO(resolution.x, resolution.y, TextureFilter.LINEAR);
		render(engine.getCache(), engine, framebuffer, bg, backGroundMaterial);
		
		GL40.glClear(GL40.GL_DEPTH_BUFFER_BIT);
		PDRUtils.checkGlError("Clear(DEPTH)");*/
		
		for (String l : layers) {
			if (l == null)
				continue;

			RenderLayer rl = engine.getCache().getRenderLayer(l);
			if (rl == null) {
				GlobalLogger.log(Level.WARNING, "Render Layer: " + l + ", not found in Cache");
				break;
			}

			if (!rl.isVisible())
				continue;

			GlobalLogger.info("Rendering: " + rl.getId());
			rl.render(engine, framebuffer);
		}

		GL40.glDepthMask(false);
		PDRUtils.checkGlError("DepthMask(false)");

		/*int scale = 5;

		((ScaleMaterial) scaleMaterial).setResolution(resolution.x / scale, resolution.y / scale);

		Framebuffer highLights = genFBO(resolution.x, resolution.y, TextureFilter.LINEAR);
		render(cache, engine, framebuffer, highLights, highLightsMaterial);

		Framebuffer lowScale = genFBO(resolution.x, resolution.y, TextureFilter.LINEAR);
		render(cache, engine, highLights, lowScale, scaleMaterial);*/
		
		GL40.glDepthMask(true);
		PDRUtils.checkGlError("DepthMask(true)");

		GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, 0);
		PDRUtils.checkGlError("BindFramebuffer()=0");
		GL40.glBindFramebuffer(GL40.GL_DRAW_FRAMEBUFFER, 0);
		PDRUtils.checkGlError("BindFramebuffer(DRAW)=0");
		lastFramebuffer.bind(GL40.GL_READ_FRAMEBUFFER);

		GL40.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL40.GL_COLOR_BUFFER_BIT, GL40.GL_NEAREST);
		PDRUtils.checkGlError();

		/*
		 * if (!passes.isEmpty()) {
		 * 
		 * Framebuffer highLights = genFBO(width, height); render(cache, engine,
		 * framebuffer, null, highLightsMaterial);
		 * 
		 * highLights.unbind(); highLights.bind(GL40.GL_READ_FRAMEBUFFER);
		 * 
		 * // GL40.glBlitFramebuffer(0, 0, resolution.x, resolution.y, 0, 0,
		 * resolution.x, resolution.y, GL40.GL_COLOR_BUFFER_BIT, GL40.GL_NEAREST);
		 * 
		 * } else { GL40.glBlitFramebuffer(0, 0, resolution.x, resolution.y, 0, 0,
		 * resolution.x, resolution.y, GL40.GL_COLOR_BUFFER_BIT, GL40.GL_NEAREST); }
		 */

		framebuffer.bind();
	}

	private Framebuffer genFBO(int width, int height, TextureFilter filter) {
		Framebuffer fbo1 = new Framebuffer("fbo1");

		fbo1.bind();

		SingleTexture fbo1color0 = new SingleTexture("color", width, height);
		fbo1color0.setTextureType(TextureType.TXT2D);
		fbo1color0.setInternalFormat(TexelInternalFormat.RGBA);
		fbo1color0.setFormat(TexelFormat.RGBA);
		fbo1color0.setFilters(filter, filter);
		fbo1color0.setDataType(DataType.UBYTE);
		fbo1color0.setup();

		fbo1.attachTexture(FrameBufferAttachment.COLOR_FIRST, 0, fbo1color0);

		/*
		 * RenderBuffer fbo1drb = new RenderBuffer("DepthRBO");
		 * fbo1drb.setTexelFormat(TexelFormat.DEPTH); fbo1drb.setSize(width, height);
		 * fbo1drb.setup();
		 * 
		 * fbo1.attachRenderBuffer(FrameBufferAttachment.DEPTH, 0, fbo1drb);
		 */

		SingleTexture fbo1depth = new SingleTexture("depth", width, height);
		fbo1depth.setTextureType(TextureType.TXT2D);
		fbo1depth.setInternalFormat(TexelInternalFormat.DEPTH_COMPONENT32F);
		fbo1depth.setFormat(TexelFormat.DEPTH);
		fbo1depth.setDataType(DataType.FLOAT);
		fbo1depth.setup();

		fbo1.attachTexture(FrameBufferAttachment.DEPTH, 0, fbo1depth);

		if (!fbo1.isComplete()) {
			GlobalLogger.log(Level.SEVERE, "Framebuffer not complete: " + framebuffer.getError() + ", w:" + width + " h:" + height);
			return null;
		}

		fbo1.unbind();

		return fbo1;
	}

	public void render(CacheManager cache, GameEngine engine, Framebuffer from, Framebuffer to, Material material) {
		GlobalLogger.log(Level.INFO, "PassRenderLayer : m:" + material);

		from.bind(GL40.GL_READ_FRAMEBUFFER);
		if (to == null) {
			// from.unbind(GL40.GL_DRAW_FRAMEBUFFER);
			GL40.glBindFramebuffer(GL40.GL_DRAW_FRAMEBUFFER, 0); // rendering to default
			lastFramebuffer = framebuffer;
		} else {
			to.bind(GL40.GL_DRAW_FRAMEBUFFER);
			lastFramebuffer = to;
		}

		SCREEN.bind();

		if (material == null) {
			GlobalLogger.log(Level.WARNING, "Material is null!");
			return;
		}
		RenderShader shader = material.getRenderShader();
		if (shader == null) {
			GlobalLogger.log(Level.WARNING, "Shader is null!");
			return;
		}

		shader.bind();

		material.setPropertyIfPresent(SCREEN_HEIGHT, engine.getWindow().getHeight());
		material.setPropertyIfPresent(SCREEN_WIDTH, engine.getWindow().getWidth());

		material.bindProperties(cache, null, shader);

		int i = 0;
		for (Entry<Integer, FramebufferAttachment> attachments : from.getAttachments().entrySet()) {
			int loc = shader.getUniformLocation(((UniqueID) attachments.getValue()).getId());

			if (attachments.getValue() instanceof RenderBuffer)
				continue;

			if (loc != -1) {
				attachments.getValue().bind(i);
				attachments.getValue().bindUniform(loc, i);
				i++;
			}
		}

		if (shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}

		GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_TRIANGLES, SCREEN.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glDisable(GL40.GL_BLEND);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		SCREEN.unbind();
	}

	public void addRenderLayer(int i, RenderLayer id) {
		if (id instanceof PassRenderLayer)
			return;
		layers.add(i, id.getId());
	}

	public void addPassLayer(int i, PassRenderLayer id) {
		passes.add(i, id.getId());
	}

	@Override
	public void cleanup() {
		if (framebuffer != null)
			framebuffer.cleanup();
		if (depth != null)
			depth.cleanup();
		if (color0 != null)
			color0.cleanup();
		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

	public Framebuffer getFramebuffer() {
		return framebuffer;
	}

}
