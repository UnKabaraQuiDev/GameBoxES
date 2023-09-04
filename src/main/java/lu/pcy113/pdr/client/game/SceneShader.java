package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class SceneShader extends Shader {
	
	public SceneShader() {
		super("scene",
				new ShaderPart("./resources/shaders/scene/scene.vert"),
				new ShaderPart("./resources/shaders/scene/scene.frag"));
	}
	
	@Override
	public void createUniforms() {
		getUniform("projectionMatrix");
		getUniform("modelMatrix");
		getUniform("t");
	}

}
