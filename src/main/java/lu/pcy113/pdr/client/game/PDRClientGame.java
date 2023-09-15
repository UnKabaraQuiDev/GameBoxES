package lu.pcy113.pdr.client.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
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
	
	public PDRClientGame() {
		Logger.log();
	}
	
	@Override
	public void init(GameEngine e) {
		Logger.log();
		
		this.engine = e;
		GameEngine.DEBUG.wireframe = false;
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
		//((Camera3D) scene.getCamera()).setPosition(new Vector3f(-10, 10, 0)).updateMatrix();
		//this.scene.addMesh(mesh.getId());
		this.scene.addModel(model.getId());
		this.scene.addModel(model1.getId());
		this.scene.addModel(model2.getId());
		this.scene.addPointLight(light.getId());
		engine.getCache().addScene(scene);
		
		Gizmo gizmo = ObjLoader.loadGizmo("gizmo", "./resources/models/cube_wireframe.obj");
		GizmoModel gizmoModel = new GizmoModel("gizmoModel", gizmo.getId(), new Transform3D());
		((Transform3D) gizmoModel.getTransform()).translateAdd(0.5f, 0.5f, 0.5f).updateMatrix();
		
		engine.getCache().addGizmo(gizmo);
		engine.getCache().addGizmoModel(gizmoModel);
		this.scene.addGizmoModel(gizmoModel.getId());
		
		/*Gizmo gizmoGrid = ObjLoader.loadGizmo("gizmoGrid", "./resources/models/grid.obj");
		GizmoModel gizmoModelGridx = new GizmoModel("gizmoModelGridY", gizmoGrid.getId(), new Transform3D());
		((Transform3D) gizmoModelGridx.getTransform()).rotateFromAxisAngleRad(0, 1, 0, 90).updateMatrix();
		GizmoModel gizmoModelGridy = new GizmoModel("gizmoModelGridX", gizmoGrid.getId(), new Transform3D());
		((Transform3D) gizmoModelGridy.getTransform()).rotateFromAxisAngleRad(1, 0, 0, 90).updateMatrix();
		GizmoModel gizmoModelGridz = new GizmoModel("gizmoModelGridZ", gizmoGrid.getId(), new Transform3D());
		((Transform3D) gizmoModelGridz.getTransform()).rotateFromAxisAngleRad(0, 0, 1, 90).updateMatrix();
		
		engine.getCache().addGizmo(gizmoGrid);
		engine.getCache().addGizmoModel(gizmoModelGridx);
		this.scene.addGizmoModel(gizmoModelGridx.getId());
		engine.getCache().addGizmoModel(gizmoModelGridy);
		this.scene.addGizmoModel(gizmoModelGridy.getId());
		engine.getCache().addGizmoModel(gizmoModelGridz);
		this.scene.addGizmoModel(gizmoModelGridz.getId());*/
		
		Gizmo gizmoAxisGrid = ObjLoader.loadGizmo("gizmoGrid", "./resources/models/axis_grid.obj");
		GizmoModel gizmoModelAxisGrid = new GizmoModel("gizmoModelGridY", gizmoAxisGrid.getId(), new Transform3D());
		//((Transform3D) gizmoModelAxisGrid.getTransform()).rotateFromAxisAngleRad(0, 1, 0, 90).updateMatrix();
		
		engine.getCache().addGizmo(gizmoAxisGrid);
		engine.getCache().addGizmoModel(gizmoModelAxisGrid);
		this.scene.addGizmoModel(gizmoModelAxisGrid.getId());
		
		this.scene3DRenderer = new Scene3DRenderer();
		engine.getCache().addRenderer(scene3DRenderer);
		engine.getCache().addRenderer(new MeshRenderer());
		engine.getCache().addRenderer(new ModelRenderer());
		engine.getCache().addRenderer(new GizmoModelRenderer());
		
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
				/*Quaternionf q = cam.getRotation();
				
				Vector3f localX = q.transform(new Vector3f(1, 0, 0)).normalize();
				Vector3f localY = q.transform(new Vector3f(0, 1, 0)).normalize();
				Vector3f localZ = q.transform(new Vector3f(0, 0, 1)).normalize();*/
				
				/*Matrix4f matrix = new Matrix4f()
						.identity();
				q.get(matrix);
				
				Vector3f localX = matrix.getColumn(0, new Vector3f());
				Vector3f localY = matrix.getColumn(1, new Vector3f());
				Vector3f localZ = matrix.getColumn(2, new Vector3f());*/
				
				/*cam.getPosition()
						.add(localZ.mul(y*camSpeed))
						.add(localY.mul(x*camSpeed));*/
				
				/*cam.setYaw(cam.getYaw()+x2*camRotSpeed);
				cam.setPitch(cam.getPitch()+y2*camRotSpeed);*/
				/*cam.getRotation().rotateLocalY(x2*camRotSpeed);
				cam.getRotation().rotateLocalZ(y2*camRotSpeed);*/
				
				cam.moveLocal(x * camSpeed, y * camSpeed);
				cam.rotate(x2 * camRotSpeed, y2 * camRotSpeed);
				
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
		((Transform3D) model.getTransform()).rotate(0.1f, 0.05f, 0.025f).updateMatrix();
		//Logger.log();
	}

	@Override
	public void render(float dTime) {
		//Logger.log();
		scene3DRenderer.render(engine.getCache(), engine, scene);
	}

}
