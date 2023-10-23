package lu.pcy113.pdr.engine.graph.material.wireframe;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class WireframeShader extends Shader {
	
	public static final String NAME = WireframeShader.class.getName();
	
	public static final String COLOR = "color";
	
	public WireframeShader() {
		super(NAME,
				new ShaderPart("./resources/shaders/debug/debug.vert"),
				new ShaderPart("./resources/shaders/debug/debug.frag"));
	}
	
	@Override
	public void createUniforms() {
		// vert
		getUniform(Shader.PROJECTION_MATRIX);
		getUniform(Shader.VIEW_MATRIX);
		getUniform(Shader.TRANSFORMATION_MATRIX);
		// frag
		getUniform(COLOR);
	}

}
