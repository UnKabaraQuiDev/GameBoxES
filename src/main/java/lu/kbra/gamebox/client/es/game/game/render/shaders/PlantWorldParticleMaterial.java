package lu.kbra.gamebox.client.es.game.game.render.shaders;

import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader.WorldParticleMaterial;

public class PlantWorldParticleMaterial extends WorldParticleMaterial {

	public static final String NAME = PlantWorldParticleMaterial.class.getName();
	
	public static final String TEXTURE_NAME = "toxins-png";
	public static final String TEXTURE_PATH = "./resources/textures/toxins.png";

	public static final int COLUMN_COUNT = 5;
	public static final int ROW_COUNT = 5;

	public PlantWorldParticleMaterial(SingleTexture texture) {
		super(NAME, texture, COLUMN_COUNT, ROW_COUNT);
	}
	
	public PlantWorldParticleMaterial(WorldParticleShader shader, SingleTexture texture) {
		super(NAME, shader, texture, COLUMN_COUNT, ROW_COUNT);
	}

}
