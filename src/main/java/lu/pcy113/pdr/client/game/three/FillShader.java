package lu.pcy113.pdr.client.game.three;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class FillShader extends RenderShader {
	
	public static final String NAME = FillShader.class.getName();
	
	public static final String COLOR = "color";

	public FillShader() {
		super(NAME,
				AbstractShaderPart.load("./resources/shaders/composite/plain.vert"),
				AbstractShaderPart.load("./resources/shaders/composite/fill/fill-color.frag"));
	}

	@Override
	public void createUniforms() {
		super.createSceneUniforms();
		
		createUniform(COLOR);
	}

	public static class FillMaterial extends Material {
		
		public static final String NAME = FillMaterial.class.getName();
		
		private Vector4f color;

		public FillMaterial() {
			super(NAME, new FillShader());
		}
		public FillMaterial(FillShader shader) {
			super(NAME, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
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
