package lu.pcy113.pdr.client.game;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.Texture;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.scene.Camera3D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	private GameEngine engine;
	
	protected float camSpeed = 0.1f;
	protected float camRotSpeed = 2.5f;
	
	protected Mesh mesh;
	protected Model model;
	protected Shader shader;
	protected Texture texture;
	protected Material material;
	protected Material material2;
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
		
		Shader shader2 = new Shader("truesceneshader",
				new ShaderPart("./resources/shaders/scene/scene.vert"),
				new ShaderPart("./resources/shaders/scene/scene.frag")) {
			@Override
			public void createUniforms() {
				getUniform("projectionMatrix");
				getUniform("viewMatrix");
				getUniform("modelMatrix");
				getUniform("t");
			}
		};
		engine.getCache().addShader(shader2);
		
		this.material2 = new Material("truescenematerial", shader2.getId());
		engine.getCache().addMaterial(material2);
		
		this.texture = new Texture("cube-texture", "./resources/textures/cube.png");
		engine.getCache().addTexture(texture);
		
		Map<String, String> txts = new HashMap<>();
		txts.put("txtSampler", texture.getId());
		this.material = new SceneMaterial(shader.getId(), txts);
		engine.getCache().addMaterial(material);
		
		this.mesh = ObjLoader.loadMesh("cube-mesh", material.getId(), "./resources/models/cube.obj");
		engine.getCache().addMesh(mesh);
		
		this.model = new Model("cube-model", mesh.getId(), new Transform3D());
		engine.getCache().addModel(model);
		
		Model model1 = new Model("cube-model1", mesh.getId(), new Transform3D());
		((Transform3D) model1.getTransform()).translateAdd(0.5f, 1, 0).updateMatrix();
		((Transform3D) model1.getTransform()).scaleMul(0.1f, 0.1f, 0.1f).updateMatrix();
		engine.getCache().addModel(model1);
		
		Mesh mesh2 = ObjLoader.loadMesh("monkey-mesh", material2.getId(), "./resources/models/monkey.obj");
		engine.getCache().addMesh(mesh2);
		Model model2 = new Model("cube-model2", mesh2.getId(), new Transform3D());
		((Transform3D) model2.getTransform()).translateAdd(0, 1, 1.5f).updateMatrix();
		engine.getCache().addModel(model2);
		
		this.scene = new Scene3D("main-scene");
		//this.scene.addMesh(mesh.getId());
		this.scene.addModel(model.getId());
		this.scene.addModel(model1.getId());
		this.scene.addModel(model2.getId());
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
				
				//System.out.println(x+" "+y+" : "+x2+" "+y2);
				
				Camera3D cam = (Camera3D) scene.getCamera();
				cam.getPosition().add(x*camSpeed, 0, y*camSpeed);
				cam.setYaw(cam.getYaw()+x2*camRotSpeed);
				cam.setPitch(cam.getPitch()+y2*camRotSpeed);
				cam.updateMatrix();
				
				material2.setProperty("t", x2);
				
				//System.err.println(cam.getPosition()+" > "+cam.getPitch()+" : "+cam.getYaw());
				//System.err.println(cam.getViewMatrix());
				
				//((Transform3D) model.getTransform()).translateAdd(x/10f, y/10f, 0.0f);
				//((Transform3D) model.getTransform()).rotate(x2, y2, 0).updateMatrix();
				
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
