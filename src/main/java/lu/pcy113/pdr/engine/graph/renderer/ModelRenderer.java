package lu.pcy113.pdr.engine.graph.renderer;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.scene.geom.Mesh;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.utils.Logger;

public class ModelRenderer extends Renderer<Model> {
	
	@Override
	public void render(Window window, Model model) {
		super.render(window, model);
		
		model.getTransform().translate(0, 0, -0.02f);
		model.getTransform().updateMatrix();
		
		Logger.log("Model: "+model.getId()+" > Me"+model.getMeshes().size()+" & TPos"+model.getTransform().getPosition()+" & TRot"+model.getTransform().getRotation()+" & TSc"+model.getTransform().getScale());
		Material material = model.getMaterial();
		Logger.log("Material: "+(material.getTexture() == null ? "null" : material.getTexture().getPath())+" > Sh"+material.getShader().getProgramId());
		ShaderProgram shader = material.getShader();
		
		UniformsMap uniforms = model.bind();
		uniforms.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
		
		for(Mesh mesh : model.getMeshes()) {
			Logger.log("Mesh: "+mesh.getVaoId()+" > V"+mesh.getNumVertices());
			
			mesh.bind(uniforms);
			
			Logger.log("Uniforms: "+uniforms.toString());
			
			GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0);
		}
		
		GL30.glBindVertexArray(0);
		shader.unbind();
	}
	
}
