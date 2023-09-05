package lu.pcy113.pdr.client.game;

import java.util.Map;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;

public class SceneMaterial extends TextureMaterial {
	
	public SceneMaterial(String shader, Map<String, String> txts) {
		super("scene", shader, txts);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Shader shader) {
		properties.put("t", (float) Math.random()/100);
		super.bindProperties(cache, shader);
	}
	
}
