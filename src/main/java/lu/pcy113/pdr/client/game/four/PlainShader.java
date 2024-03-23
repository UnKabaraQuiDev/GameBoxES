package lu.pcy113.pdr.client.game.four;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class PlainShader extends RenderShader {

	public static final String NAME = PlainShader.class.getName();

	public static final String COLOR = "color";

	public PlainShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/plain.vert"), AbstractShaderPart.load("./resources/shaders/plain.frag"));
	}

	@Override
	public void createUniforms() {
		super.createSceneUniforms();

		super.createUniform(COLOR);
	}

	public static class PlainMaterial extends Material {

		public static final String NAME = PlainMaterial.class.getName();

		private Vector4f color;

		public PlainMaterial() {
			super(NAME, new PlainShader());
		}

		public PlainMaterial(PlainShader shader) {
			super(NAME, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(COLOR, color);

			super.bindProperties(cache, parent, shader);
		}

		public void setColor(Vector4f color) {
			this.color = color;
		}

		public Vector4f getColor() {
			return color;
		}

	}

}
