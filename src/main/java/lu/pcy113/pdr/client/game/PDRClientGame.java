package lu.pcy113.pdr.client.game;

import java.util.Arrays;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.Mesh;
import lu.pcy113.pdr.engine.graph.Model;
import lu.pcy113.pdr.engine.graph.Renderer;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.scene.Entity;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	public PDRClientGame() {
		Logger.log();
	}
	
	Model model;
	Entity entity;
	
	Vector4f displInc = new Vector4f();
    float rotation;
	
	@Override
	public void init(Window window, Scene scene, Renderer renderer) {
		Logger.log();
		
		/*float[] pos = new float[] {
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
		
		Mesh plane = new Mesh(pos, col, ids);*/
		//Mesh mesh = ObjLoader.loadMesh("cube.obj").get(0);
		float[] positions = new float[]{
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };
        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };
        Mesh cube = new Mesh(positions, colors, indices);
        
		model = new Model("cube", Arrays.asList(cube));
		scene.addModel(model);
		entity = new Entity("cubeEntity", model.getId());
		entity.setPosition(0, 0, -2);
		scene.addEntity(entity);
	}

	@Override
	public void input(Window window, Scene scene, long dTime) {
		Logger.log();
		
		displInc.zero();
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            displInc.y = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            displInc.y = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            displInc.x = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            displInc.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            displInc.z = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_Q)) {
            displInc.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z)) {
            displInc.w = -1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_X)) {
            displInc.w = 1;
        }
        System.err.println("ispressed ? UP"+window.isKeyPressed(GLFW.GLFW_KEY_UP));
        
        float speed = 5;
        
        displInc.mul(dTime / 1000.0f);
        displInc.mul(speed);

        Vector3f entityPos = entity.getPosition();
        entity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
        entity.setScale(entity.getScale() + displInc.w);
        entity.updateModelMatrix();
	}
	
	@Override
	public void update(Window window, Scene scene, float dTime) {
		Logger.log();
		
		rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }
        entity.rotateAlongAxisDeg(1, 1, 1, rotation);
        entity.updateModelMatrix();
	}

	@Override
	public void cleanup() {
		Logger.log();
	}

}
