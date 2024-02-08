package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TransformComponent;
import lu.pcy113.pdr.engine.scene.Scene;

public class MeshRenderer extends Renderer<Scene, MeshComponent> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, MeshComponent m) {
		Mesh mesh = m.getMesh(cache);
		if (mesh == null) {
			GlobalLogger.log(Level.WARNING, "Mesh is null!");
			return;
		}

		GlobalLogger.log(Level.INFO, "Mesh : " + mesh.getId() + ", vao:" + mesh.getVao() + ", vec:" + mesh.getVertexCount() + ", vbo:" + mesh.getVbo());

		mesh.bind();

		Material material = mesh.getMaterial();
		if (material == null) {
			GlobalLogger.log(Level.WARNING, "Material is null!");
			return;
		}
		RenderShader shader = material.getShader();
		if (shader == null) {
			GlobalLogger.log(Level.WARNING, "Shader is null!");
			return;
		}

		shader.bind();

		Matrix4f projectionMatrix = null, viewMatrix = null, transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		}
		if (m.getParent().hasComponent(TransformComponent.class)) {
			TransformComponent transform = (TransformComponent) m.getParent().getComponent(m.getParent().getComponents(TransformComponent.class).get(0));
			if (transform != null) {
				transformationMatrix = transform.getTransform().getMatrix();
			}
		}
		material.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

		material.bindProperties(cache, scene, shader);

		if (shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}

		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glDisable(GL40.GL_BLEND);

		// debug only
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, transformationMatrix);

		mesh.unbind();

		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
