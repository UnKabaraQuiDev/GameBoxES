package lu.pcy113.pdr.engine;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.audio.AudioMaster;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.utils.DebugOptions;

public class GameEngine
		implements
		Runnable,
		Cleanupable {

	private final Window window;
	private final AudioMaster audioMaster;
	private GameLogic gameLogic;

	private boolean running = false;
	private long startTime;

	public int targetUps, targetFps;

	public static DebugOptions DEBUG = new DebugOptions();

	private SharedCacheManager cache;

	public static Vector3f X_POS = new Vector3f(1, 0, 0);
	public static Vector3f X_NEG = new Vector3f(-1, 0, 0);
	public static Vector3f Y_POS = new Vector3f(0, 1, 0);
	public static Vector3f Y_NEG = new Vector3f(0, -1, 0);
	public static Vector3f Z_POS = new Vector3f(0, 0, 1);
	public static Vector3f Z_NEG = new Vector3f(0, 0, -1);

	public static Vector3f UP = new Vector3f(Z_POS);
	public static Vector3f DOWN = new Vector3f(Z_NEG);
	public static Vector3f LEFT = new Vector3f(X_NEG);
	public static Vector3f RIGHT = new Vector3f(X_POS);
	public static Vector3f FORWARD = new Vector3f(Y_POS);
	public static Vector3f BACK = new Vector3f(Y_NEG);

	public GameEngine(GameLogic game, WindowOptions options) {
		this.gameLogic = game;
		this.window = new Window(options);
		this.audioMaster = new AudioMaster();
	}

	@Override
	public void run() {
		this.targetUps = this.window.getOptions().ups;
		this.targetFps = this.window.getOptions().fps;

		this.startTime = System.currentTimeMillis();
		long initialTime = this.startTime;
		float timeU = 1000.0f / this.targetUps;
		float timeR = this.targetFps > 0 ? 1000.0f / this.targetFps : 0;
		float deltaUpdate = 0;
		float deltaFps = 0;

		/* DEBUG */
		long lRender = 0, lUpdate = 0, tRender = 0, tUpdate = 0;

		long lastUpdate = this.startTime;
		long lastRender = this.startTime;
		long lastInput = this.startTime;
		while (!this.window.shouldClose() && this.running) {
			this.window.pollEvents();

			long now = System.currentTimeMillis();
			deltaUpdate += (now - initialTime) / timeU;
			deltaFps += (now - initialTime) / timeR;

			if (this.targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				this.gameLogic.input(now - lastInput);
				tUpdate = System.nanoTime() - start;
				this.window.clearScroll();

				lastInput = now;
			}

			if (deltaUpdate >= 1) {
				long start = System.nanoTime();
				this.gameLogic.update(now - lastUpdate);
				lUpdate = now - lastUpdate;
				tUpdate += System.nanoTime() - start;

				lastUpdate = now;
				deltaUpdate--;
			}

			if (this.targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				this.window.clear();

				this.gameLogic.render(now - lastRender);
				lRender = now - lastRender;

				this.window.swapBuffers();
				tRender += System.nanoTime() - start;

				lastRender = now;
				deltaFps--;
			}

			if (DEBUG.perfHistory) DEBUG.perfHistory(this.cache, this, lUpdate, lRender, tUpdate, tRender);

			initialTime = now;
		}
		this.running = !this.window.shouldClose();
		this.stop();
	}

	public void start() {
		this.cache = new SharedCacheManager();
		this.gameLogic.init(this);
		this.window.runCallbacks();
		this.running = true;
		this.run();
	}

	public void stop() {
		this.running = false;
		this.cleanup();
	}

	public GameLogic getGameLogic() { return this.gameLogic; }

	public Window getWindow() { return this.window; }

	public boolean isRunning() { return this.running; }

	public SharedCacheManager getCache() { return this.cache; }

	@Override
	public void cleanup() {
		this.cache.cleanup();
		this.window.cleanup();
	}

}
