package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.Map.Entry;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.pcy113.pclib.logger.GlobalLogger;

public class PassRenderLayer extends RenderLayer<GameEngine, Framebuffer, Mesh> {

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh("PASS_SCREEN", null, new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER), new Vec2fAttribArray("uv", 1, 1, new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected CacheManager cache;
	protected Material material;

	public PassRenderLayer(String name, Material material, CacheManager cache) {
		super(name, SCREEN);
		this.material = material;
		this.cache = cache;
	}

	@Override
	public void render(GameEngine engine, Framebuffer from) {
		GlobalLogger.log(Level.INFO, "PassRenderLayer : m:" + material);

		target.bind();

		Material material = this.material;
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

		for (Entry<Integer, FramebufferAttachment> attachments : from.getAttachments().entrySet()) {
			int id = shader.getUniformLocation(((UniqueID) attachments.getValue()).getId());
			if (id != -1) {
				attachments.getValue().bind(id);
			}
		}

		material.bindProperties(cache, this, shader);

		if (shader.isTransparent()) {
			GL_W.glEnable(GL_W.GL_BLEND);
			GL_W.glBlendFunc(GL_W.GL_SRC_ALPHA, GL_W.GL_ONE_MINUS_SRC_ALPHA);
		}

		GL_W.glDisable(GL_W.GL_DEPTH_TEST);

		GL_W.glDrawElements(GL_W.GL_TRIANGLES, target.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);

		GL_W.glDisable(GL_W.GL_BLEND);
		GL_W.glEnable(GL_W.GL_DEPTH_TEST);

		target.unbind();
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + name);

		super.cleanup();

		if (SCREEN == null)
			return;

		SCREEN.cleanup();
		SCREEN = null;
	}

}
