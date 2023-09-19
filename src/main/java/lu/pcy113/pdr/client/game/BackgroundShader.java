package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class BackgroundShader extends Shader {
	
	public static final String NAME = BackgroundShader.class.getName();
	
	public static final String HUE = "hue";
	
	public BackgroundShader(int index) {
		super(NAME+"-"+index,
				new ShaderPart("./resources/shaders/background/background-"+index+".frag"),
				new ShaderPart("./resources/shaders/background/background-"+index+".vert"));
	}
	
	@Override
	public void createUniforms() {
		// vert
		// None
		// frag
		getUniform(HUE);
	}

}
