package lu.pcy113.pdr.client.game.three;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pdr.client.game.three.FillShader.FillMaterial;
import lu.pcy113.pdr.client.game.three.SlotInstanceShader.SlotInstanceMaterial;
import lu.pcy113.pdr.client.game.three.SlotShader.SlotMaterial;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.ObjLoader;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene2DRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.graph.window.Window;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.scene.camera.Projection;
import lu.pcy113.pdr.engine.utils.interpol.Interpolators;
import lu.pcy113.pdr.engine.utils.transform.Transform2D;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class PDRClientGame3 implements GameLogic {
	
	CacheManager cache;
	GameEngine engine;
	Window window;
	
	SlotMaterial slotMaterial;
	FillMaterial backgroundMaterial;
	SlotInstanceMaterial slotInstMaterial;
	
	CallbackValueInterpolation<FillMaterial, Vector4f> backgroundMaterialInterpolation;
	
	InstanceEmitter slotInstancer;
	
	Entity slotEntity, slotEntityUi;
	
	Camera3D camera;
	Camera3D cameraUi;
	Scene3D scene;
	Scene2D ui;
	
	Compositor compositor;
	
	@Override
	public void init(GameEngine e) {
		engine = e;
		cache = e.getCache();
		window = e.getWindow();
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.wireframeColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = true;
		
		
		Texture slotTexture = cache.loadTexture("single_slot", "./resources/textures/ui/single_slot.png");
		slotMaterial = (SlotMaterial) cache.loadMaterial(SlotShader.SlotMaterial.class, slotTexture);
		Mesh slotMesh = ObjLoader.loadMesh("slot", slotMaterial, "./resources/models/plane.obj");
		this.cache.addMesh(slotMesh);
		
		
		this.backgroundMaterial = (FillMaterial) cache.loadMaterial(FillShader.FillMaterial.class);
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", this.backgroundMaterial);
		this.cache.addRenderLayer(genLayer);
		
		
		slotInstMaterial = (SlotInstanceMaterial) cache.loadMaterial(SlotInstanceShader.SlotInstanceMaterial.class, slotTexture);
		Mesh slotInstMesh = ObjLoader.loadMesh("slotInst", slotInstMaterial, "./resources/models/plane.obj");
		this.cache.addMesh(slotInstMesh);
		slotInstancer = cache.loadInstanceEmitter("slotEmitter", slotInstMesh, 3*5, new Transform3D());
		slotInstancer.update((inst) -> {
			((Transform3D) inst.getTransform()).setTranslation(new Vector3f(inst.getIndex()%3-1f, (inst.getIndex()/3)-2f, 0f).mul(1.1f));
			System.out.println("instance: "+inst.getIndex()+" > "+((Transform3D) inst.getTransform()).getTranslation());
			((Transform3D) inst.getTransform()).updateMatrix();
		});
		slotEntityUi = new Entity(new Transform3DComponent(new Transform3D().setTranslation(new Vector3f(4, 0, 0))), new InstanceEmitterComponent(slotInstancer));
		//slotEntityUi.getComponent(Transform3DComponent.class).getTransform().getRotation().rotateAxis((float) Math.toRadians(90), GameEngine.UP);
		slotEntityUi.getComponent(Transform3DComponent.class).getTransform().updateMatrix();
		
		ui = new Scene2D("ui", Camera.orthographicCamera3D());
		ui.addEntity("slotUi", slotEntityUi);
		
		scene = new Scene3D("main");
		slotEntity = new Entity(new Transform3DComponent(), new MeshComponent(slotMesh));
		scene.addEntity("slot", this.slotEntity);
		
		this.cache.addRenderer(new Scene3DRenderer());
		this.cache.addRenderer(new Scene2DRenderer());
		this.cache.addRenderer(new MeshRenderer());
		this.cache.addRenderer(new InstanceEmitterRenderer());
		
		
		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", this.scene);
		this.cache.addRenderLayer(sceneRender);
		
		SceneRenderLayer uiRender = new SceneRenderLayer("ui", this.ui);
		this.cache.addRenderLayer(uiRender);
		
		
		this.compositor = new Compositor();
		this.compositor.addRenderLayer(0, genLayer);
		this.compositor.addRenderLayer(1, sceneRender);
		this.compositor.addRenderLayer(2, uiRender);
		
		
		camera = (Camera3D) this.scene.getCamera();
		camera.getProjection().setPerspective(true);
		camera.getProjection().setFov((float) Math.toRadians(60));
		
		cameraUi = (Camera3D) this.ui.getCamera();
		cameraUi.setProjection(new Projection(0.01f, 100f, -0.5f, 0.5f, 0.5f, -0.5f));
		cameraUi.setPosition(new Vector3f(0, 0, -1f));
		cameraUi.updateMatrix();
		
		this.scene.getCamera().getProjection().setPerspective(true);
		this.engine.getWindow().onResize((w, h) -> {
			this.scene.getCamera().getProjection().update(w, h);
			this.ui.getCamera().getProjection().update(w, h);
		});
		this.engine.getWindow().setBackground(new Vector4f(0.1f));
		
		
		this.backgroundMaterialInterpolation = new CallbackValueInterpolation<FillMaterial, Vector4f>(
				this.backgroundMaterial,
				new Vector4f(0, 1, 0, 1),
				new Vector4f(1, 0, 1, 1),
				Interpolators.BOUNCE_IN_OUT) {
			@Override
			public Vector4f evaluate(float progress) {
				return new Vector4f(this.start).lerp(this.end, progress);
			}

			@Override
			public void callback(FillMaterial object, Vector4f value) {
				object.setColor(value);
			}
		};
		
		cache.dump(System.out);
	}

	@Override
	public void input(float dTime) {
		/*cameraUi.getPosition().add(
				(window.isKeyPressed(GLFW.GLFW_KEY_Q)?0.1f:0)-(window.isKeyPressed(GLFW.GLFW_KEY_D)?0.1f:0),
				0,
				(window.isKeyPressed(GLFW.GLFW_KEY_Z)?0.1f:0)-(window.isKeyPressed(GLFW.GLFW_KEY_S)?0.1f:0));
		cameraUi.setPosition(new Vector3f(0, 0, -1f));
		cameraUi.updateMatrix();*/
		
		camera.getPosition().add(
				(window.isKeyPressed(GLFW.GLFW_KEY_Q)?0.1f:0)-(window.isKeyPressed(GLFW.GLFW_KEY_D)?0.1f:0),
				0,
				(window.isKeyPressed(GLFW.GLFW_KEY_Z)?0.1f:0)-(window.isKeyPressed(GLFW.GLFW_KEY_S)?0.1f:0));
		camera.updateMatrix();
	}
	
	float GX = 0f;
	
	@Override
	public void update(float dTime) {
		GX = (GX + 0.01f) % 1;
		
		this.backgroundMaterialInterpolation.set(this.GX);
		
		slotEntity.getComponent(Transform3DComponent.class).getTransform().getRotation().rotateXYZ(0.01f, 0.01f, 0.01f);
		slotEntity.getComponent(Transform3DComponent.class).getTransform().updateMatrix();
		
		//slotInstancer.update((inst) -> ((Transform2D) inst.getTransform()).rotate((float) Math.toRadians(Math.random())).updateMatrix());
		
		//System.out.println("GX: "+GX+" int: "+backgroundMaterial.getColor());
	}

	@Override
	public void render(float dTime) {
		compositor.render(cache, engine);
	}

}