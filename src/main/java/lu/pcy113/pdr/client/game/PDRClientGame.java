package lu.pcy113.pdr.client.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.renderer.Renderer;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.graph.shader.Texture;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.geom.Mesh;
import lu.pcy113.pdr.engine.scene.geom.MeshInstance;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.engine.scene.geom.ModelInstance;
import lu.pcy113.pdr.engine.scene.geom.Transform;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	public PDRClientGame() {
		Logger.log();
	}
	
	float rotation = 0.0f;
	Model model;
	
	ModelInstance inst;
	
	@Override
	public void init(Window window, Scene scene, Renderer renderer) {
		Logger.log();
		
		float[] positions = new float[]{
				// V0
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

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] uv = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
        		// Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7
        };
        
        Mesh cube = new Mesh(positions, uv, indices);
        
        Texture texture = scene.getRenderer().getTextures().createTexture("cube.png");
        
		ShaderProgram shader = ShaderProgram.create("scene");
        
        Material material = new Material(texture, shader);

        model = new Model("cube-model", Arrays.asList(cube), material);
        ((Scene3D) scene).addModel(model);
        
        
        Mesh mesh = ObjLoader.loadMesh("monkey.obj");
        
        /*inst = new ModelInstance("cube-instance", Arrays.asList(new MeshInstance(100, positions, uv, indices)), new Material(null, ShaderProgram.create("instances")), 100, (i) -> new Transform(new Vector3f(i % 10, 0, i / 10), new Quaternionf(), new Vector3f(1)));
        ((Scene3D) scene).addInstances(inst);*/
	}

	@Override
	public void input(Window window, Scene scene, long dTime) {
		Logger.log();
		
		/*displInc.zero();
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
        
        float speed = 5;
        
        displInc.mul(dTime / 1000.0f);
        displInc.mul(speed);

        Vector3f entityPos = entity.getPosition();
        entity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
        entity.setScale(entity.getScale() + displInc.w);
        entity.updateModelMatrix();*/
	}
	
	@Override
	public void update(Window window, Scene scene, float dTime) {
		Logger.log();
		
		rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }
        model.getTransform().setRotateAlongAxisDeg(1, 1, 1, rotation);
        model.getTransform().updateMatrix();

        for(int i = 0; i < inst.getCount(); i++) {
        	inst.getTransforms().get(i).translate(0, (float) Math.sin(inst.getTransforms().get(i).getPosition().x + inst.getTransforms().get(i).getPosition().y), 0);
        }
	}

	@Override
	public void cleanup() {
		Logger.log();
	}

}
