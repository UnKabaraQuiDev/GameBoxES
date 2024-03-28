package lu.kbra.gamebox.client.es.engine.graph.render;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;

public abstract class Renderer<T, K extends Renderable> implements UniqueID, Cleanupable {

	private String clazz;

	public Renderer(Class<?> clas) {
		this.clazz = clas.getName();
	}

	public abstract void render(CacheManager cache, T parent, K obj);

	@Override
	public void cleanup() {
		if (clazz != null) {
			clazz = null;
		}
	}

	@Override
	public String getId() {
		return clazz;
	}

}
