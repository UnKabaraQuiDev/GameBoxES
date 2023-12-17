package lu.pcy113.pdr.engine.graph.render;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

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
