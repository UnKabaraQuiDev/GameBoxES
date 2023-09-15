package lu.pcy113.pdr.engine.graph.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class MeshRenderer extends Renderer<Scene3D, Mesh> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public void render(CacheManager cache, Scene3D scene, Mesh mesh) {
		System.out.println("Mesh : "+mesh.getId()+", vao:"+mesh.getVao()+", vec:"+mesh.getVertexCount()+", vbo:"+mesh.getVbo());
		
		mesh.bind();
		
		Material material = cache.getMaterial(mesh.getMaterial());
		Shader shader = cache.getShader(material.getShader());
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		material.bindProperties(cache, scene, shader);
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		// debug only
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, null);
		
		mesh.unbind();
	}

}
