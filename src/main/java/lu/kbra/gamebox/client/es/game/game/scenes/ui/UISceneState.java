package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;

public abstract class UISceneState implements Cleanupable, UniqueID {

	private String name;

	protected UIScene3D scene;
	protected CacheManager cache;
	protected Window window;

	public UISceneState(String name, UIScene3D scene) {
		this.name = name;
		this.scene = scene;
		this.cache = new CacheManager(scene.getCache().getId()+"-"+name, scene.getCache());
		this.window = scene.getWindow();
	}

	public abstract void input(float dTime);
	public abstract void update(float dTime);
	public abstract void render(float dTime);

	public CacheManager getCache() {
		return cache;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: "+name);
		
		cache.cleanup();
	}

	@Override
	public String getId() {
		return name;
	}

}
