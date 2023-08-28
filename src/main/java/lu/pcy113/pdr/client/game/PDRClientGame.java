package lu.pcy113.pdr.client.game;

import org.joml.Math;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.Mesh;
import lu.pcy113.pdr.engine.graph.Renderer;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	private int direction = 0;
	private float color = 0f;
	
	public PDRClientGame() {
		Logger.log();
	}
	
	@Override
	public void init(Window window, Scene scene, Renderer renderer) {
		Logger.log();
		
		float[] pos = new float[] {
				-0.5f, 0.5f, -1.0f,
				-0.5f, -0.5f, -1.0f,
				0.5f, -0.5f, -1.0f,
				0.5f, 0.5f, -1.0f,
		};
		float[] col = new float[] {
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		int[] ids = new int[] {
			0, 1, 3,
			3, 1, 2
		};
		
		Mesh mesh = new Mesh(pos, col, ids);
		scene.addMesh("plane", mesh);
		
		//scene.addMesh("monkey", ObjLoader.loadMesh("monkey.obj").get(0));
	}

	@Override
	public void input(Window window, Scene scene, long dTime) {
		Logger.log();
		
		System.out.println("dir "+direction);
		if(window.isKeyPressed(GLFW.GLFW_KEY_Z)) {
			direction = 1;
		}else if(window.isKeyPressed(GLFW.GLFW_KEY_S)) {
			direction = -1;
		}else {
			direction = 0;
		}
	}

	@Override
	public void update(Window window, Scene scene, float dTime) {
		Logger.log();
		
		color += direction*5;
		
		GL11.glRotatef(color, 0, 0, 1);
	}

	@Override
	public void cleanup() {
		Logger.log();
	}

}
