package lu.pcy113.pdr.client.game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.client.game.options.KeyOptions;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.text.TextMesh;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.blur.gaussian.GaussianBlurMaterial;
import lu.pcy113.pdr.engine.graph.composition.blur.gaussian.GaussianBlurShader;
import lu.pcy113.pdr.engine.graph.composition.debug.PerfHistoryLayer;
import lu.pcy113.pdr.engine.graph.composition.debug.PerfHistoryLayerMaterial;
import lu.pcy113.pdr.engine.graph.composition.debug.PerfHistoryLayerShader;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.components.PointLightMaterialComponent;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.render.GizmoModelRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.render.TextModelRenderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.PointLight;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.TextModelComponent;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	private GameEngine engine;
	
	protected Mesh mesh;
	protected Model model;
	protected Shader diffShader, txtDiffShader;
	protected Texture diffTexture, specTexture;
	protected Material material, txtMaterial;
	protected Scene3D scene;
	protected PointLight light;
	protected Scene3DRenderer scene3DRenderer;
	protected FillTextShader fillTShader;
	protected FillTextMaterial fillTMaterial;
	protected TextModel tModel;
	
	protected Compositor compositor;
	
	public PDRClientGame() {
		Logger.log();
	}
	
	@Override
	public void init(GameEngine e) {
		Logger.log();
		
		this.engine = e;
		GameEngine.DEBUG.wireframe = true;
		GameEngine.DEBUG.wireframeColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = false;
		
		
		this.fillTShader = new FillTextShader();
		engine.getCache().addShader(fillTShader);
		
		Texture raster = new Texture("raster", "./resources/textures/fonts/font1.png");
		engine.getCache().addTexture(raster);
		
		Texture rasterIndex = new Texture("rasterIndex", "./resources/textures/fonts/font1indices.png");
		engine.getCache().addTexture(rasterIndex);
		
		this.fillTMaterial = new FillTextMaterial("fillTMaterial", raster.getId(), rasterIndex.getId(), new Vector4f(1, 0.5f, 0.5f, 1));
		engine.getCache().addMaterial(fillTMaterial);
		
		TextMesh tMesh = new TextMesh(100);
		engine.getCache().addMesh(tMesh);
		
		tModel = new TextModel("tModel", fillTMaterial.getId(), new Transform3D().scaleMul(0.3f, 0.3f, 0.3f), "Test Text", new Vector2f(0.3f, 0.3f));
		engine.getCache().addTextModel(tModel);
		
		this.txtDiffShader = new TxtDiffuse1Shader();
		engine.getCache().addShader(txtDiffShader);
		
		this.diffTexture = new Texture("cube-diffTexture", "./resources/textures/cube.png");
		engine.getCache().addTexture(diffTexture);
		
		this.specTexture = new Texture("cube-specTexture", "./resources/textures/default.png");
		engine.getCache().addTexture(specTexture);
		
		this.txtMaterial = new TxtDiffuseMaterial("txtDiffuse", diffTexture.getId(), new Vector3f(1), specTexture.getId(), 0.5f)
				.addComponent(new PointLightMaterialComponent("lights", "lightCount", 10));
		engine.getCache().addMaterial(txtMaterial);
		
		
		this.diffShader = new DiffuseShader();
		engine.getCache().addShader(diffShader);
		
		this.material = new DiffuseMaterial("diffuse", new Vector3f(1, 0.5f, 0.5f), new Vector3f(0.5f, 0.2f, 0), new Vector3f(1), 0.5f)
				.addComponent(new PointLightMaterialComponent("lights", "lightCount", 10));
		engine.getCache().addMaterial(material);
		
		Mesh chestMesh = ObjLoader.loadMesh("chest-mesh", GizmoMaterial.NAME, "./resources/models/chest.obj");
		Model chestModel = new Model("chest-model", chestMesh.getId(), new Transform3D());
		((Transform3D) chestModel.getTransform()).translateAdd(5, 0, 5).updateMatrix();
		engine.getCache().addMesh(chestMesh);
		engine.getCache().addModel(chestModel);
		
		this.mesh = ObjLoader.loadMesh("scene-mesh", txtMaterial.getId(), "./resources/models/scene_pdrassets_1.obj");
		engine.getCache().addMesh(mesh);
		
		this.model = new Model("cube-model", mesh.getId(), new Transform3D());
		engine.getCache().addModel(model);
		
		Model model1 = new Model("cube-model1", mesh.getId(), new Transform3D());
		((Transform3D) model1.getTransform()).translateAdd(0.5f, 1, 0).updateMatrix();
		((Transform3D) model1.getTransform()).scaleMul(0.1f, 0.1f, 0.1f).updateMatrix();
		engine.getCache().addModel(model1);
		
		this.light = new PointLight(
				"point-1",
				new Vector3f(0, 2, 0),
				1, 1, 1,
				new Vector3f(0, 0, 1), new Vector3f(0, 0.5f, 0.5f), new Vector3f(0, 0.2f, 0.5f));
		engine.getCache().addPointLight(light);
		
		
		Mesh mesh2 = ObjLoader.loadMesh("monkey-mesh", material.getId(), "./resources/models/monkey.obj");
		engine.getCache().addMesh(mesh2);
		
		Model model2 = new Model("cube-model2", mesh2.getId(), new Transform3D());
		((Transform3D) model2.getTransform()).translateAdd(0, 1, 1.5f).updateMatrix();
		engine.getCache().addModel(model2);
		
		this.scene = new Scene3D("main-scene");
		/*this.scene.addEntity("model", new Entity()
				.addComponent(new ModelComponent(model))
				.addComponent(new PointLightSurfaceComponent()));
		this.scene.addEntity("model1", new Entity()
				.addComponent(new ModelComponent(model1))
				.addComponent(new PointLightSurfaceComponent()));
		this.scene.addEntity("model2", new Entity()
				.addComponent(new ModelComponent(model2))
				.addComponent(new PointLightSurfaceComponent()));
		this.scene.addEntity("light", new Entity()
				.addComponent(new PointLightComponent(light)));
		this.scene.addEntity("chestModel", new Entity()
				.addComponent(new ModelComponent(chestModel))
				.addComponent(new PointLightSurfaceComponent()));*/
		
		this.scene.addEntity("tMod", new Entity()
				.addComponent(new TextModelComponent(tModel)));
		
		engine.getCache().addScene(scene);
		
		Gizmo gizmo = ObjLoader.loadGizmo("gizmo", "./resources/models/cube_wireframe.obj");
		GizmoModel gizmoModel = new GizmoModel("gizmoModel", gizmo.getId(), new Transform3D());
		((Transform3D) gizmoModel.getTransform()).translateAdd(0.5f, 0.5f, 0.5f).updateMatrix();
		
		engine.getCache().addGizmo(gizmo);
		engine.getCache().addGizmoModel(gizmoModel);
		//this.scene.addEntity("gizmoModel", new Entity()
		//		.addComponent(new GizmoModelComponent(gizmoModel)));
		
		Gizmo gizmoAxisGrid = ObjLoader.loadGizmo("gizmoGrid", "./resources/models/gizmos/grid_xyz.obj");
		GizmoModel gizmoModelAxisGrid = new GizmoModel("gizmoModelGridXYZ", gizmoAxisGrid.getId(), new Transform3D());
		//((Transform3D) gizmoModelAxisGrid.getTransform()).rotateFromAxisAngleRad(0, 1, 0, 90).updateMatrix();
		
		engine.getCache().addGizmo(gizmoAxisGrid);
		engine.getCache().addGizmoModel(gizmoModelAxisGrid);
		//this.scene.addEntity("gizmoModelAxisGrid", new Entity()
		//		.addComponent(new GizmoModelComponent(gizmoModelAxisGrid)));
		
		this.scene3DRenderer = new Scene3DRenderer();
		engine.getCache().addRenderer(scene3DRenderer);
		engine.getCache().addRenderer(new MeshRenderer());
		engine.getCache().addRenderer(new ModelRenderer());
		engine.getCache().addRenderer(new GizmoModelRenderer());
		engine.getCache().addRenderer(new TextModelRenderer());
		
		Shader passRenderShader = new BackgroundShader(0);
		Material passRenderMaterial = new BackgroundMaterial(0);
		engine.getCache().addShader(passRenderShader);
		engine.getCache().addMaterial(passRenderMaterial);
		
		Shader blurRenderShader = new GaussianBlurShader();
		Material blurRenderMaterial = new GaussianBlurMaterial(5, 5);
		engine.getCache().addShader(blurRenderShader);
		engine.getCache().addMaterial(blurRenderMaterial);
		
		GenerateRenderLayer passRender = new GenerateRenderLayer("background", passRenderMaterial.getId());
		engine.getCache().addRenderLayer(passRender);
		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", scene);
		engine.getCache().addRenderLayer(sceneRender);
		PassRenderLayer blurRender = new PassRenderLayer("blur", blurRenderMaterial.getId());
		engine.getCache().addRenderLayer(blurRender);
		PerfHistoryLayer perfRender = new PerfHistoryLayer();
		engine.getCache().addShader(new PerfHistoryLayerShader());
		engine.getCache().addMaterial(new PerfHistoryLayerMaterial());
		engine.getCache().addRenderLayer(perfRender);
		
		/*ColorFilterMaterial colorFilterMaterial = new ColorFilterMaterial();
		engine.getCache().addMaterial(colorFilterMaterial);
		//ColorFilterShader colorFilterShader = new ColorFilterShader();
		//engine.getCache().addShader(colorFilterShader);
		PassRenderLayer colorFilterRender = new PassRenderLayer("colorFilter", colorFilterMaterial.getId());
		engine.getCache().addRenderLayer(colorFilterRender);
		
		colorFilterMaterial.setMul(new Vector4f(1, 0, 1, 1));
		//colorFilterMaterial.setAdd(new Vector4f(0, 1, 0, 0));*/
		
		compositor = new Compositor();
		compositor.addRenderLayer(0, passRender);
		compositor.addRenderLayer(1, sceneRender);
		//compositor.addRenderLayer(0, blurRender);
		//compositor.addRenderLayer(2, perfRender.getId());
		//compositor.addRenderLayer(0, colorFilterRender);
		
		engine.getWindow().onResize((w, h) -> scene.getCamera().getProjection().update(w, h));
		
		//((Camera3D) scene.getCamera()).lookAt(new Vector3f(0, -5, 9.17f).mul(0.5f), new Vector3f().zero());
		//((Camera3D) scene.getCamera()).getProjection().setPerspective(false);
		//((Camera3D) scene.getCamera()).getProjection().setSize(100f);
		
		try {
			fw = new FileWriter(new File("./logs/io.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	float GX = 0;
	
	protected float camSpeed = 0.1f;
	protected float camRotSpeed = (float) Math.toRadians(2.5);
	
	protected FileWriter fw;
	
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
				
				cam.move(cx, cy, camSpeed);
				
				cam.move(ax, ay, bx, by, camSpeed, camRotSpeed);
				
				float scrollSpeed = 0.5f;
				
				cam.getProjection().setSize((float) (org.joml.Math.clamp(0, 150, cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				cam.getProjection().update();
				
				System.err.println("ax"+ax+" ay"+ay+" : bx"+bx+" by"+by+" : sx"+engine.getWindow().getScroll().x+" sy"+engine.getWindow().getScroll().y+" > s"+cam.getProjection().getSize());
				try {
					fw.write("ax"+ax+" ay"+ay+" : bx"+bx+" by"+by+" : sx"+engine.getWindow().getScroll().x+" sy"+engine.getWindow().getScroll().y+" > s"+cam.getProjection().getSize()+"\n");
					fw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				cam.updateMatrix();
				
				light.setPosition(new Vector3f(0, (float) Math.sin(GX)+1.5f, 0));
				
				GX += 0.01f;
				
				tModel.getTransform().scaleMul(0, 0, 0).rotate(0.5f, 0.2f, 0.1f).updateMatrix();
				
				//material.setProperty(DiffuseShader.DIFFUSE_COLOR, new Vector3f(x2/2+0.5f, y2/2+0.5f, x/2+0.5f));
				
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
		//((Transform3D) model.getTransform()).rotate(0.1f, 0.05f, 0.025f).updateMatrix();
		//Logger.log();
	}

	@Override
	public void render(float dTime) {
		//Logger.log();
		compositor.render(engine.getCache(), engine);
		//scene3DRenderer.render(engine.getCache(), engine, scene);
	}

}
