package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.particles.ParticleEmitter;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.ParticleEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.components.ParticleEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class ParticleEmitterModelRenderer extends Renderer<Scene3D, ParticleEmitterModelComponent> {
	
	public ParticleEmitterModelRenderer() {
		super(ParticleEmitterModel.class);
	}
	
	@Override
	public void render(CacheManager cache, Scene3D scene, ParticleEmitterModelComponent pec) {
		ParticleEmitterModel pem = pec.getParticleEmitterModel(cache);
		if(pem == null)
			return;
		
		Logger.log(Level.INFO, "ParticleEmitterModel : "+pem.getId());
		
		ParticleEmitter pe = cache.getParticleEmitter(pem.getEmitter());
		if(pe == null)
			return;
		Mesh mesh = cache.getMesh(pe.getMesh());
		if(mesh == null)
			return;
		
		mesh.bind();
		
		Material material = cache.getMaterial(pe.getMaterial());
		if(material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		if(shader == null)
			return;
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
		material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, pem.getTransform().getMatrix());
		
		PointLightSurfaceComponent plsc = pec.getParent().getComponent(PointLightSurfaceComponent.class);
		if(plsc != null)
			plsc.bindLights(cache, scene.getLights(), material);
		
		material.bindProperties(cache, scene, shader);
		
		pe.getMatrices().bind();
		
		if(shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		pe.getMatrices().enable();
		GL40.glDrawArraysInstanced(GL40.GL_TRIANGLES, 0, mesh.getIndicesCount(), pe.getParticleCount());
		
		GL40.glDisable(GL40.GL_BLEND);
		
		pe.getMatrices().unbind();
		
		// debug only
		//GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, c.getTransform().getMatrix());
		
		mesh.unbind();
		
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, pem.getTransform().getMatrix());
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
}
