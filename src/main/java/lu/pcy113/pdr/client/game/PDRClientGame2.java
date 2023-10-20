package lu.pcy113.pdr.client.game;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.client.game.options.KeyOptions;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.render.GizmoModelRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame2 implements GameLogic {
	
	GameEngine engine;
	CacheManager cache;
	
	Scene3D scene;
	Model model;
	
	Compositor compositor;
	
	public PDRClientGame2() {
		Logger.log();
	}
	
	@Override
	public void init(GameEngine e) {
		Logger.log();
		
		this.engine = e;
		this.cache = e.getCache();
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.wireframeColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = false;
		
		Shader shader = new Shader("main",
				new ShaderPart("./resources/shaders/main/uv.frag"),
				new ShaderPart("./resources/shaders/main/main.vert")) {
			@Override
			public void createUniforms() {
				createUniform(Shader.PROJECTION_MATRIX);
				createUniform(Shader.VIEW_MATRIX);
				createUniform(Shader.TRANSFORMATION_MATRIX);
			}
		};
		cache.addShader(shader);
		Material mat = new Material("main", shader) {{
			
		}};
		cache.addMaterial(mat);
		Mesh mesh = ObjLoader.loadMesh("chest", mat.getId(), "./resources/models/cube.obj");
		cache.addMesh(mesh);
		model = new Model("model", mesh.getId(), new Transform3D());
		cache.addModel(model);
		
		this.scene = new Scene3D("main-scene");
		this.scene.addEntity("model", new Entity()
				.addComponent(new ModelComponent(model)));
		
		Gizmo gizmoXYZ = ObjLoader.loadGizmo("gizmoXYZ", "./resources/models/gizmos/grid_xyz.obj");
		GizmoModel gizmoXYZModel = new GizmoModel("gizmoXYZ", gizmoXYZ.getId(), new Transform3D());
		this.scene.addEntity("gizmoXYZ", new Entity()
				.addComponent(new GizmoModelComponent(gizmoXYZModel)));
		
		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", scene);
		cache.addRenderLayer(sceneRender);
		
		cache.addRenderer(new Scene3DRenderer());
		cache.addRenderer(new ModelRenderer());
		cache.addRenderer(new GizmoModelRenderer());
		
		compositor = new Compositor();
		compositor.addRenderLayer(0, sceneRender);
		
		engine.getWindow().onResize((w, h) -> scene.getCamera().getProjection().update(w, h));
		engine.getWindow().setBackground(new Vector4f(1, 1, 1, 1));
		
		Camera3D cam = (Camera3D) scene.getCamera();
		cam.getProjection().setPerspective(true);
		cam.getProjection().setFov((float) Math.toRadians(60));
		cam.setPosition(new Vector3f(5, 0, 0)).updateMatrix();
		
	}
	
	float GX = 0;
	
	protected float camSpeed = 0.1f;
	protected float camRotSpeed = (float) Math.toRadians(2.5);
	
	private float applyThreshold(float axes, float f) {
		return Math.abs(axes) < f ? 0 : axes;
	}
	
	@Override
	public void input(float dTime) {
		if(engine.getWindow().isJoystickPresent()) {
			if(engine.getWindow().updateGamepad(0)) {
				GLFWGamepadState gps = engine.getWindow().getGamepad();
				
				float ax = applyThreshold(gps.axes(0), 0.01f);
				float ay = applyThreshold(gps.axes(1), 0.01f)*(-1);
				
				float bx = applyThreshold(gps.axes(2), 0.01f);
				float by = applyThreshold(gps.axes(3), 0.01f)*(-1);
				
				Camera3D cam = (Camera3D) scene.getCamera();
				
				cam.roll(by*camRotSpeed);
				
				float cy = (engine.getWindow().isKeyPressed(KeyOptions.FORWARD) ? 1 : 0) - (engine.getWindow().isKeyPressed(KeyOptions.BACKWARD) ? 1 : 0);
				float cx = (engine.getWindow().isKeyPressed(KeyOptions.RIGHT) ? 1 : 0) - (engine.getWindow().isKeyPressed(KeyOptions.LEFT) ? 1 : 0);
				
				System.err.println(ax+" "+ay+" : "+bx+" "+by+" : "+cx+" "+cy);
				
				cam.move(cx, cy, camSpeed);
				
				cam.move(ax, ay, bx, by, camSpeed, camRotSpeed);
				
				System.err.println(cam.getViewMatrix());
				
				cam.getProjection().setPerspective(true);
				cam.getProjection().setFov((float) Math.toRadians(80));
				cam.getProjection().update();
				cam.updateMatrix();
				
				GX += 0.01f;
				
				((Transform3D) model.getTransform()).rotate(0.01f, 0.01f, 0.01f).updateMatrix();
				
				engine.getWindow().setBackground(new Vector4f(GX % 1, GX % 1, GX % 1, 1));
				
				float scrollSpeed = 0.5f;
				
				//cam.getProjection().setSize((float) (org.joml.Math.clamp(0, 150, cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				//cam.getProjection().update();
			}
		}
	}
	
	@Override
	public void update(float dTime) {
	}

	@Override
	public void render(float dTime) {
		compositor.render(engine.getCache(), engine);
	}

}
