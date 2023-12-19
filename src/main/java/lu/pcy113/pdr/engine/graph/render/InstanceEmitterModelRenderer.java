package lu.pcy113.pdr.engine.graph.render;

import java.util.List;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TransformComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class InstanceEmitterModelRenderer
		extends
		Renderer<Scene, InstanceEmitterModelComponent> {

	public InstanceEmitterModelRenderer() {
		super(InstanceEmitterModel.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, InstanceEmitterModelComponent pec) {
		InstanceEmitterModel pem = pec.getInstanceEmitterModel(cache);
		if (pem == null) return;

		GlobalLogger.log(Level.INFO, "InstanceEmitterModel : " + pem.getId());

		InstanceEmitter pe = cache.getInstanceEmitter(pem.getEmitter());
		if (pe == null) return;
		Mesh mesh = pe.getParticleMesh();
		if (mesh == null) return;

		Material material = cache.getMaterial(mesh.getMaterial());
		if (material == null) return;
		Shader shader = cache.getShader(material.getShader());
		if (shader == null) return;

		shader.bind();

		Matrix4f projectionMatrix = null, viewMatrix = null;
		Entity parent = pec.getParent();
		List<Class<? extends Component>> transforms = parent.getComponents(TransformComponent.class);
		// TransformComponent transform = null;
		Object transformationMatrix = null;
		if (!transforms.isEmpty())
			transformationMatrix = ((TransformComponent) parent.getComponent(transforms.get(0))).getTransform().getMatrix();
		else
			transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, transformationMatrix);
		}

		if (scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = pec.getParent().getComponent(PointLightSurfaceComponent.class);
			if (plsc != null) plsc.bindLights(cache, ((Scene3D) scene).getLights(), material);
		}

		material.bindProperties(cache, scene, shader);

		if (shader.isTransparent()) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}

		pe.bind();

		GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, mesh.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0, pe.getParticleCount());

		GL11.glDisable(GL11.GL_BLEND);

		// debug only
		// GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix,
		// c.getTransform().getMatrix());

		mesh.unbind();

		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
