package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class SceneShader extends Shader {
	
	public SceneShader() {
		super("scene",
				new ShaderPart("./resources/shaders/instances/instances.vert"),
				new ShaderPart("./resources/shaders/instances/instances.frag"));
	}
	
	@Override
	public void createUniforms() {
		getUniform("projectionMatrix");
		getUniform("viewMatrix");
		getUniform("modelMatrix");
		getUniform("t");
		getUniform("txtSampler");
	}

}
