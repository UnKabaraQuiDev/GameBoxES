package lu.pcy113.pdr.engine.graph.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

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
	public void render(CacheManager cache, Scene3D scene, Mesh obj) {
		System.out.println("Mesh : "+obj.getId()+", vao:"+obj.getVao()+", vec:"+obj.getVertexCount()+", vbo:"+obj.getVbo());
		
		obj.bind();
		
		Material material = cache.getMaterial(obj.getMaterial());
		Shader shader = cache.getShader(material.getShader());
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		material.setProperty("projectionMatrix", projectionMatrix);
		material.bindProperties(shader);
		
		//GL30.glUniformMatrix4fv(obj.getProjectionMatrixLocation(), false, projectionMatrix);
		
		GL30.glDrawElements(GL30.GL_TRIANGLES, obj.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
		
		obj.unbind();
	}

}
