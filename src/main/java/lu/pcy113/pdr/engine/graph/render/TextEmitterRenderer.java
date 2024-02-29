package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TransformComponent;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class TextEmitterRenderer extends Renderer<Scene, TextEmitterComponent> {

	public TextEmitterRenderer() {
		super(TextEmitter.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, TextEmitterComponent tec) {
		TextEmitter te = tec.getTextEmitter(
				cache);
		if (te == null) {
			GlobalLogger.log(
					Level.WARNING,
					"TextEmitter is null!");
			return;
		}

		GlobalLogger.log(
				Level.INFO,
				"TextEmitter : " + te.getId());

		InstanceEmitter pe = te.getInstances();
		if (pe == null) {
			GlobalLogger.log(
					Level.WARNING,
					"InstanceEmitter is null!");
			return;
		}

		Mesh mesh = pe.getParticleMesh();
		if (mesh == null)
			return;

		Material material = mesh.getMaterial();
		if (material == null) {
			GlobalLogger.log(
					Level.WARNING,
					"Material is null!");
			return;
		}
		RenderShader shader = material.getRenderShader();
		if (shader == null) {
			GlobalLogger.log(
					Level.WARNING,
					"Shader is null!");
			return;
		}

		shader.bind();

		Matrix4f projectionMatrix = null, viewMatrix = null, transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(
					RenderShader.PROJECTION_MATRIX,
					projectionMatrix);
			material.setPropertyIfPresent(
					RenderShader.VIEW_MATRIX,
					viewMatrix);
		}
		if (tec.getParent().hasComponent(
				TransformComponent.class)) {
			TransformComponent transform = (TransformComponent) tec.getParent().getComponent(
					tec.getParent().getComponents(
							TransformComponent.class).get(
									0));
			if (transform != null) {
				transformationMatrix = transform.getTransform().getMatrix();
			}
		}
		material.setPropertyIfPresent(
				RenderShader.TRANSFORMATION_MATRIX,
				transformationMatrix);

		if (scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = tec.getParent().getComponent(
					PointLightSurfaceComponent.class);
			if (plsc != null)
				plsc.bindLights(
						cache,
						((Scene3D) scene).getLights(),
						material);
		}

		material.bindProperties(
				cache,
				scene,
				shader);

		if (shader.isTransparent()) {
			GL40.glEnable(
					GL40.GL_BLEND);
			GL40.glBlendFunc(
					GL40.GL_SRC_ALPHA,
					GL40.GL_ONE_MINUS_SRC_ALPHA);
		}

		pe.bind();

		// pe.updatePull();

		GL40.glDrawElementsInstanced(
				GL40.GL_TRIANGLES,
				mesh.getIndicesCount(),
				GL40.GL_UNSIGNED_INT,
				0,
				pe.getParticleCount());

		GL40.glDisable(
				GL40.GL_BLEND);

		// debug only
		// GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix,
		// c.getTransform().getMatrix());

		mesh.unbind();

		GameEngine.DEBUG.gizmos(
				cache,
				scene,
				projectionMatrix,
				viewMatrix,
				transformationMatrix);
	}

}
