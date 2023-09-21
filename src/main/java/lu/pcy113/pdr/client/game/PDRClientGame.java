package lu.pcy113.pdr.client.game;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.RenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.blur.gaussian.GaussianBlurMaterial;
import lu.pcy113.pdr.engine.graph.composition.blur.gaussian.GaussianBlurShader;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.render.GizmoModelRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.PointLight;
import lu.pcy113.pdr.engine.scene.Camera3D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.utils.ObjLoader;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class PDRClientGame implements GameLogic {
	
	private GameEngine engine;
	
	protected float camSpeed = 0.1f;
	protected float camRotSpeed = (float) Math.toRadians(2.5);
	
	protected Mesh mesh;
	protected Model model;
	protected Shader diffShader, txtDiffShader;
	protected Texture diffTexture, specTexture;
	protected Material material, txtMaterial;
	protected Scene3D scene;
	protected PointLight light;
	protected Scene3DRenderer scene3DRenderer;
	
	protected Compositor compositor;
	
	public PDRClientGame() {
		Logger.log();
	}
	
	@Override
	public void init(GameEngine e) {
		Logger.log();
		
		this.engine = e;
		GameEngine.DEBUG.wireframe = true;
		GameEngine.DEBUG.wireframeColor = new Vector4f(0.5f, 0, 0, 1);
		//GameEngine.DEBUG.ignoreDepth = false;
		
		
		this.txtDiffShader = new TxtDiffuse1Shader();
		engine.getCache().addShader(txtDiffShader);
		
		this.diffTexture = new Texture("cube-diffTexture", "./resources/textures/cube.png");
		engine.getCache().addTexture(diffTexture);
		
		this.specTexture = new Texture("cube-specTexture", "./resources/textures/default.png");
		engine.getCache().addTexture(specTexture);
		
		this.txtMaterial = new TxtDiffuseMaterial("txtDiffuse", diffTexture.getId(), new Vector3f(1), specTexture.getId(), 0.5f);
		engine.getCache().addMaterial(txtMaterial);
		
		
		this.diffShader = new DiffuseShader();
		engine.getCache().addShader(diffShader);
		
		this.material = new DiffuseMaterial("diffuse", new Vector3f(1, 0.5f, 0.5f), new Vector3f(0.5f, 0.2f, 0), new Vector3f(1), 0.5f);
		engine.getCache().addMaterial(material);
		
		Mesh chestMesh = ObjLoader.loadMesh("chest-mesh", GizmoMaterial.NAME, "./resources/models/chest.obj");
		Model chestModel = new Model("chest-model", chestMesh.getId(), new Transform3D());
		((Transform3D) chestModel.getTransform()).translateAdd(5, 0, 5).updateMatrix();
		engine.getCache().addMesh(chestMesh);
		engine.getCache().addModel(chestModel);
		
		this.mesh = ObjLoader.loadMesh("cube-mesh", txtMaterial.getId(), "./resources/models/cube.obj");
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
		this.scene.addModel(model.getId());
		this.scene.addModel(model1.getId());
		this.scene.addModel(model2.getId());
		this.scene.addPointLight(light.getId());
		this.scene.addModel(chestModel.getId());
		engine.getCache().addScene(scene);
		
		Gizmo gizmo = ObjLoader.loadGizmo("gizmo", "./resources/models/cube_wireframe.obj");
		GizmoModel gizmoModel = new GizmoModel("gizmoModel", gizmo.getId(), new Transform3D());
		((Transform3D) gizmoModel.getTransform()).translateAdd(0.5f, 0.5f, 0.5f).updateMatrix();
		
		engine.getCache().addGizmo(gizmo);
		engine.getCache().addGizmoModel(gizmoModel);
		this.scene.addGizmoModel(gizmoModel.getId());
		
		Gizmo gizmoAxisGrid = ObjLoader.loadGizmo("gizmoGrid", "./resources/models/axis_grid.obj");
		GizmoModel gizmoModelAxisGrid = new GizmoModel("gizmoModelGridXYZ", gizmoAxisGrid.getId(), new Transform3D());
		//((Transform3D) gizmoModelAxisGrid.getTransform()).rotateFromAxisAngleRad(0, 1, 0, 90).updateMatrix();
		
		engine.getCache().addGizmo(gizmoAxisGrid);
		engine.getCache().addGizmoModel(gizmoModelAxisGrid);
		this.scene.addGizmoModel(gizmoModelAxisGrid.getId());
		
		this.scene3DRenderer = new Scene3DRenderer();
		engine.getCache().addRenderer(scene3DRenderer);
		engine.getCache().addRenderer(new MeshRenderer());
		engine.getCache().addRenderer(new ModelRenderer());
		engine.getCache().addRenderer(new GizmoModelRenderer());
		
		Shader passRenderShader = new BackgroundShader(0);
		Material passRenderMaterial = new BackgroundMaterial(0);
		engine.getCache().addShader(passRenderShader);
		engine.getCache().addMaterial(passRenderMaterial);
		
		Shader blurRenderShader = new GaussianBlurShader();
		Material blurRenderMaterial = new GaussianBlurMaterial(5, 5);
		engine.getCache().addShader(blurRenderShader);
		engine.getCache().addMaterial(blurRenderMaterial);
		
		PassRenderLayer passRender = new PassRenderLayer("background", passRenderMaterial.getId());
		engine.getCache().addRenderLayer(passRender);
		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", scene);
		engine.getCache().addRenderLayer(sceneRender);
		PassRenderLayer blurRender = new PassRenderLayer("blur", blurRenderMaterial.getId());
		engine.getCache().addRenderLayer(blurRender);
		
		compositor = new Compositor();
		compositor.addRenderLayer(0, passRender.getId());
		//compositor.addRenderLayer(1, sceneRender.getId());
		//compositor.addRenderLayer(2, sceneRender.getId());
		
		engine.getWindow().onResize((w, h) -> scene.getCamera().getProjection().perspectiveUpdateMatrix(w, h));
	}
	
	float GX = 0;
	
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
				//cam.rotate(x2 * camRotSpeed, y2 * camRotSpeed);
				//cam.moveLocal(x * camSpeed, y * camSpeed);
				
				cam.rotate(x2*camRotSpeed, y2*camRotSpeed);
				cam.moveLocalXZ(x*camSpeed, y*camSpeed);
				
				/*((Transform3D) model.getTransform()).setRotation(cam.getRotation()).getRotation();//.mul(-1);
				model.getTransform().updateMatrix();*/
				
				cam.updateMatrix();
				
				light.setPosition(new Vector3f(0, (float) Math.sin(GX)+1.5f, 0));
				
				GX += 0.01f;
				
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
