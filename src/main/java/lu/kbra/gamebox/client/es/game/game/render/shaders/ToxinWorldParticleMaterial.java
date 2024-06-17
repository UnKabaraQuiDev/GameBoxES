package lu.kbra.gamebox.client.es.game.game.render.shaders;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader.WorldParticleMaterial;

public class ToxinWorldParticleMaterial extends WorldParticleMaterial {

	public static final String NAME = ToxinWorldParticleMaterial.class.getName();
	
	public static final String TEXTURE_NAME = "toxins-png";
	public static final String TEXTURE_PATH = "./resources/textures/toxins-1.png";

	public static final int COLUMN_COUNT = 1;
	public static final int ROW_COUNT = 5;

	public ToxinWorldParticleMaterial(SingleTexture texture) {
		super(NAME, texture, COLUMN_COUNT, ROW_COUNT);
	}
	
	public ToxinWorldParticleMaterial(WorldParticleShader shader, SingleTexture texture) {
		super(NAME, shader, texture, COLUMN_COUNT, ROW_COUNT);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable scene, RenderShader shader) {
		super.setProperty(WorldParticleShader.OPACITY, 0.55f);
		
		super.bindProperties(cache, scene, shader);
	}

}
