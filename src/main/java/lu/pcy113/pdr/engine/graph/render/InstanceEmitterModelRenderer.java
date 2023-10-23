package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class InstanceEmitterModelRenderer extends Renderer<Scene, InstanceEmitterModelComponent> {
	
	public InstanceEmitterModelRenderer() {
		super(InstanceEmitterModel.class);
	}
	
	@Override
	public void render(CacheManager cache, Scene scene, InstanceEmitterModelComponent pec) {
		InstanceEmitterModel pem = pec.getInstanceEmitterModel(cache);
		if(pem == null)
			return;
		
		Logger.log(Level.INFO, "InstanceEmitterModel : "+pem.getId());
		
		InstanceEmitter pe = cache.getInstanceEmitter(pem.getEmitter());
		if(pe == null)
			return;
		Mesh mesh = pe.getParticleMesh();
		if(mesh == null)
			return;
		
		Material material = cache.getMaterial(mesh.getMaterial());
		if(material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		if(shader == null)
			return;
		
		shader.bind();
		
		Matrix4f projectionMatrix = null, viewMatrix = null;
		Object transformationMatrix = pem.getTransform().getMatrix();
		if(scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, transformationMatrix);
		}
		
		if(scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = pec.getParent().getComponent(PointLightSurfaceComponent.class);
			if(plsc != null)
				plsc.bindLights(cache, ((Scene3D) scene).getLights(), material);
		}
			
		material.bindProperties(cache, scene, shader);
		
		if(shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		pe.bind();
		
		GL40.glDrawElementsInstanced(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0, pe.getParticleCount());
		
		GL40.glDisable(GL40.GL_BLEND);
		
		// debug only
		//GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, c.getTransform().getMatrix());
		
		mesh.unbind();
		
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
}
