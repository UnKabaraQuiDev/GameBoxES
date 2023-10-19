package lu.pcy113.pdr.engine.graph.material.text;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class TextShader extends Shader {
	
	public TextShader(String name) {
		super(TextShader.class.getName()+"."+name, new ShaderPart[] {
			new ShaderPart("./resources/shaders/text/text.vert"),
			new ShaderPart("./resources/shaders/text/text.geo"),
			new ShaderPart("./resources/shaders/text/"+name+".frag")
		});
	}

	public static final String TXT_LENGTH = "length";
	public static final String TXT_BUFFER = "buffer";
	public static final String CHAR_SIZE = "size";

	@Override
	public void createUniforms() {
		// geo
		createUniform(TXT_LENGTH);
		createUniform(TXT_BUFFER);
		createUniform(CHAR_SIZE);
		
		// vert
		createUniform(Shader.PROJECTION_MATRIX);
		createUniform(Shader.VIEW_MATRIX);
		createUniform(Shader.TRANSFORMATION_MATRIX);
	}
	
}
