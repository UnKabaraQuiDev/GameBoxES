package lu.kbra.gamebox.client.es.game.game.shaders;

import lu.kbra.gamebox.client.es.engine.graph.composition.PassRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class ScaleShader extends RenderShader {

	public static final String NAME = ScaleShader.class.getName();

	public static final String SCREEN_DEST_WIDTH = "screen_dest_width";
	public static final String SCREEN_DEST_HEIGHT = "screen_dest_height";

	public ScaleShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/composite/plain.vert"), AbstractShaderPart.load("./resources/shaders/composite/scale/scale.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(PassRenderLayer.SCREEN_HEIGHT);
		createUniform(PassRenderLayer.SCREEN_WIDTH);

		createUniform("color");
		createUniform("depth");

		createUniform(SCREEN_DEST_HEIGHT);
		createUniform(SCREEN_DEST_WIDTH);
	}

	public static class ScaleMaterial extends Material {

		public static final String NAME = ScaleMaterial.class.getName();

		public ScaleMaterial(Integer width, Integer height) {
			this(new ScaleShader(), width, height);
		}

		public ScaleMaterial(ScaleShader shader, int width, int height) {
			super(NAME, shader);

			setResolution(width, height);
		}

		public void setResolution(int width, int height) {
			setProperty(SCREEN_DEST_HEIGHT, height);
			setProperty(SCREEN_DEST_WIDTH, width);
		}

	}

}
