package lu.kbra.gamebox.client.es.game.game.render.shaders;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class FillShader extends RenderShader {

	public static final String NAME = FillShader.class.getName();

	public static final String COLOR = "color";

	public FillShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/plain.vert"), AbstractShaderPart.load("./resources/shaders/plain.frag"));
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

		public FillMaterial(Vector4f color) {
			super(NAME, new FillShader());
			this.color = color;
		}
		
		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(COLOR, color);
			
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
