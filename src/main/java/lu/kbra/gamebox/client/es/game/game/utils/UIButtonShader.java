package lu.kbra.gamebox.client.es.game.game.utils;

import java.util.Map;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class UIButtonShader extends RenderShader {

	public UIButtonShader(String name) {
		super(name, true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/button.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();
	}
	
	public class UIButtonMaterial extends TextureMaterial {

		public UIButtonMaterial(String name, RenderShader shader, Map<String, Texture> textures) {
			super(name, shader, textures);
		}

	}

}
