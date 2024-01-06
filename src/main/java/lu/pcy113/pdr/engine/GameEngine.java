package lu.pcy113.pdr.engine;

import java.util.logging.Level;

import org.joml.Vector3f;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.audio.AudioMaster;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.utils.DebugOptions;

public class GameEngine implements Runnable, Cleanupable {
	
	private final Window window;
	private final AudioMaster audioMaster;
	private GameLogic gameLogic;
	
	private boolean running = false;
	private long startTime;
	
	public int targetUps, targetFps;
	private double currentFps;
	
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
		// time between
		long delayInput = 0, delayRender = 0, delayUpdate = 0;
		// time to process
		long processInput = 0, processRender = 0, processUpdate = 0;
		
		long lastUpdate = 0;
		long lastRender = 0;
		long lastInput = 0;
		while (!this.window.shouldClose() && this.running) {
			this.window.pollEvents();
			
			long now = System.currentTimeMillis();
			deltaUpdate += (now - initialTime) / timeU;
			deltaFps += (now - initialTime) / timeR;
			
			if (this.targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				delayInput = now - lastInput;
				
				this.gameLogic.input(now - lastInput);
				this.window.clearScroll();
				
				processInput = System.nanoTime() - start;
				
				lastInput = now;
			}
			
			if (deltaUpdate >= 1) {
				long start = System.nanoTime();
				delayUpdate = now - lastUpdate;
				
				this.gameLogic.update(now - lastUpdate);
				
				processUpdate = System.nanoTime() - start;
				
				lastUpdate = now;
				deltaUpdate--;
			}
			
			if (this.targetFps <= 0 || deltaFps >= 1) {
				long start = System.nanoTime();
				delayRender = now - lastRender;
				
				this.window.clear();
				this.gameLogic.render(now - lastRender);
				this.window.swapBuffers();
				
				processRender = System.nanoTime() - start;
				
				lastRender = now;
				deltaFps--;
			}
			
			initialTime = now;
			
			this.currentFps = (double) 1 / ((double) delayRender / 1_000);
			GlobalLogger.log(Level.INFO, String.format("input duration: %fms; update duration: %fms; render duration: %fms\ninput delay: %dns; update delay: %dns; render delay: %dns\nFrame rate: %ffps",
					(double) processInput / 1_000_000, (double) processUpdate / 1_000_000, (double) processRender / 1_000_000,
					delayInput, delayUpdate, delayRender,
					currentFps));
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
	
	public GameLogic getGameLogic() {
		return this.gameLogic;
	}
	
	public Window getWindow() {
		return this.window;
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public SharedCacheManager getCache() {
		return this.cache;
	}
	
	public double getCurrentFps() {
		return currentFps;
	}
	
	public AudioMaster getAudioMaster() {
		return audioMaster;
	}
	
	@Override
	public void cleanup() {
		this.cache.cleanup();
		this.window.cleanup();
	}
	
}
