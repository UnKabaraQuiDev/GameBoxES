package lu.kbra.gamebox.client.es.engine.graph.material.text;

import java.util.HashMap;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class TextShader extends RenderShader {

	public static final String NAME = TextShader.class.getName();

	public static final String TXT1 = "txt1";
	public static final String FG_COLOR = "fgColor";
	public static final String BG_COLOR = "bgColor";
	public static final String TXT_LENGTH = "length";

	public TextShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/text/text.vert"), AbstractShaderPart.load("./resources/shaders/text/text.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);

		createUniform(FG_COLOR);
		createUniform(BG_COLOR);

		createUniform(TXT_LENGTH);
	}

	public static class TextMaterial extends TextureMaterial {

		public static final String NAME = TextMaterial.class.getName();

		private Vector4f fgColor = new Vector4f(1, 1, 1, 1);
		private Vector4f bgColor = new Vector4f(0, 0, 0, 0);

		public TextMaterial(SingleTexture txt1) {
			super(NAME, new TextShader(), new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene, RenderShader shader) {
			setProperty(FG_COLOR, fgColor);
			setProperty(BG_COLOR, bgColor);

			super.bindProperties(cache, scene, shader);
		}

		public void setFgColor(Vector4f fgColor) {
			this.fgColor = fgColor;
		}

		public Vector4f getFgColor() {
			return fgColor;
		}

		public void setBgColor(Vector4f bgColor) {
			this.bgColor = bgColor;
		}

		public Vector4f getBgColor() {
			return bgColor;
		}

	}

}
