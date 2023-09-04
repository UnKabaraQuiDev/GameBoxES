package lu.pcy113.pdr.engine;

import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.engine.logic.GameLogic;

public class GameEngine implements Runnable {
	
	private final Window window;
	private GameLogic gameLogic;
	
	private boolean running = false;
	private long startTime;
	
	private SharedCacheManager cache;
	
	public GameEngine(GameLogic game, WindowOptions options) {
		this.gameLogic = game;
		this.window = new Window(options);
	}
	
	@Override
	public void run() {
		while(!window.shouldClose() && running) {
			window.pollEvents();
			
			gameLogic.input(0);
			
			gameLogic.update(0);
			
			window.clear();
			
			gameLogic.render(0);
			
			window.swapBuffers();
		}
		running = !window.shouldClose();
	}
	
	public void start() {
		cache = new SharedCacheManager();
		gameLogic.init(this);
		running = true;
		startTime = System.currentTimeMillis();
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
	
}
