package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;

public class GizmoRenderer
		extends
		Renderer<Scene, GizmoComponent> {

	public GizmoRenderer() {
		super(Gizmo.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, GizmoComponent gi) {
		Gizmo gizmo = gi.getGizmo(cache);
		if (gizmo == null) return;

		GlobalLogger.log(Level.INFO, "Gizmo : " + gizmo.getId());

		gizmo.bind();

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		Material material = cache.getMaterial(GizmoMaterial.NAME);
		if (material == null) {
			GizmoShader shader = new GizmoShader();
			cache.addShader(shader);
			material = new GizmoMaterial(shader);
			cache.addMaterial(material);
		}
		Shader shader = cache.getShader(material.getShader());

		shader.bind();

		if (scene != null) {
			Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, new Matrix4f().identity());
			material.setPropertyIfPresent(Shader.VIEW_POSITION, ((Camera3D) scene.getCamera()).getPosition());
		}

		material.bindProperties(cache, scene, shader);

		if (GameEngine.DEBUG.ignoreDepth) GL40.glDisable(GL40.GL_DEPTH_TEST);
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);

		GL40.glLineWidth(Gizmo.LINE_WIDTH);
		GL40.glDrawElements(GL40.GL_LINES, gizmo.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmo.unbind();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
