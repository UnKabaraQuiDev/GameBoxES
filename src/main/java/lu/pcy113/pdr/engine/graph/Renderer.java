package lu.pcy113.pdr.engine.graph;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.utils.Logger;

public class Renderer implements Cleanupable {
	
	private SceneRenderer sceneRenderer;
	
	public Renderer() {
		Logger.log();
		
		GL.createCapabilities();
		GLUtil.setupDebugMessageCallback(System.err);
		sceneRenderer = new SceneRenderer();
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		sceneRenderer.cleanup();
	}
	
	public void render(Window window, Scene scene) {
		Logger.log();
		
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BITS);
		GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
		//GL11.glClearColor(0, 1, 0, 1);
		
		sceneRenderer.render(scene);
	}
	
}
