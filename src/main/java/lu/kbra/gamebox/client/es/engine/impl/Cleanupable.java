package lu.kbra.gamebox.client.es.engine.impl;

public interface Cleanupable extends AutoCloseable {

	void cleanup();
	
	@Override
	default void close() {
		cleanup();
	}

}
