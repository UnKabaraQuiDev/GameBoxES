package lu.pcy113.pdr.engine.graph.render;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public abstract class Renderer<T, K> implements UniqueID, Cleanupable {
	
	private final String clazz;
	
	public Renderer(Class<?> clas) {
		this.clazz = clas.getName();
	}
	
	public abstract void render(CacheManager cache, T parent, K obj);
	
	@Override
	public String getId() {return clazz;}
	
}
