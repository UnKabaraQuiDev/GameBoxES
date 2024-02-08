package lu.pcy113.pdr.engine.graph.composition.color_filter;

import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class ColorFilterShader extends RenderShader {

	public static final String NAME = ColorFilterShader.class.getName();

	public static final String MUL = "mul";
	public static final String ADD = "add";

	public ColorFilterShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/composite/plain.vert"), AbstractShaderPart.load("./resources/shaders/composite/color/filter.frag"));
	}

	@Override
	public void createUniforms() {
		getUniform(MUL);
		getUniform(ADD);
	}

}
