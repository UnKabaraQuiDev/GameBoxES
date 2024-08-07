package lu.kbra.gamebox.client.es.engine.impl;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.audio.AudioMaster;
import lu.kbra.gamebox.client.es.engine.cache.SharedCacheManager;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTask;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTaskEnvironnment;

public abstract class GameLogic {

	public GameEngine engine;
	public SharedCacheManager cache;
	public Window window;
	public AudioMaster audio;

	public void register(GameEngine e) {
		/*
		 * if (this.engine != null) throw new
		 * IllegalStateException("Already registered");
		 */

		this.engine = e;

		this.cache = e.getCache();
		this.window = e.getWindow();

		this.audio = e.getAudioMaster();
	}

	public abstract void init(GameEngine e);

	public void updateInit() {
	}

	public abstract void input(float dTime);

	public abstract void update(float dTime);

	public abstract void render(float dTime);

	public <I, B, C> NextTask<I, B, C> createTask(int target) {
		return engine.<I, B, C>createTask(target);
	}

	public <I, B, C> NextTask<I, B, C> createTask(int from, int target) {
		return engine.<I, B, C>createTask(from, target);
	}

	public NextTaskEnvironnment getTaskEnvironnment() {
		return engine.getTaskEnvironnment();
	}

	/*
	 * protected boolean pushTask(NextTask nt) { return engine.pushTask(nt); }
	 */

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

	public void stop() {
		engine.stop();
	}

}
