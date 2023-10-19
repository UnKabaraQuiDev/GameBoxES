package lu.pcy113.pdr.engine;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.utils.DebugOptions;

public class GameEngine implements Runnable, Cleanupable {
	
	private final Window window;
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
	}
	
	@Override
	public void run() {
		targetUps = window.getOptions().ups;
		targetFps = window.getOptions().fps;
		
		startTime = System.currentTimeMillis();
		long initialTime = startTime;
		float timeU = 1000.0f / targetUps;
		float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
		float deltaUpdate = 0;
		float deltaFps = 0;
		
		/* DEBUG */
		long lRender = 0, lUpdate = 0, tRender = 0, tUpdate = 0;
		
		long lastUpdate = startTime;
		long lastRender = startTime;
		long lastInput = startTime;
		while(!window.shouldClose() && running) {
			window.pollEvents();
			
			long now = System.currentTimeMillis();
			deltaUpdate += (now - initialTime) / timeU;
			deltaFps += (now - initialTime) / timeR;
			
			if(targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				gameLogic.input(now - lastInput);
				tUpdate = System.nanoTime() - start;
				window.clearScroll();
				
				lastInput = now;
			}
			
			if(deltaUpdate >= 1) {
				long start = System.nanoTime();
				gameLogic.update(now - lastUpdate);
				lUpdate = now - lastUpdate;
				tUpdate += System.nanoTime() - start;
				
				lastUpdate = now;
				deltaUpdate--;
			}
			
			if(targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				window.clear();
				
				gameLogic.render(now - lastRender);
				lRender = now - lastRender;
				
				window.swapBuffers();
				tRender += System.nanoTime() - start;
				
				lastRender = now;
				deltaFps--;
			}
			
			if(DEBUG.perfHistory)
				DEBUG.perfHistory(cache, this, lUpdate, lRender, tUpdate, tRender);
			
			initialTime = now;
		}
		running = !window.shouldClose();
		
		this.cleanup();
	}
	
	public void start() {
		cache = new SharedCacheManager();
		gameLogic.init(this);
		window.runCallbacks();
		running = true;
		run();
	}
	public void stop() {
		this.running = false;
		cache.cleanup();
	}
	
	public GameLogic getGameLogic() {return gameLogic;}
	public Window getWindow() {return window;}
	public boolean isRunning() {return running;}
	public SharedCacheManager getCache() {return cache;}
	
	@Override
	public void cleanup() {
		cache.cleanup();
		window.cleanup();
	}
	
}
