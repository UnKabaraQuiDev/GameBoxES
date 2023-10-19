package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class MeshRenderer extends Renderer<Scene3D, MeshComponent> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void render(CacheManager cache, Scene3D scene, MeshComponent m) {
		Mesh mesh = m.getMesh(cache);
		if(mesh == null)
			return;
		
		Logger.log(Level.INFO, "Mesh : "+mesh.getId()+", vao:"+mesh.getVao()+", vec:"+mesh.getVertexCount()+", vbo:"+mesh.getVbo());
		
		mesh.bind();
		
		Material material = cache.getMaterial(mesh.getMaterial());
		Shader shader = cache.getShader(material.getShader());
		
		shader.bind();
		
		Matrix4f projectionMatrix = null, viewMatrix = null;
		if(scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		}
		material.bindProperties(cache, scene, shader);
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		// debug only
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, null);
		
		mesh.unbind();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}

}
