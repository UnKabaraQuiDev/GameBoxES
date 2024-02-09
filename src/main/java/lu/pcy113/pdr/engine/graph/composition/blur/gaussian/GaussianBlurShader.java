package lu.pcy113.pdr.engine.graph.composition.blur.gaussian;

import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class GaussianBlurShader extends RenderShader {

	public static final String NAME = GaussianBlurShader.class.getName();

	public static final String TEXTURE = "input"; // txt 0
	public static final String DEPTH = "depth"; // txt 1

	public static final String KERNEL = "kernel";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	public GaussianBlurShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/composite/plain.vert"), AbstractShaderPart.load("./resources/shaders/composite/blur/gaussian.frag"));
	}

	@Override
	public void createUniforms() {
		// frag
		// None
		// vert
		createUniform(TEXTURE);
		createUniform(DEPTH);
		createUniform(KERNEL);
		createUniform(WIDTH);
		createUniform(HEIGHT);
		createUniform(PassRenderLayer.SCREEN_WIDTH);
		createUniform(PassRenderLayer.SCREEN_HEIGHT);
	}

}
