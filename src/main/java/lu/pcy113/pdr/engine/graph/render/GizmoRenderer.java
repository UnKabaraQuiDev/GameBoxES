package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;

public class GizmoRenderer extends Renderer<Scene, GizmoComponent> {

	public GizmoRenderer() {
		super(Gizmo.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, GizmoComponent gi) {
		Gizmo gizmo = gi.getGizmo(cache);
		if (gizmo == null)
			return;
		
		GameEngine.DEBUG.start("r_gizmo");
		
		GlobalLogger.log(Level.INFO, "Gizmo : " + gizmo.getId());

		gizmo.bind();
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		GizmoMaterial material;
		if (cache.hasMaterial(GizmoShader.GizmoMaterial.NAME)) {
			material = (GizmoMaterial) cache.getMaterial(GizmoShader.GizmoMaterial.NAME);
		} else {
			material = (GizmoMaterial) cache.loadMaterial(GizmoShader.GizmoMaterial.class);
		}
		RenderShader shader = material.getRenderShader();

		shader.bind();
		
		GameEngine.DEBUG.start("r_uniforms");
		
		if (scene != null) {
			Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, new Matrix4f().identity());
			material.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) scene.getCamera()).getPosition());
		}

		material.bindProperties(cache, scene, shader);
		
		GameEngine.DEBUG.end("r_uniforms");
		
		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		GL40.glLineWidth(Gizmo.LINE_WIDTH);
		
		GameEngine.DEBUG.start("r_draw");
		GL40.glDrawElements(GL40.GL_LINES, gizmo.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);
		GameEngine.DEBUG.end("r_draw");

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmo.unbind();
		
		GameEngine.DEBUG.end("r_gizmo");
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
