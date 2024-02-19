package lu.pcy113.pdr.engine.impl;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.impl.nexttask.NextTask;

public abstract class GameLogic {
	
	protected GameEngine engine;
	protected SharedCacheManager cache;
	protected Window window;
	
	public void register(GameEngine e) {
		if (this.engine != null)
			throw new IllegalStateException("Already registered");
		
		System.out.println("registered");
		
		this.engine = e;
		
		this.cache = e.getCache();
		this.window = e.getWindow();
	}
	
	public abstract void init(GameEngine e);
	
	public abstract void input(float dTime);
	
	public abstract void update(float dTime);
	
	public abstract void render(float dTime);
	
	protected NextTask createTask(int target) {
		return engine.createTask(target);
	}
	
	/*protected boolean pushTask(NextTask nt) {
		return engine.pushTask(nt);
	}*/
	
	protected boolean waitForFrameEnd() {
		return engine.waitForFrameEnd();
	}
	
	protected boolean waitForUpdateEnd() {
		return engine.waitForUpdateEnd();
	}
	
	protected boolean waitForFrameStart() {
		return engine.waitForFrameStart();
	}
	
	protected boolean waitForUpdateStart() {
		return engine.waitForUpdateStart();
	}
	
	protected void stop() {
		engine.stop();
	}
	
}
