package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class ModelRenderer extends Renderer<Scene, ModelComponent> {

	public ModelRenderer() {
		super(Model.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, ModelComponent co) {
		Model c = co.getModel(cache);
		if(c == null)
			return;
		
		Logger.log(Level.INFO, "Model : "+c.getId());
		
		Mesh mesh = cache.getMesh(c.getMesh());
		if(mesh == null)
			return;
		
		mesh.bind();
		
		Material material = cache.getMaterial(mesh.getMaterial());
		System.out.println("mat: "+material);
		if(material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		System.out.println("sha: "+shader);
		if(shader == null)
			return;
		
		shader.bind();
		
		Matrix4f projectionMatrix = null, viewMatrix = null;
		Object transformationMatrix = c.getTransform().getMatrix();
		if(scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, transformationMatrix);
		}
		
		if(scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = co.getParent().getComponent(PointLightSurfaceComponent.class);
			if(plsc != null)
				plsc.bindLights(cache, ((Scene3D) scene).getLights(), material);
		}
		
		material.bindProperties(cache, scene, shader);
		
		if(shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);
		System.out.println("drawn: "+mesh.getIndicesCount());
		
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
