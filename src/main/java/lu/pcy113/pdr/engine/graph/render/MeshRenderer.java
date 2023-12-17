package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.scene.Scene;

public class MeshRenderer extends Renderer<Scene, MeshComponent> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, MeshComponent m) {
		Mesh mesh = m.getMesh(cache);
		if (mesh == null)
			return;

		GlobalLogger.log(Level.INFO, "Mesh : " + mesh.getId() + ", vao:" + mesh.getVao() + ", vec:"
				+ mesh.getVertexCount() + ", vbo:" + mesh.getVbo());

		mesh.bind();

		Material material = cache.getMaterial(mesh.getMaterial());
		if (material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		if (shader == null)
			return;

		shader.bind();

		Matrix4f projectionMatrix = null, viewMatrix = null;
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, new Matrix4f().identity());
		}

		material.bindProperties(cache, scene, shader);

		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		// debug only
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, null);

		mesh.unbind();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
