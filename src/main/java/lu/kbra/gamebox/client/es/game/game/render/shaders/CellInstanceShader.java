package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class CellInstanceShader extends RenderShader {

	public static final String NAME = CellInstanceShader.class.getName();
	
	public static final String TXT1 = "txt1";
	public static final String GRID_COLUMNS = "columns";
	public static final String GRID_ROWS = "rows";
	
	public CellInstanceShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/world/cell/instance.vert"), AbstractShaderPart.load("./resources/shaders/world/cell/instance.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();
		
		createUniform(TXT1);
		createUniform(GRID_COLUMNS);
		createUniform(GRID_ROWS);
	}

	public static class CellInstanceMaterial extends TextureMaterial {

		public CellInstanceMaterial(String name, RenderShader shader, SingleTexture texture) {
			super(name, shader, new HashMap<String, Texture>() {
				{
					put(TXT1, texture);
				}
			});
		}

	}

}
