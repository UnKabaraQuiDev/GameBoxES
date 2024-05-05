package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class BackgroundShader extends RenderShader {
	
	public static final String NAME = BackgroundShader.class.getName();
	public static final String TXT1 = "txt1";
	
	public BackgroundShader() {
		super(NAME, false, AbstractShaderPart.load("./resources/shaders/plain.vert"), AbstractShaderPart.load("./resources/shaders/world/background.frag"));
	}
	
	@Override
	public void createUniforms() {
		createSceneUniforms();
		
		createUniform(TXT1);
	}

	public static class BackgroundMaterial extends TextureMaterial {

		public static final String NAME = BackgroundMaterial.class.getName();

		public BackgroundMaterial(SingleTexture texture) {
			super(NAME, new BackgroundShader(), new HashMap<String, Texture>() {
				{
					put(TXT1, texture);
				}
			});
		}
		
		public Texture setTexture(SingleTexture bg) {
			return super.setTexture(TXT1, bg);
		}

	}

}
