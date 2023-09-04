package lu.pcy113.pdr.client.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	private GameEngine engine;
	
	protected Mesh mesh;
	protected Model model;
	protected Shader shader;
	protected Material material;
	protected Scene3D scene;
	protected Scene3DRenderer scene3DRenderer;
	
	public PDRClientGame() {
		Logger.log();
	}
	
	@Override
	public void init(GameEngine e) {
		Logger.log();
		
		this.engine = e;
		
		this.shader = new SceneShader();
		engine.getCache().addShader(shader);
		
		this.material = new SceneMaterial(shader.getId());
		engine.getCache().addMaterial(material);
		
		this.mesh = ObjLoader.loadMesh("cube-mesh", material.getId(), "./resources/models/cube.obj");
		engine.getCache().addMesh(mesh);
		
		this.model = new Model("cube-model", mesh.getId(), new Transform3D());
		engine.getCache().addModel(model);
		
		this.scene = new Scene3D("main-scene");
		//this.scene.addMesh(mesh.getId());
		this.scene.addModel(model.getId());
		engine.getCache().addScene(scene);
		
		this.scene3DRenderer = new Scene3DRenderer();
		engine.getCache().addRenderer(scene3DRenderer);
		engine.getCache().addRenderer(new MeshRenderer());
		engine.getCache().addRenderer(new ModelRenderer());
		
		engine.getWindow().onResize((w, h) -> scene.getCamera().getProjection().perspectiveUpdateMatrix(w, h));
	}

	@Override
	public void input(float dTime) {
		if(engine.getWindow().isJoystickPresent()) {
			if(engine.getWindow().updateGamepad(0)) {
				GLFWGamepadState gps = engine.getWindow().getGamepad();
				
				float x = gps.axes(0);
				float y = gps.axes(1);
				
				float x2 = gps.axes(2);
				float y2 = gps.axes(3);
				
				System.out.println(x+" "+y+" : "+x2+" "+y2);
				
				((Transform3D) model.getTransform()).translateAdd(x/10f, y/10f, 0.0f);
				((Transform3D) model.getTransform()).rotate(x2, y2, 0).updateMatrix();
				
				/*FloatBuffer fb = gps.axes();
				int i = 0;
				while(fb.hasRemaining()) {
					System.out.println("axes "+i+"> "+fb.get());
					i++;
				}
				
				ByteBuffer bb = gps.buttons();
				i = 0;
				while(bb.hasRemaining()) {
					System.out.println("buttons "+i+"> "+bb.get());
					i++;
				}*/
			}
		}
		//Logger.log();
	}
	
	@Override
	public void update(float dTime) {
		//Logger.log();
	}

	@Override
	public void render(float dTime) {
		//Logger.log();
		scene3DRenderer.render(engine.getCache(), engine, scene);
	}

}
