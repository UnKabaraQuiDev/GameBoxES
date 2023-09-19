package lu.pcy113.pdr.engine.graph.composition.blur.gaussian;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class GaussianBlurShader extends Shader {
	
	public static final String NAME = GaussianBlurShader.class.getName();
	
	public static final String TEXTURE = "input"; // txt 0
	public static final String DEPTH = "depth";   // txt 1
	
	public static final String KERNEL = "kernel";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	
	public GaussianBlurShader() {
		super(NAME,
				new ShaderPart("./resources/shaders/blur/gaussian.vert"),
				new ShaderPart("./resources/shaders/blur/gaussian.frag"));
	}

	@Override
	public void createUniforms() {
		// frag
		// None
		// vert
		getUniform(TEXTURE);
		getUniform(DEPTH);
		getUniform(KERNEL);
		getUniform(WIDTH);
		getUniform(HEIGHT);
	}

}
