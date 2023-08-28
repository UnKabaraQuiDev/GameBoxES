package lu.pcy113.pdr.engine;

import lu.pcy113.pdr.engine.graph.Renderer;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.utils.Logger;

public class GameEngine implements Runnable, Cleanupable {

	private final Window window;
	private GameLogic gameLogic;
	private final Renderer renderer;
	
	private boolean running;
	private Scene scene;
	private int targetFps, targetUps;
	
	public GameEngine(String title, WindowOptions options, GameLogic gl) {
		Logger.log();
		
		this.window = new Window(title, options, () -> resize());
		this.gameLogic = gl;
		
		this.targetFps = options.fps;
		this.targetUps = options.ups;
		
		this.renderer = new Renderer();
		this.scene = new Scene(window.getWidth(), window.getHeight());
		
		this.gameLogic.init(window, scene, renderer);
		this.running = true;
	}
	
	public void start() {
		Logger.log();
		
		running = true;
		run();
	}
	public void stop() {
		Logger.log();
		
		running = false;
	}
	
	@Override
	public void run() {
		Logger.log();
		
		long start = System.currentTimeMillis();
		float timeU = 1000f/targetUps;
		float timeR = targetFps > 0 ? 1000f/targetFps : 0;
		float dUpdate = 0;
		float dFps = 0;
		
		//int i = 0;
		long updateTime = start;
		while(running && !window.windowShouldClose()) {
			//Logger.log("-------------------------- + "+i);
			//i++;
			
			window.pollEvents();
			
			long now = System.currentTimeMillis();
			dUpdate += (now - start) / timeU;
			dFps += (now - start) / timeR;
			
			/*Logger.log("start: "+start);
			Logger.log("timeU: "+timeU);
			Logger.log("timeR: "+timeR);
			Logger.log("dUpdate: "+dUpdate);
			Logger.log("dFps: "+dFps);
			Logger.log("updateTime: "+updateTime);
			Logger.log("now: "+now);*/
			
			if(targetFps <= 0 || dFps >= 1) {
				gameLogic.input(window, scene, now-start);
			}
			
			if(dUpdate >= 1) {
				long diffTime = start - updateTime;
				//Logger.log("diffTime: "+diffTime);
				gameLogic.update(window, scene, diffTime);
				updateTime = now;
				dUpdate--;
			}
			
			if(targetFps <= 0 || dFps  >= 1) {
				renderer.render(window, scene);
				dFps--;
				window.update();
			}
			
			start = now;
		}
		
		cleanup();
	}
	
	private Void resize() {
		Logger.log("resized: "+window.getWidth()+"x"+window.getHeight());
		return null;
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		gameLogic.cleanup();
		renderer.cleanup();
		scene.cleanup();
		window.cleanup();
	}

}
