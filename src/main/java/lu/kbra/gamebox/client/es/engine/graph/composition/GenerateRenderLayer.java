package lu.kbra.gamebox.client.es.engine.graph.composition;

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
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.pcy113.pclib.logger.GlobalLogger;

public class GenerateRenderLayer extends RenderLayer<GameEngine, Framebuffer, Mesh> {

	private static Mesh SCREEN = new Mesh("GEN_SCREEN", null, new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 3, 1, 2, 3 }), new Vec2fAttribArray("uv", 1, 1, new Vector2f[] { new Vector2f(-1, 1), new Vector2f(1, 1), new Vector2f(1, -1), new Vector2f(-1, -1) }));

	protected CacheManager cache;
	protected Material material;

	public GenerateRenderLayer(String name, Material material, CacheManager cache) {
		super(name, SCREEN);
		this.material = material;
		this.cache = cache;
	}

	@Override
	public void render(GameEngine engine, Framebuffer fb) {
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

		material.bindProperties(cache, this, shader);

		GL_W.glDepthMask(false);

		GL_W.glDrawElements(GL_W.GL_TRIANGLES, target.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0);

		GL_W.glDepthMask(true);

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
