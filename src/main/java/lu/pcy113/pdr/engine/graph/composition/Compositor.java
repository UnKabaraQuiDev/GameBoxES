package lu.pcy113.pdr.engine.graph.composition;

import java.util.LinkedList;
import java.util.logging.Level;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.consts.DataType;
import lu.pcy113.pdr.engine.utils.consts.FrameBufferAttachment;
import lu.pcy113.pdr.engine.utils.consts.TexelFormat;
import lu.pcy113.pdr.engine.utils.consts.TexelInternalFormat;
import lu.pcy113.pdr.engine.utils.consts.TextureType;

public class Compositor implements Cleanupable {

	protected Vector4f background = new Vector4f(
			0.5f,
			0.5f,
			0.5f,
			1);

	protected LinkedList<String> layers = new LinkedList<>();
	protected LinkedList<String> passes = new LinkedList<>();

	protected Framebuffer framebuffer;

	protected SingleTexture depth, color0;

	protected Vector2i resolution = new Vector2i(
			0,
			0);

	private boolean genTextures() {
		if (depth != null && depth.isValid())
			depth.cleanup();
		if (color0 != null && color0.isValid())
			color0.cleanup();

		framebuffer.clearAttachments();

		depth = new SingleTexture(
				"depth",
				resolution.x,
				resolution.y);
		depth.setTextureType(
				TextureType.TXT2DMS);
		depth.setSampleCount(
				8);
		depth.setInternalFormat(
				TexelInternalFormat.DEPTH_COMPONENT32F);
		depth.setFormat(
				TexelFormat.DEPTH);
		depth.setDataType(
				DataType.FLOAT);
		depth.setup();

		color0 = new SingleTexture(
				"color",
				resolution.x,
				resolution.y);
		color0.setTextureType(
				TextureType.TXT2DMS);
		color0.setSampleCount(
				8);
		color0.setInternalFormat(
				TexelInternalFormat.RGBA);
		color0.setFormat(
				TexelFormat.RGBA);
		color0.setDataType(
				DataType.UBYTE);
		color0.setup();

		if (!framebuffer.attachTexture(
				FrameBufferAttachment.DEPTH,
				0,
				depth))
			return false;
		if (!framebuffer.attachTexture(
				FrameBufferAttachment.COLOR_FIRST,
				0,
				color0))
			return false;

		return true;
	}

	public void render(CacheManager cache, GameEngine engine) {
		if (framebuffer == null) {
			framebuffer = cache.loadFramebuffer(
					"Compositor");
		}

		framebuffer.bind();

		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();

		if (resolution.equals(
				width,
				height)) {
			// keep same texture
		} else {
      resolution = new Vector2i(
					width,
					height);
			if (!genTextures())
				throw new RuntimeException(
						"Could not attach textures to framebuffer");
			GL40.glViewport(
					0,
					0,
					width,
					height);
		}

		color0.bind();

		if (!framebuffer.isComplete()) {
			GlobalLogger.log(
					Level.SEVERE,
					"Framebuffer not complete: " + framebuffer.getError() + ", w:" + width + " h:" + height);
			return;
		}

		GL40.glEnable(
				GL40.GL_DEPTH_TEST);

		for (String l : layers) {
			if (l == null)
				continue;

			RenderLayer rl = cache.getRenderLayer(
					l);
			if (rl == null) {
				GlobalLogger.log(
						Level.WARNING,
						"Render Layer: " + l + ", not found in Cache");
				break;
			}

			if (!rl.isVisible())
				continue;

			rl.render(
					cache,
					engine,
					framebuffer);
		}

		framebuffer.unbind(
				GL40.GL_FRAMEBUFFER);

		GL40.glDepthMask(
				false);
		for (String l : passes) {
			if (l == null)
				continue;

			RenderLayer prl = cache.getRenderLayer(
					l);
			if (prl == null) {
				GlobalLogger.log(
						Level.WARNING,
						"Pass Render Layer: " + l + ", not found in Cache");
				break;
			}

			if (!(prl instanceof PassRenderLayer))
				continue;

			// color0.bind(0);
			// depth.bind

			prl.render(
					cache,
					engine,
					framebuffer);
		}
		GL40.glDepthMask(
				true);

		framebuffer.unbind(
				GL40.GL_DRAW_FRAMEBUFFER);
		framebuffer.bind(
				GL40.GL_READ_FRAMEBUFFER);

		if (passes.isEmpty())
			GL40.glBlitFramebuffer(
					0,
					0,
					width,
					height,
					0,
					0,
					width,
					height,
					GL40.GL_COLOR_BUFFER_BIT,
					GL40.GL_NEAREST);

		framebuffer.bind();

		GL40.glClearColor(
				background.x,
				background.y,
				background.z,
				background.w);
		GL40.glClear(
				GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
	}

	public void addRenderLayer(int i, RenderLayer id) {
		if (id instanceof PassRenderLayer)
			return;
		layers.add(
				i,
				id.getId());
	}

	public void addPassLayer(int i, PassRenderLayer id) {
		passes.add(
				i,
				id.getId());
	}

	@Override
	public void cleanup() {
		if (framebuffer != null)
			framebuffer.cleanup();
		if (depth != null)
			depth.cleanup();
		if (color0 != null)
			color0.cleanup();
	}

	public Framebuffer getFramebuffer() {
		return framebuffer;
	}

}
