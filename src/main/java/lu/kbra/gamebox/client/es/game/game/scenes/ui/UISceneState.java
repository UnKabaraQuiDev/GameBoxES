package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;

public abstract class UISceneState implements Cleanupable {
	
	protected UIScene3D scene;
	protected CacheManager cache;
	protected Window window;
	
	public UISceneState(UIScene3D scene) {
		this.scene = scene;
		this.cache = scene.getCache();
		this.window = scene.getWindow();
	}
	
	public abstract void input(float dTime);
	
	@Override
	public void cleanup() {
		cache.cleanup();
	}
	
}
