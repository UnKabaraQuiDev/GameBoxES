package lu.pcy113.pdr.engine;

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
					BUFFER_SWAP_TIMEOUT = 500,
					WAIT_FRAME_END_TIMEOUT = 500,
					WAIT_UPDATE_END_TIMEOUT = 500; // ms
	
	public static DebugOptions DEBUG = new DebugOptions();
	
	private final String name;
	
	private WindowOptions windowOptions;
	private Window window;
	private AudioMaster audioMaster;
	private GameLogic gameLogic;
	
	private boolean running = false;
	private volatile boolean waitingForEvents = false;
	
	public int targetUps, targetFps;
	private double currentFps;
	
	private SharedCacheManager cache;
	
	private ThreadGroup threadGroup;
	private Thread updateThread, renderThread, mainThread;
	
	private final Object waitForFrameEnd = new Object();
	private final Object waitForUpdateEnd = new Object();
	
	public GameEngine(String name, GameLogic game, WindowOptions options) {
		this.name = name;
		game.register(this);
		this.gameLogic = game;
		this.windowOptions = options;
	}
	
	private boolean shouldRun() {
		//System.out.println(Thread.currentThread().getName()+"> should close: "+!this.window.shouldClose()+" && "+this.running+" = "+(!this.window.shouldClose() && this.running));
		return !this.window.shouldClose() && this.running && updateThread.isAlive() && renderThread.isAlive();
	}
	
	public boolean waitForFrameEnd() {
		if(Thread.currentThread().equals(renderThread))
			throw new IllegalAccessError(renderThread.getName()+" cannot wait for itself");
		
		synchronized (waitForFrameEnd) {
			try {
				waitForFrameEnd.wait(WAIT_FRAME_END_TIMEOUT);
				return true;
			} catch (InterruptedException e) {
				return true;
			}
		}
	}
	
	public boolean waitForUpdateEnd() {
		if(Thread.currentThread().equals(updateThread))
			throw new IllegalAccessError(updateThread.getName()+" cannot wait for itself");
		
		synchronized (waitForUpdateEnd) {
			try {
				waitForUpdateEnd.wait(WAIT_UPDATE_END_TIMEOUT);
				return true;
			} catch (InterruptedException e) {
				return true;
			}
		}
	}
	
	private void updateRun() {
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
		
		try {
		
			this.targetUps = this.window.getOptions().ups;
			long lastTime = System.nanoTime(); // nanos
			float timeUps = 1e9f / this.targetUps;
			
			while(this.shouldRun()) {
				long now = System.nanoTime();
				
				long deltaUpdate = now - lastTime;
				if(deltaUpdate > timeUps) {
					DEBUG.start("u_update_loop");
					DEBUG.start("u_pollEvents");
					this.pollEvents();
					DEBUG.end("u_pollEvents");
					DEBUG.start("u_input");
					this.gameLogic.input(deltaUpdate);
					DEBUG.end("u_input");
					this.window.clearScroll();
					
					DEBUG.start("u_update");
					this.gameLogic.update(deltaUpdate);
					DEBUG.end("u_update");
					
					lastTime = now;
					DEBUG.end("u_update_loop");
					
					synchronized (waitForUpdateEnd) {
						waitForUpdateEnd.notifyAll();
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
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

	private void renderRun() {
		this.window.takeGlContext();
		
		try {
		
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
					DEBUG.start("r_render_loop");
					DEBUG.start("r_clear");
					//this.window.clear();
					DEBUG.end("r_clear");
					DEBUG.start("r_render");
					this.gameLogic.render(deltaRender);
					DEBUG.end("r_render");
					DEBUG.start("r_swap");
					this.window.swapBuffers();
					DEBUG.end("r_swap");
					
					lastTime = now;
					
					this.currentFps = (double) 1 / ((double) deltaRender / 1_000_000_000);
					DEBUG.end("r_render_loop");
					
					synchronized (waitForFrameEnd) {
						waitForFrameEnd.notifyAll(); // wake up waiting threads
					}
					
					GlobalLogger.info("FPS: "+this.currentFps+" delta: "+((double) deltaRender/1_000_000)+"ms");
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.window.clearGLContext();
		this.stop();
	}

	public void start() {
		if(running)
			throw new IllegalStateException("Already running");
		
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
		DEBUG.cleanup();
	}
	
}
