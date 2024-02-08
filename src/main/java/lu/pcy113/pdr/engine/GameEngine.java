package lu.pcy113.pdr.engine;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.joml.Vector3f;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.audio.AudioMaster;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.utils.DebugOptions;

public class GameEngine implements Cleanupable, UniqueID {
	
	public static Vector3f X_POS = new Vector3f(1, 0, 0),
			X_NEG = new Vector3f(-1, 0, 0),
			Y_POS = new Vector3f(0, 1, 0),
			Y_NEG = new Vector3f(0, -1, 0),
			Z_POS = new Vector3f(0, 0, 1),
			Z_NEG = new Vector3f(0, 0, -1);
	
	public static Vector3f UP = new Vector3f(Z_POS),
			DOWN = new Vector3f(Z_NEG),
			LEFT = new Vector3f(X_NEG),
			RIGHT = new Vector3f(X_POS),
			FORWARD = new Vector3f(Y_POS),
			BACK = new Vector3f(Y_NEG);
	
	public static long POLL_EVENT_TIMEOUT = 500,
					BUFFER_SWAP_TIMEOUT = 500;
	
	public static DebugOptions DEBUG = new DebugOptions();
	
	private final String name;
	
	private WindowOptions windowOptions;
	private Window window;
	private AudioMaster audioMaster;
	private GameLogic gameLogic;
	
	private boolean running = false;
	private volatile boolean waitingForEvents = false, waitingForSwapBuffer = false;
	
	public int targetUps, targetFps;
	private double currentFps;
	
	private SharedCacheManager cache;
	
	private ThreadGroup threadGroup;
	private Thread updateThread, renderThread, mainThread;
	
	private final Object lock = new Object();
    private final Lock renderLock = new ReentrantLock();
    private final Lock updateLock = new ReentrantLock();
    private final Condition renderFinished = renderLock.newCondition();
    private final Condition updateFinished = updateLock.newCondition();
	
	public GameEngine(String name, GameLogic game, WindowOptions options) {
		this.name = name;
		this.gameLogic = game;
		this.windowOptions = options;
	}
	
	/*@Override
	public void run() {
		this.targetFps = this.window.getOptions().fps;
		
		this.startTime = System.currentTimeMillis();
		long initialTime = this.startTime;
		float timeU = 1000.0f / this.targetUps;
		float timeR = this.targetFps > 0 ? 1000.0f / this.targetFps : 0;
		float deltaUpdate = 0;
		float deltaFps = 0;
		
		// DEBUG
		// time between
		long delayInput = 0, delayRender = 0, delayUpdate = 0;
		// time to process
		long processInput = 0, processRender = 0, processUpdate = 0;
		
		long lastUpdate = 0;
		long lastRender = 0;
		long lastInput = 0;
		while (!this.shouldClose()) {
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
	*/
	private boolean shouldRun() {
		//System.out.println(Thread.currentThread().getName()+"> should close: "+!this.window.shouldClose()+" && "+this.running+" = "+(!this.window.shouldClose() && this.running));
		return !this.window.shouldClose() && this.running;
	}

	public void updateRun() {
		if(!running) {
			try {
				Thread.sleep(Long.MAX_VALUE); // waiting for renderThread to finish GameLogic#init()
				if(!Thread.interrupted()) {
					GlobalLogger.severe("Update thread waiting too long for init");
					return;
				}
			} catch (InterruptedException e) {
				GlobalLogger.severe("Update thread interrupted, continuing");
			}
		}
		
		this.targetUps = this.window.getOptions().ups;
		long lastTime = System.nanoTime(); // nanos
		float timeUps = 1e9f / this.targetUps;
		
		while(this.shouldRun()) {
			long now = System.nanoTime();
			
			long deltaUpdate = now - lastTime;
			if(deltaUpdate > timeUps) {
				this.pollEvents();
				this.gameLogic.input(deltaUpdate);
				this.window.clearScroll();
				
				this.gameLogic.update(deltaUpdate);
				
				lastTime = now;
			}
		}
		this.stop();
	}
	
	private boolean pollEvents() {
		try {
			waitingForEvents = true;
			Thread.sleep(POLL_EVENT_TIMEOUT);
			return Thread.interrupted();
		} catch (InterruptedException e) {
			return false;
		}
	}

	public void renderRun() {
		this.window.takeGlContext();
		
		this.gameLogic.init(this);
		running = true;
		updateThread.interrupt();
		mainThread.interrupt();
		
		this.targetFps = this.window.getOptions().fps;
		long lastTime = System.nanoTime(); // nanos
		float timeUps = this.targetFps > 0 ? 1e9f / this.targetFps : 0;
		
		while(this.shouldRun()) {
			long now = System.nanoTime();
			
			long deltaRender = now - lastTime;
			if(deltaRender > timeUps) {
				
				this.window.clear();
				this.gameLogic.render(deltaRender);
				this.window.swapBuffers();
				
				lastTime = now;
				
				this.currentFps = (double) 1 / ((double) deltaRender / 1000_000_000);
			}
		}
		
		this.window.clearGLContext();
		this.stop();
	}

	public void start() {
		this.cache = new SharedCacheManager();
		
		this.window = new Window(this.windowOptions);
		this.audioMaster = new AudioMaster();
		
		this.window.runCallbacks();
		this.window.clearGLContext();
		//this.running = true;
		
		this.threadGroup = new ThreadGroup(getClass().getName()+"#"+name);
		
		this.mainThread = Thread.currentThread();
		this.updateThread = new Thread(threadGroup, this::updateRun, threadGroup.getName()+":update");
		this.renderThread = new Thread(threadGroup, this::renderRun, threadGroup.getName()+":render");
		
		this.updateThread.start();
		this.renderThread.start();
		
		this.waitStop();
		
		//this.run();
	}
	
	private void waitStop() {
		if(!running) {
			try {
				Thread.sleep(Long.MAX_VALUE); // waiting for renderThread to finish GameLogic#init()
				if(!Thread.interrupted()) {
					GlobalLogger.severe("Main thread waiting too long for init");
					return;
				}
			} catch (InterruptedException e) {
				GlobalLogger.severe("Main thread interrupted, continuing");
			}
		}
		
		while(running) {
			if(waitingForEvents) {
				this.window.pollEvents();
				waitingForEvents = false;
				updateThread.interrupt();
			}
		}
		
		try {
			this.updateThread.join();
			this.renderThread.join();
		} catch (InterruptedException e) {
			GlobalLogger.severe("Main thread interrupted while joining subthreads");
		}
		
		this.window.takeGlContext();
		this.cleanup();
	}

	public void stop() {
		this.running = false;
	}
	
	@Override
	public String getId() {
		return name;
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
