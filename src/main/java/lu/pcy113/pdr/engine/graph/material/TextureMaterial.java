package lu.pcy113.pdr.engine.graph.material;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Renderable;

public class TextureMaterial
		extends
		Material {

	private Map<String, String> textures;

	public TextureMaterial(String name, Shader shader, Map<String, String> textures) {
		super(name, shader);

		this.textures = textures;
		int i = 0;
		for (Entry<String, String> txt : textures.entrySet()) {
			properties.put(txt.getKey(), i++);
		}
	}

	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		super.bindProperties(cache, scene, shader);

		int i = 0;
		for (Entry<String, String> txt : textures.entrySet()) {
			Texture texture = cache.getTexture(txt.getValue());
			if (texture == null) {
				GlobalLogger.log(Level.WARNING, "Could not find texture: " + txt.getValue());
				continue;
			}
			texture.bind(i++);
		}
	}

}
