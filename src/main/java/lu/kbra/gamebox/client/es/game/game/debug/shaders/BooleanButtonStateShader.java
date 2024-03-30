package lu.kbra.gamebox.client.es.game.game.debug.shaders;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class BooleanButtonStateShader extends RenderShader {

	public static final String NAME = BooleanButtonStateShader.class.getName();

	public static final String COLOR = "color";
	public static final String VALUE = "value";

	public BooleanButtonStateShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/ui/boolean_button.frag"), AbstractShaderPart.load("./resources/shaders/ui/plain.vert"));
	}

	@Override
	public void createUniforms() {
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		createUniform(RenderShader.VIEW_POSITION);

		createUniform(COLOR);
		createUniform(VALUE);
	}

	public static class BooleanButtonStateMaterial extends Material {

		public static final String NAME = BooleanButtonStateMaterial.class.getName();

		private float value;
		private Vector4f color;

		public BooleanButtonStateMaterial() {
			super(NAME, new BooleanButtonStateShader());
		}

		public BooleanButtonStateMaterial(BooleanButtonStateShader shader) {
			super(NAME, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(COLOR, color);
			setProperty(VALUE, value);

			super.bindProperties(cache, parent, shader);
		}

		public void setValue(float value) {
			this.value = value;
		}

		public void setColor(Vector4f color) {
			this.color = color;
		}

	}

}
