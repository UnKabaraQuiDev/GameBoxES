package lu.pcy113.pdr.engine.graph.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class ModelRenderer extends Renderer<Scene3D, Model> {

	public ModelRenderer() {
		super(Model.class);
	}

	@Override
	public void render(CacheManager cache, Scene3D scene, Model model) {
		System.out.println("Model : "+model.getId());
		
		Mesh mesh = cache.getMesh(model.getMesh());
		if(mesh == null)
			return;
		
		/*MeshRenderer meshRender = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		meshRender.render(cache, scene, mesh);*/
		
		mesh.bind();
		
		Material material = cache.getMaterial(mesh.getMaterial());
		Shader shader = cache.getShader(material.getShader());
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		material.setProperty(Shader.TRANSFORMATION_MATRIX, model.getTransform().getMatrix());
		
		material.bindLights(cache, scene.getPointLights());
		
		material.bindProperties(cache, scene, shader);
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		// debug only
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, model.getTransform().getMatrix());
		
		mesh.unbind();
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, model.getTransform().getMatrix());
	}
	
	@Override
	public void cleanup() {}
	
}
