package lu.kbra.gamebox.client.es.game.game.shaders;

import lu.kbra.gamebox.client.es.engine.graph.composition.PassRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class BrightnessFilterShader extends RenderShader {
	
	public static final String NAME = BrightnessFilterShader.class.getName();

	public static final String MIN_LUM = "min_lum";

	public BrightnessFilterShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/composite/plain.vert"), AbstractShaderPart.load("./resources/shaders/composite/filter/brightness.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(PassRenderLayer.SCREEN_HEIGHT);
		createUniform(PassRenderLayer.SCREEN_WIDTH);

		createUniform("color");
		createUniform("depth");

		createUniform(MIN_LUM);
	}

	public static class BrightnessFilterMaterial extends Material {

		public static final String NAME = BrightnessFilterMaterial.class.getName();

		public BrightnessFilterMaterial(Float minLum) {
			this(new BrightnessFilterShader(), minLum);
		}

		public BrightnessFilterMaterial(BrightnessFilterShader shader, float minLum) {
			super(NAME, shader);
			
			setMinBrightness(minLum);
		}
		
		public void setMinBrightness(float minLum) {
			setProperty(MIN_LUM, minLum);
		}

	}

}
