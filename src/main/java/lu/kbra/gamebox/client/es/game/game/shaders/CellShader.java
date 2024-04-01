package lu.kbra.gamebox.client.es.game.game.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class CellShader extends RenderShader {

	public static final String NAME = CellShader.class.getName();

	public static final String TEXTURE = "color";

	public CellShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/cell/cell.frag"),
				AbstractShaderPart.load("./resources/shaders/plain.vert"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TEXTURE);
	}

	public static class CellMaterial extends TextureMaterial {

		public CellMaterial(String name, CellShader shader, Texture color) {
			super(name, shader, new HashMap<String, Texture>() {
				{
					put(TEXTURE, color);
				}
			});
		}

		public CellMaterial(String name, Texture color) {
			this(name, new CellShader(), color);
		}

	}

}
