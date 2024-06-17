package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class WorldParticleShader extends RenderShader {

	public static final String NAME = WorldParticleShader.class.getName();

	public static final String TXT1 = "txt1";
	public static final String GRID_COLUMNS = "columns";
	public static final String GRID_ROWS = "rows";
	public static final String OPACITY = "opacity";

	public WorldParticleShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/world/particle/instance.vert"), AbstractShaderPart.load("./resources/shaders/world/particle/instance.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);
		createUniform(GRID_COLUMNS);
		createUniform(GRID_ROWS);
		createUniform(OPACITY);
	}

	public abstract static class WorldParticleMaterial extends TextureMaterial {

		// public static final String NAME = WorldParticleMaterial.class.getName();

		public WorldParticleMaterial(String name, WorldParticleShader shader, SingleTexture texture, int columns, int rows) {
			super(name, shader, new HashMap<String, Texture>() {
				{
					put(TXT1, texture);
				}
			});

			setProperty(GRID_COLUMNS, columns);
			setProperty(GRID_ROWS, rows);
		}

		public WorldParticleMaterial(String name, SingleTexture texture, int column, int row) {
			this(name, new WorldParticleShader(), texture, column, row);
		}

	}

}
