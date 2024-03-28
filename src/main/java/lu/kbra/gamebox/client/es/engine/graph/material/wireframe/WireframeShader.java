package lu.kbra.gamebox.client.es.engine.graph.material.wireframe;

import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class WireframeShader extends RenderShader {

	public static final String NAME = WireframeShader.class.getName();

	public static final String COLOR = "color";

	public WireframeShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/debug/debug.vert"), AbstractShaderPart.load("./resources/shaders/debug/debug.frag"));
	}

	@Override
	public void createUniforms() {
		// vert
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		// frag
		createUniform(COLOR);
	}

}
