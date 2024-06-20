package lu.kbra.gamebox.client.es.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.graph.material.gizmo.GizmoShader;
import lu.kbra.gamebox.client.es.engine.graph.material.gizmo.GizmoShader.GizmoMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TransformComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;

public class GizmoRenderer extends Renderer<Scene, GizmoComponent> {

	public GizmoRenderer() {
		super(Gizmo.class);
	}

	@Override
	public void render_in(CacheManager cache, Scene scene, GizmoComponent gi) {
		Entity e = gi.getParent();

		Gizmo gizmo = gi.getGizmo(cache);
		if (gizmo == null)
			return;

		GameEngine.DEBUG.start("r_gizmo");

		GlobalLogger.log(Level.FINE, "Gizmo : " + gizmo.getId());

		gizmo.bind();

		GizmoMaterial material;
		if (cache.hasMaterial(GizmoShader.GizmoMaterial.NAME)) {
			material = (GizmoMaterial) cache.getMaterial(GizmoShader.GizmoMaterial.NAME);
		} else {
			material = (GizmoMaterial) cache.loadMaterial(GizmoShader.GizmoMaterial.class);
		}
		RenderShader shader = material.getRenderShader();

		shader.bind();

		GameEngine.DEBUG.start("r_uniforms");
		GameEngine.DEBUG.start("r_uniforms_scene");
		Matrix4f projectionMatrix = null, viewMatrix = null, transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			Camera camera = scene.getCamera();
			projectionMatrix = camera.getProjection().getProjectionMatrix();
			viewMatrix = camera.getViewMatrix();
			shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
			shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
			if (camera instanceof Camera3D) {
				material.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) camera).getPosition());
			}
		}
		GameEngine.DEBUG.end("r_uniforms_scene");

		GameEngine.DEBUG.start("r_uniforms_transform");
		if (material.hasProperty(RenderShader.TRANSFORMATION_MATRIX)) {
			if (e.hasComponent(TransformComponent.class)) {
				TransformComponent transform = (TransformComponent) e.getComponent(e.getComponents(TransformComponent.class).get(0));
				if (transform != null) {
					transformationMatrix = transform.getTransform().getMatrix();
				}
			}
			material.setProperty(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		}
		GameEngine.DEBUG.end("r_uniforms_transform");

		material.bindProperties(cache, scene, shader);

		GameEngine.DEBUG.end("r_uniforms");

		if (GameEngine.DEBUG.ignoreDepth)
			GLES30.glDisable(GLES30.GL_DEPTH_TEST);

		// GLES30.glPolygonMode(shader.getFaceMode().getGlId(), shader.getRenderType().getGlId());
		GLES30.glLineWidth(Gizmo.LINE_WIDTH);

		GameEngine.DEBUG.start("r_draw");
		GLES30.glDrawElements(shader.getBeginMode().getGlId(), gizmo.getIndicesCount(), GLES30.GL_UNSIGNED_INT, 0);
		GameEngine.DEBUG.end("r_draw");

		// GLES30.glPolygonMode(GLES30.GL_FRONT_AND_BACK, GLES30.GL_FILL);
		GLES30.glEnable(GLES30.GL_DEPTH_TEST);

		gizmo.unbind();

		GameEngine.DEBUG.end("r_gizmo");
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
