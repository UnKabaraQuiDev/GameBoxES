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

	private Map<String, Texture> textures;

	public TextureMaterial(String name, Shader shader, Map<String, Texture> textures) {
		super(name, shader);

		this.textures = textures;
		int i = 0;
		for (Entry<String, Texture> txt : textures.entrySet()) {
			properties.put(txt.getKey(), i++);
			//this.textures.put(txt.getKey(), new Texture(txt.getValue()));
		}
	}

	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		super.bindProperties(cache, scene, shader);

		int i = 0;
		for (Entry<String, Texture> txt : textures.entrySet()) {
			Texture texture = txt.getValue();
			if (texture == null) {
				GlobalLogger.log(Level.WARNING, "Could not find texture: " + txt.getKey());
				continue;
			}
			texture.bind(i++);
		}
	}

}
