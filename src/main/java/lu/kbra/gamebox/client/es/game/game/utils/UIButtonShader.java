package lu.kbra.gamebox.client.es.game.game.utils;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class UIButtonShader extends RenderShader {

	public static final String NAME = UIButtonShader.class.getName();

	private static final String TXT1 = "txt1";

	public UIButtonShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/button.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);
	}

	public static class UIButtonMaterial extends TextureMaterial {

		public static final String NAME = UIButtonMaterial.class.getName();

		public UIButtonMaterial(SingleTexture texture) {
			super(NAME, new UIButtonShader(), new HashMap<String, Texture>() {
				{
					put("txt1", texture);
				}
			});
		}
		
		public UIButtonMaterial(RenderShader shader, SingleTexture texture) {
			super(NAME, shader, new HashMap<String, Texture>() {
				{
					put("txt1", texture);
				}
			});
		}

	}

}
