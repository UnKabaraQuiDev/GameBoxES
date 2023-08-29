package lu.pcy113.pdr.engine.graph.shader;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class TextureCache implements Cleanupable {
	
	public static final String DEFAULT_TEXTURE = "default.png";
	
	private Map<String, Texture> textures;
	
	public TextureCache() {
		textures = new HashMap<>();
		textures.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE));
	}
	
	@Override
	public void cleanup() {
		textures.values().forEach(Texture::cleanup);
	}
	
	public Texture createTexture(String path) {
		Logger.log();
		
		return textures.computeIfAbsent(path, Texture::new);
	}
	
	public Texture getTexture(String path) {
		Texture te = null;
		if(path != null) {
			te = textures.get(path);
		}
		if(te == null) {
			te = textures.get(DEFAULT_TEXTURE);
		}
		return te;
	}
	
}
