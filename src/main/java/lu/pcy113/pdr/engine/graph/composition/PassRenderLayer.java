package lu.pcy113.pdr.engine.graph.composition;

import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;

public class PassRenderLayer
		extends
		RenderLayer<GameEngine, Mesh> {

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh("PASS_SCREEN", null,
			new Vec3fAttribArray("pos", 0, 1,
					new Vector3f[] {new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(1, -1, 0)}),
			new UIntAttribArray("ind", -1, 1, new int[] {0, 1, 2, 0, 2, 3}),
			new Vec2fAttribArray("uv", 1, 1, new Vector2f[] {new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0)}));

	protected Material material;

	public PassRenderLayer(String name, Material material) {
		super(name, SCREEN);
		this.material = material;
	}

	@Override
	public void render(CacheManager cache, GameEngine engine) {
		GlobalLogger.log(Level.INFO, "PassRenderLayer : m:" + material);

		/*
		 * MeshRenderer renderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		 * if(renderer == null) { GlobalLogger.log(Level.SEVERE,
		 * "No renderer found for: "+Mesh.NAME); return; }
		 * 
		 * renderer.render(cache, null, (Mesh) target);
		 */

		target.bind();

		Material material = this.material;
		if (material == null) return;
		Shader shader = material.getShader();
		if (shader == null) return;

		shader.bind();

		material.bindProperties(cache, this, shader);

		if (shader.hasUniform(SCREEN_WIDTH)) {
			shader.setUniform(SCREEN_WIDTH, engine.getWindow().getWidth());
		}
		if (shader.hasUniform(SCREEN_HEIGHT)) {
			shader.setUniform(SCREEN_HEIGHT, engine.getWindow().getHeight());
		}

		// GL40.glDisable(GL40.GL_DEPTH_TEST);
		// GL40.glDepthMask(false);

		GL40.glDrawElements(GL40.GL_TRIANGLES, target.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);

		// GL40.glDepthMask(true);

		target.unbind();
	}

	@Override
	public void cleanup() {
		super.cleanup();

		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

}
