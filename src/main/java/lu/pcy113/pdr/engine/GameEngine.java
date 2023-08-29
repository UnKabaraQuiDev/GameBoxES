package lu.pcy113.pdr.engine;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import lu.pcy113.pdr.engine.graph.Compositor;
import lu.pcy113.pdr.engine.graph.renderer.Renderer;
import lu.pcy113.pdr.engine.graph.renderer.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.utils.Logger;

public class GameEngine implements Runnable, Cleanupable {

	private final Window window;
	private GameLogic gameLogic;
	
	private Compositor compositor;
	private Renderer contentRenderer;
	private Renderer uiRenderer;
	
	private boolean running;
	private Scene contentScene, uiScene;
	private int targetFps, targetUps;
	
	public GameEngine(String title, WindowOptions options, GameLogic gl) {
		Logger.log();
		
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

		this.window = new Window(title, options, () -> resize());
		this.gameLogic = gl;
		
		this.targetFps = options.fps;
		this.targetUps = options.ups;
		
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		this.compositor = new Compositor(this);
		this.contentRenderer = new Scene3DRenderer();
		this.uiRenderer = new Scene3DRenderer();
		
		this.contentScene = new Scene3D("content", window.getWidth(), window.getHeight(), this.contentRenderer);
		this.uiScene = new Scene3D("ui", window.getWidth(), window.getHeight(), this.uiRenderer);
		
		Model quad = new Model("ui-plane", Arrays.asList(ObjLoader.loadMesh("monkey.obj")), new Material(null, ShaderProgram.create("scene")));
		quad.getTransform().setRotateAlongAxisDeg(1, 0.5f, 0, 90);
		//quad.getTransform().translate(0.1f, 0.1f, .1f);
		quad.getTransform().setScale(0.1f, 0.1f, 0.1f);
		quad.getTransform().updateMatrix();
		//quad.getMaterial().getShader().getUniformsMap().createUniform("render");
		((Scene3D) this.uiScene).addModel(quad);
		
		this.gameLogic.init(window, contentScene, contentRenderer);
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
				if(uiScene.isVisible())
					gameLogic.input(window, uiScene, now-start);
				else if(contentScene.isVisible())
					gameLogic.input(window, contentScene, now-start);
			}
			
			if(dUpdate >= 1) {
				long diffTime = start - updateTime;
				//Logger.log("diffTime: "+diffTime);
				if(uiScene.isVisible())
					gameLogic.update(window, uiScene, now-start);
				else if(contentScene.isVisible())
					gameLogic.update(window, contentScene, now-start);
				updateTime = now;
				dUpdate--;
			}
			
			if(targetFps <= 0 || dFps  >= 1) {
				compositor.render(window);
				dFps--;
				window.update();
			}
			
			start = now;
		}
		
		cleanup();
	}
	
	private Void resize() {
		Logger.log("resized: "+window.getWidth()+"x"+window.getHeight());
		contentScene.resize(window.getWidth(), window.getHeight());
		uiScene.resize(window.getWidth(), window.getHeight());
		return null;
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		gameLogic.cleanup();
		contentRenderer.cleanup();
		uiRenderer.cleanup();
		contentScene.cleanup();
		uiScene.cleanup();
		window.cleanup();
	}
	
	public Renderer getContentRenderer() {return contentRenderer;}
	public Scene getContentScene() {return contentScene;}
	public Renderer getUiRenderer() {return uiRenderer;}
	public Scene getUiScene() {return uiScene;}
	public Compositor getCompositor() {return compositor;}

}
