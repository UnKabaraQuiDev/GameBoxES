package lu.pcy113.pdr.engine.graph.render;

import java.util.Arrays;
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
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class ModelRenderer extends Renderer<Scene3D, ModelComponent> {

	public ModelRenderer() {
		super(Model.class);
	}

	@Override
	public void render(CacheManager cache, Scene3D scene, ModelComponent co) {
		Model c = co.getModel(cache);
		if(c == null)
			return;
		
		Logger.log(Level.INFO, "Model : "+c.getId());
		
		Mesh mesh = cache.getMesh(c.getMesh());
		if(mesh == null)
			return;
		
		/*MeshRenderer meshRender = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		meshRender.render(cache, scene, mesh);*/
		
		mesh.bind();
		
		Material material = cache.getMaterial(mesh.getMaterial());
		System.out.println(material);
		if(material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		System.out.println(shader);
		if(shader == null)
			return;
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
		material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, c.getTransform().getMatrix());
		
		PointLightSurfaceComponent plsc = co.getParent().getComponent(PointLightSurfaceComponent.class);
		if(plsc != null)
			plsc.bindLights(cache, scene.getLights(), material);
		
		material.bindProperties(cache, scene, shader);
		
		System.err.println(material.getProperties());
		System.err.println(Arrays.toString(mesh.getAttribs()));
		System.err.println(mesh.getVertices());
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		// debug only
		//GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, c.getTransform().getMatrix());
		
		mesh.unbind();
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, c.getTransform().getMatrix());
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
}
