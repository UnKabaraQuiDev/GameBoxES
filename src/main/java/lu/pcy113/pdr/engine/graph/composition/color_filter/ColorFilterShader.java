package lu.pcy113.pdr.engine.graph.composition.color_filter;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class ColorFilterShader extends Shader {

	public static final String NAME = ColorFilterShader.class.getName();

	public static final String MUL = "mul";
	public static final String ADD = "add";

	public ColorFilterShader() {
		super(NAME, new ShaderPart("./resources/shaders/composite/plain.vert"),
				new ShaderPart("./resources/shaders/composite/color/filter.frag"));
	}

	@Override
	public void createUniforms() {
		getUniform(MUL);
		getUniform(ADD);
	}

}
