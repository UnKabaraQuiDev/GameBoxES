package lu.pcy113.pdr.client.game.three;

import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class BoxBlurShader extends RenderShader {

	public static final String NAME = BoxBlurShader.class.getName();

	public BoxBlurShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/composite/plain.vert"), AbstractShaderPart.load("./resources/shaders/composite/blur/box.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(PassRenderLayer.SCREEN_HEIGHT);
		createUniform(PassRenderLayer.SCREEN_WIDTH);

		createUniform("color");
		createUniform("depth");
	}

	public static class BoxBlurMaterial extends Material {

		public static final String NAME = BoxBlurMaterial.class.getName();

		public BoxBlurMaterial() {
			super(NAME, new BoxBlurShader());
		}

	}

}
