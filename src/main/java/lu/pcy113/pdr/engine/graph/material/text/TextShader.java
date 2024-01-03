package lu.pcy113.pdr.engine.graph.material.text;

import java.util.HashMap;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Renderable;

public class TextShader extends Shader {

	public static final String NAME = TextShader.class.getName();

	public static final String TXT1 = "txt1";
	public static final String FG_COLOR = "fgColor";
	public static final String BG_COLOR = "bgColor";
	public static final String TXT_LENGTH = "length";

	public TextShader() {
		super(NAME, true, new ShaderPart("./resources/shaders/text/text.vert"), new ShaderPart("./resources/shaders/text/text.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(TRANSFORMATION_MATRIX);
		createUniform(VIEW_MATRIX);
		createUniform(PROJECTION_MATRIX);
		createUniform(VIEW_POSITION);

		createUniform(TXT1);

		createUniform(FG_COLOR);
		createUniform(BG_COLOR);

		createUniform(TXT_LENGTH);
	}

	public static class TextMaterial extends TextureMaterial {

		public static final String NAME = TextMaterial.class.getName();

		private Vector4f fgColor = new Vector4f(1, 1, 1, 1);
		private Vector4f bgColor = new Vector4f(0, 0, 0, 1);

		public TextMaterial(Texture txt1) {
			super(NAME, new TextShader(), new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
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
