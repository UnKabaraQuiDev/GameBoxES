package lu.pcy113.pdr.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import lombok.Getter;
import lombok.Setter;
import lu.pcy113.pdr.engine.graph.EntityRenderer;
import lu.pcy113.pdr.engine.graph.RenderManager;
import lu.pcy113.pdr.engine.graph.Scene3DRenderer;
import lu.pcy113.pdr.engine.logic.GameLogic;

public class GameEngine implements Runnable {
	
	@Getter @Setter
	private Window window;
	@Getter @Setter
	private GameLogic gameLogic;
	@Getter @Setter
	private RenderManager renderManager;
	
	public GameEngine(WindowOptions options, GameLogic gl) {
		this.gameLogic = gl;
		
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		GL.createCapabilities();
		
		this.window = new Window(options);
		
		this.renderManager = new RenderManager();
		this.renderManager.addRenderer(new Scene3DRenderer(renderManager));
		this.renderManager.addRenderer(new EntityRenderer(renderManager));
	}
	
	@Override
	public void run() {
		render(0);
	}
	
	private void render(float dTime) {
		window.prepare();
		gameLogic.render(0);
		window.update();
	}
	
	public Window getWindow() {return window;}
	public RenderManager getRenderManager() {return renderManager;}
	public GameLogic getGameLogic() {return gameLogic;}

}
