package lu.pcy113.pdr.client.game.three;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.impl.Renderable;

public class FillShader extends Shader {

	public static final String COLOR = "color";

	public FillShader() {
		super(FillShader.class.getName(), new ShaderPart("./resources/shaders/composite/plain.vert"), new ShaderPart("./resources/shaders/composite/fill/fill-color.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(COLOR);
	}

	public static class FillMaterial extends Material {

		private Vector4f color;

		public FillMaterial() {
			super(FillMaterial.class.getName(), new FillShader());
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
			super.setProperty(COLOR, color);

			super.bindProperties(cache, parent, shader);
		}

		public Vector4f getColor() {
			return color;
		}

		public void setColor(Vector4f color) {
			this.color = color;
		}

	}

}
