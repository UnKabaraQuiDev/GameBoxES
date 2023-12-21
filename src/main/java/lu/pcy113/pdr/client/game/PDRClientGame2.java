package lu.pcy113.pdr.client.game;

import java.util.HashMap;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.client.game.options.KeyOptions;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.ObjLoader;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.geom.text.TextMesh;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.graph.render.GizmoModelRenderer;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterModelRenderer;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.ModelRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene2DRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.render.TextModelRenderer;
import lu.pcy113.pdr.engine.graph.render.UIModelRenderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.logic.GameLogic;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.UIModel;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform2DComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;
import lu.pcy113.pdr.engine.objs.entity.components.UIModelComponent;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.scene.camera.Projection;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class PDRClientGame2
		implements
		GameLogic {

	GameEngine engine;
	CacheManager cache;

	Scene3D scene;
	Model plane;
	BackgroundMaterial genMat;
	InstanceEmitter parts;
	InstanceEmitterModel partsModel;
	FillTextMaterial textMaterial;
	UIModel slotModel;
	Scene2D ui;

	Entity planeEntity, partsModelEntity, txtModelEntity, slotModelEntity;

	Compositor compositor;

	final static String FG_COLOR = "fgColor", BG_COLOR = "bgColor";

	public PDRClientGame2() {
		GlobalLogger.log();
	}

	@Override
	public void init(GameEngine e) {
		GlobalLogger.log();

		this.engine = e;
		this.cache = e.getCache();
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.wireframeColor = new Vector4f(0.2f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = true;

		Shader shader1 = new Shader("main1", new ShaderPart("./resources/shaders/main/uv.frag"),
				new ShaderPart("./resources/shaders/main/main.vert")) {
			@Override
			public void createUniforms() {
				createUniform(Shader.PROJECTION_MATRIX);
				createUniform(Shader.VIEW_MATRIX);
				createUniform(Shader.TRANSFORMATION_MATRIX);
			}
		};
		cache.addShader(shader1);

		Shader partShader = new Shader("partMain", true, new ShaderPart("./resources/shaders/main/uv.frag"),
				new ShaderPart("./resources/shaders/parts/main.vert")) {
			@Override
			public void createUniforms() {
				createUniform(Shader.PROJECTION_MATRIX);
				createUniform(Shader.VIEW_MATRIX);
				createUniform(Shader.TRANSFORMATION_MATRIX);
			}
		};
		cache.addShader(partShader);

		Material partMat = new Material("parts", partShader);
		cache.addMaterial(partMat);
		Mesh partCube = ObjLoader.loadMesh("partCube", partMat.getId(), "./resources/models/cube.obj");
		cache.addMesh(partCube);
		parts = new InstanceEmitter("parts", partCube, 200, new Transform3D());
		parts.update((part) -> {
			((Transform3D) part.getTransform())
					.setTranslation(new Vector3f((float) Math.cos(2 * Math.PI / parts.getParticleCount() * part.getIndex()),
							(float) Math.sin(2 * Math.PI / parts.getParticleCount() * part.getIndex()),
							(float) Math.sin(2 * Math.PI / parts.getParticleCount() * part.getIndex() * 5)))
					.setScale(new Vector3f(0.05f, 0.05f, 0.05f)).updateMatrix();
		});
		cache.addInstanceEmitter(parts);

		Material mat = new Material("main", shader1);
		cache.addMaterial(mat);
		Mesh plane = ObjLoader.loadMesh("chest", mat.getId(), "./resources/models/plane.obj");
		cache.addMesh(plane);
		this.plane = new Model("plane", plane);
		cache.addModel(this.plane);
		partsModel = new InstanceEmitterModel("parts", parts);
		cache.addInstanceEmitterModel(partsModel);

		TextMesh textMesh = new TextMesh(64);
		cache.addMesh(textMesh);
		FillTextShader textShader = new FillTextShader();
		cache.addShader(textShader);
		Texture rasterTxt = new Texture("raster", "./resources/textures/fonts/font1.png", GL40.GL_NEAREST);
		cache.addTexture(rasterTxt);
		Texture rasterIndexTxt = new Texture("rasterIndex", "./resources/textures/fonts/font1indices.png", GL40.GL_NEAREST);
		cache.addTexture(rasterIndexTxt);
		textMaterial = new FillTextMaterial("fill", textShader, "raster", "rasterIndex", new Vector4f(0.2f, 0.5f, 0.8f, 1));
		cache.addMaterial(textMaterial);
		TextModel txtModel = new TextModel("text", textMaterial, "abcdefghijklmnop", new Vector2f(0.2f));
		cache.addTextModel(txtModel);

		this.scene = new Scene3D("main-scene");
		// this.scene.addEntity("mesh", new Entity(new
		// MeshComponent(partCube))).setActive(false);
		planeEntity = this.scene.addEntity("model", new Entity(new ModelComponent(this.plane),
				new Transform3DComponent(new Transform3D(new Vector3f(1, 1, 1), new Quaternionf(), new Vector3f(0.2f)))));
		partsModelEntity = this.scene.addEntity("parts", new Entity(new InstanceEmitterModelComponent(partsModel), new Transform3DComponent()));
		txtModelEntity = this.scene.addEntity("text", new Entity(new TextModelComponent(txtModel), new Transform3DComponent()));

		Gizmo gizmoXYZ = ObjLoader.loadGizmo("gizmoXYZ", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(gizmoXYZ);
		GizmoModel gizmoXYZModel = new GizmoModel("gizmoXYZ", gizmoXYZ.getId());
		cache.addGizmoModel(gizmoXYZModel);
		this.scene.addEntity("gizmoXYZ", new Entity(new GizmoModelComponent(gizmoXYZModel), new Transform3DComponent()));

		cache.addRenderer(new Scene3DRenderer());
		cache.addRenderer(new Scene2DRenderer());
		cache.addRenderer(new ModelRenderer());
		cache.addRenderer(new GizmoModelRenderer());
		cache.addRenderer(new MeshRenderer());
		cache.addRenderer(new InstanceEmitterModelRenderer());
		cache.addRenderer(new InstanceEmitterRenderer());
		cache.addRenderer(new TextModelRenderer());
		cache.addRenderer(new UIModelRenderer());

		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", scene);
		cache.addRenderLayer(sceneRender);

		BackgroundShader genShader = new BackgroundShader(0);
		cache.addShader(genShader);
		genMat = new BackgroundMaterial(0, genShader);
		cache.addMaterial(genMat);
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", genMat.getId());
		cache.addRenderLayer(genLayer);

		ui = new Scene2D("ui", Camera.orthographicCamera3D());
		cache.addScene(ui);
		Shader slotShader = new Shader("slot", true, new ShaderPart("./resources/shaders/ui/plain.vert"),
				new ShaderPart("./resources/shaders/ui/txt1.frag")) {
			@Override
			public void createUniforms() {
				createUniform(Shader.TRANSFORMATION_MATRIX);
				createUniform(Shader.VIEW_MATRIX);
				createUniform(Shader.PROJECTION_MATRIX);

				createUniform(BG_COLOR);
				createUniform(FG_COLOR);
			}
		};
		cache.addShader(slotShader);
		Texture slotTexture = new Texture("slot", "./resources/textures/ui/single_slot.png");
		cache.addTexture(slotTexture);
		Material slotMaterial = new TextureMaterial("slot", slotShader, new HashMap<String, String>() {
			{
				put("txt1", slotTexture.getId());
			}
		});
		cache.addMaterial(slotMaterial);
		Mesh slot = ObjLoader.loadMesh("slot", slotMaterial.getId(), "./resources/models/cube.obj");
		cache.addMesh(slot);
		slotModel = new UIModel("slot", slot);
		cache.addUIModel(slotModel);
		slotModelEntity = ui.addEntity("slot", new Entity(new UIModelComponent(slotModel), new Transform2DComponent()));

		SceneRenderLayer uiRender = new SceneRenderLayer("ui", ui);
		cache.addRenderLayer(uiRender);

		compositor = new Compositor();
		compositor.addRenderLayer(0, genLayer);
		compositor.addRenderLayer(1, sceneRender);
		compositor.addRenderLayer(2, uiRender);

		scene.getCamera().getProjection().setPerspective(true);
		engine.getWindow().onResize((w, h) -> {
			scene.getCamera().getProjection().update(w, h);
			ui.getCamera().getProjection().update(w, h);
		});
		engine.getWindow().setBackground(new Vector4f(1, 1, 1, 1));

		Camera3D cam = (Camera3D) scene.getCamera();
		cam.getProjection().setPerspective(true);
		cam.getProjection().setFov((float) Math.toRadians(60));
		// cam.setPosition(new Vector3f(5, 0, 0)).updateMatrix();
		
		Camera3D uiCam = (Camera3D) ui.getCamera();
		uiCam.setPosition(new Vector3f(0, 0, 1));
		uiCam.setProjection(new Projection(0.01f, 1000f, -1, 1, -1, 1));
		uiCam.setRotation(new Quaternionf().lookAlong(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
		uiCam.updateMatrix();
		
		int[] numbers = {1, 2, 3};
        System.out.println(numbers[3]); // Accessing an element out of bounds
	}

	float GX = 0;

	protected float camSpeed = 0.1f;
	protected float camRotSpeed = (float) Math.toRadians(2.5);

	private float applyThreshold(float axes, float f) {
		return Math.abs(axes) < f ? 0 : axes;
	}

	@Override
	public void input(float dTime) {
		if (engine.getWindow().isJoystickPresent()) {
			if (engine.getWindow().updateGamepad(0)) {
				GLFWGamepadState gps = engine.getWindow().getGamepad();

				float ax = applyThreshold(gps.axes(0), 0.01f);
				float ay = applyThreshold(gps.axes(1), 0.01f) * (-1);

				float bx = applyThreshold(gps.axes(2), 0.01f);
				float by = applyThreshold(gps.axes(3), 0.01f) * (-1);

				Camera3D cam = (Camera3D) scene.getCamera();

				cam.roll(by * camRotSpeed);

				float cy = (engine.getWindow().isKeyPressed(KeyOptions.FORWARD) ? 1 : 0)
						- (engine.getWindow().isKeyPressed(KeyOptions.BACKWARD) ? 1 : 0);
				float cx = (engine.getWindow().isKeyPressed(KeyOptions.RIGHT) ? 1 : 0) - (engine.getWindow().isKeyPressed(KeyOptions.LEFT) ? 1 : 0);

				System.err.println(ax + " " + ay + " : " + bx + " " + by + " : " + cx + " " + cy);

				cam.move(cx, cy, camSpeed);

				cam.move(ax, ay, bx, by, camSpeed, camRotSpeed);

				Camera3D uiCam = (Camera3D) ui.getCamera();
				//uiCam.setRotation(uiCam.getRotation().rotateXYZ(0.1f, 0.1f, 0.1f));
				uiCam.updateMatrix();
				
				// uiCam.getProjection().setSize((float) (org.joml.Math.clamp(0.5f, 150f,
				// cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				// uiCam.getProjection().setSize(GX%1*150f);
				//System.out.println("NEW: " + uiCam.getProjection().getSize());
				//uiCam.getProjection().update();
				//uiCam.move(ax, ay, bx, by, camSpeed, camRotSpeed).updateMatrix();

				System.err.println(cam.getViewMatrix());

				// cam.getRotation().rotateZ(0.1f);
				cam.updateMatrix();

				((Transform3D) planeEntity.getComponent(Transform3DComponent.class).getTransform()).rotate(0.01f, 0.01f, 0.01f).updateMatrix();

				genMat.setColor(new Vector4f(GX % 1, GX % 1, GX % 1, 1));
				engine.getWindow().setBackground(new Vector4f(GX % 1, GX % 1, GX % 1, 1));

				// cam.getProjection().setSize((float) (org.joml.Math.clamp(0, 150,
				// cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				// cam.getProjection().update();
			}
		}
	}

	@Override
	public void update(float dTime) {
		GX += 0.01f;

		/*cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial()).setProperty(BG_COLOR,
				new Vector3f(0.112f / 255, 0.112f / 255, 0.112f / 255));
		cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial()).setProperty(FG_COLOR,
				new Vector3f(1 * (GX % 3) / 3, 1 * ((GX + 1) % 3) / 3, 1 * ((GX + 2) % 3) / 3));
		cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial() + "2").setProperty(BG_COLOR,
				new Vector3f(0.112f / 255, 0.112f / 255, 0.112f / 255));
		cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial() + "2").setProperty(FG_COLOR,
				new Vector3f(1 * (GX % 3) / 3, 1 * ((GX + 1) % 3) / 3, 1 * ((GX + 2) % 3) / 3));*/
		
		//slotModelEntity.getComponent(Transform3DComponent.class).getTransform().rotate(GX%1, GX%1, GX%1).updateMatrix();
		slotModelEntity.getComponent(Transform2DComponent.class).getTransform().rotate((GX%1)*0.01f).updateMatrix();
		//slotModelEntity.setActive(false);
		
		textMaterial.setColor(new Vector4f(GX % 1, GX % 1, GX % 1, 1));
		textMaterial.setProperty(TextShader.SIZE, (float) GX % 1);

		/*((Transform2D) slotModelEntity.getComponent(Transform2DComponent.class).getTransform())
				.translateMul(new Vector2f((float) Math.cos(2 * Math.PI * (GX % 1)), (float) Math.sin(2 * Math.PI * (GX % 1))).normalize().mul(0.1f),
						0.95f, 0.95f)
				.rotate(-5).updateMatrix();*/

		partsModel.getEmitter(cache).update((part) -> {
			((Transform3D) part.getTransform()).rotate(0, 0, 0.01f).updateMatrix();
		});
		((Transform3D) partsModelEntity.getComponent(Transform3DComponent.class).getTransform()).rotate(0, 0, -0.1f).updateMatrix();
	}

	@Override
	public void render(float dTime) {
		compositor.render(engine.getCache(), engine);
	}

}
