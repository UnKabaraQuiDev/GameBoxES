package lu.pcy113.pdr.client.game.three;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class FourButtonStateShader extends RenderShader {

	public static final String NAME = FourButtonStateShader.class.getName();

	public static final String BUTTONS = "buttons";

	public FourButtonStateShader() {
		super(NAME, AbstractShaderPart.load(
				"./resources/shaders/ui/four_button.frag"),
				AbstractShaderPart.load(
						"./resources/shaders/ui/plain.vert"));
	}

	@Override
	public void createUniforms() {
		createUniform(
				RenderShader.PROJECTION_MATRIX);
		createUniform(
				RenderShader.VIEW_MATRIX);
		createUniform(
				RenderShader.TRANSFORMATION_MATRIX);
		createUniform(
				RenderShader.VIEW_POSITION);

		createUniform(
				BUTTONS);
	}

	public static class FourButtonStateMaterial extends Material {

		public static final String NAME = FourButtonStateMaterial.class.getName();

		private Vector4f buttons;

		public FourButtonStateMaterial() {
			super(NAME, new FourButtonStateShader());
		}

		public FourButtonStateMaterial(RenderShader shader) {
			super(NAME, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(
					BUTTONS,
					buttons);

			super.bindProperties(
					cache,
					parent,
					shader);
		}

		public void setButtons(Vector4f buttons) {
			this.buttons = buttons;
		}

	}

}
