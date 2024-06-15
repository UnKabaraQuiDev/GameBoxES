package lu.kbra.gamebox.client.es.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL46;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TransformComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;

public class InstanceEmitterRenderer extends Renderer<Scene, InstanceEmitterComponent> {

	public InstanceEmitterRenderer() {
		super(InstanceEmitter.class);
	}

	@Override
	public void render_in(CacheManager cache, Scene scene, InstanceEmitterComponent pec) {
		InstanceEmitter pe = pec.getInstanceEmitter(cache);
		if (pe == null) {
			GlobalLogger.log(Level.WARNING, "InstanceEmitter is null!");
			return;
		}

		GameEngine.DEBUG.start("r_inst");

		GlobalLogger.log(Level.FINE, "InstanceEmitter : " + pe.getId());

		Mesh mesh = pe.getParticleMesh();
		if (mesh == null)
			return;

		Material material = mesh.getMaterial();
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

		GameEngine.DEBUG.start("r_uniforms");

		Matrix4f projectionMatrix = null, viewMatrix = null, transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjectionMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		}
		if (pec.getParent().hasComponent(TransformComponent.class)) {
			TransformComponent transform = (TransformComponent) pec.getParent().getComponent(pec.getParent().getComponents(TransformComponent.class).get(0));
			if (transform != null) {
				transformationMatrix = transform.getTransform().getMatrix();
			}
		}
		material.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);

		if (scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = pec.getParent().getComponent(PointLightSurfaceComponent.class);
			if (plsc != null)
				plsc.bindLights(cache, ((Scene3D) scene).getLights(), material);
		}

		material.bindProperties(cache, scene, shader);

		GameEngine.DEBUG.start("r_blend");
		if (shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}
		GameEngine.DEBUG.end("r_blend");

		GameEngine.DEBUG.end("r_uniforms");

		pe.bind();

		// GameEngine.DEBUG.start("r_compute");
		// pe.updatePull();
		// GameEngine.DEBUG.end("r_compute");

		GL40.glPolygonMode(shader.getFaceMode().getGlId(), shader.getRenderType().getGlId());

		GameEngine.DEBUG.start("r_draw");
		if (mesh.hasDrawBuffer()) {
			mesh.getDrawBuffer().bind();
			GL46.glDrawElementsIndirect(shader.getBeginMode().getGlId(), GL40.GL_UNSIGNED_INT, 0);
		} else {
			GL40.glDrawElementsInstanced(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0, pe.getParticleCount());
		}
		GameEngine.DEBUG.end("r_draw");

		GL40.glDisable(GL40.GL_BLEND);

		// debug only
		// GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix,
		// c.getTransform().getMatrix());

		mesh.unbind();

		GameEngine.DEBUG.end("r_inst");

		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
