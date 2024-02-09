package lu.pcy113.pdr.engine.logic;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;

public abstract class GameLogic {
	
	protected GameEngine engine;
	protected SharedCacheManager cache;
	protected Window window;
	
	public void register(GameEngine e) {
		if(this.engine != null)
			throw new IllegalStateException("Already registered");
		
		this.engine = e;
		
		this.cache = e.getCache();
		this.window = e.getWindow();
	}
	
	public abstract void init(GameEngine e);

	public abstract void input(float dTime);

	public abstract void update(float dTime);

	public abstract void render(float dTime);
	
	protected boolean waitForFrameEnd() {
		return engine.waitForFrameEnd();
	}
	protected boolean waitForUpdateEnd() {
		return engine.waitForUpdateEnd();
	}
	protected void stop() {
		engine.stop();
	}

}
