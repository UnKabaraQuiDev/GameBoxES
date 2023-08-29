package lu.pcy113.pdr.engine.graph.renderer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.scene.geom.Mesh;
import lu.pcy113.pdr.engine.scene.geom.MeshInstance;
import lu.pcy113.pdr.engine.scene.geom.ModelInstance;
import lu.pcy113.pdr.utils.Logger;

public class ModelInstanceRenderer extends Renderer<ModelInstance> {
	
	@Override
	public void render(Window window, ModelInstance model) {
		super.render(window, model);
		
		Logger.log("ModelInstance: "+model.getId()+" > Me"+model.getMeshes().size()+" & C"+model.getCount());
		Material material = model.getMaterial();
		Logger.log("Material: "+(material.getTexture() == null ? "null" : material.getTexture().getPath())+" > Sh"+material.getShader().getProgramId());
		ShaderProgram shader = material.getShader();
		
		UniformsMap uniforms = model.bind();
		uniforms.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
		
		/*int instanceVBO = GL30.glGenBuffers();
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, instanceVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, instanceVBO, GL30.GL_STATIC_DRAW);
		GL30.glEnableVertexAttribArray(2);
		GL30.glVertexAttribPointer(1, 4*4, GL30.GL_FLOAT_MAT4, false, 0, 0);*/
		
		/*for(int i = 0; i < model.getCount(); i++) {
			if(uniforms.hasUniform("modelMatrix[]"))
				uniforms.setUniform("modelMatrix["+i+"]", model.getTransforms().get(i).getMatrix());
		}*/
		
		for(MeshInstance mesh : model.getMeshes()) {
			Logger.log("MeshInstance: "+mesh.getVaoId()+" > V"+mesh.getNumVertices());
			
			mesh.bind(uniforms);
			
			Logger.log("Uniforms: "+uniforms.toString());
			
			GL33.glDrawElementsInstanced(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0l, model.getCount());
		}
			
		GL30.glBindVertexArray(0);
		shader.unbind();
	}
	
}
