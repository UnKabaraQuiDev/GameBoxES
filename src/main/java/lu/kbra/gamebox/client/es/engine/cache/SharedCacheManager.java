package lu.kbra.gamebox.client.es.engine.cache;

public class SharedCacheManager extends CacheManager {

	public SharedCacheManager(CacheManager parent) {
		super(parent);
	}

	public SharedCacheManager(String name, CacheManager parent) {
		super(name, parent);
	}

	public SharedCacheManager(String name) {
		super(name);
	}
	
}
