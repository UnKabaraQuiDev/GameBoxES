package lu.kbra.gamebox.client.es.game.game.debug;

import lu.kbra.gamebox.client.es.engine.graph.composition.PassRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

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
		
		public BoxBlurMaterial(BoxBlurShader shader) {
			super(NAME, shader);
		}

	}

}
