package lu.pcy113.pdr.client.game;

import java.util.HashMap;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.opengl.GL11;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.client.game.options.KeyOptions;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
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
import lu.pcy113.pdr.engine.utils.interpol.Interpolators;
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

	CallbackValueInterpolation<BackgroundMaterial, Vector4f> genMatInterpolation;

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
				this.createUniform(Shader.PROJECTION_MATRIX);
				this.createUniform(Shader.VIEW_MATRIX);
				this.createUniform(Shader.TRANSFORMATION_MATRIX);
			}
		};
		this.cache.addShader(shader1);

		Shader partShader = new Shader("partMain", true, new ShaderPart("./resources/shaders/main/uv.frag"),
				new ShaderPart("./resources/shaders/parts/main.vert")) {
			@Override
			public void createUniforms() {
				this.createUniform(Shader.PROJECTION_MATRIX);
				this.createUniform(Shader.VIEW_MATRIX);
				this.createUniform(Shader.TRANSFORMATION_MATRIX);
			}
		};
		this.cache.addShader(partShader);

		Material partMat = new Material("parts", partShader);
		this.cache.addMaterial(partMat);
		Mesh partCube = ObjLoader.loadMesh("partCube", partMat.getId(), "./resources/models/cube.obj");
		this.cache.addMesh(partCube);
		this.parts = new InstanceEmitter("parts", partCube, 200, new Transform3D());
		this.parts.update((part) -> {
			((Transform3D) part.getTransform())
					.setTranslation(new Vector3f((float) Math.cos(2 * Math.PI / this.parts.getParticleCount() * part.getIndex()),
							(float) Math.sin(2 * Math.PI / this.parts.getParticleCount() * part.getIndex()),
							(float) Math.sin(2 * Math.PI / this.parts.getParticleCount() * part.getIndex() * 5)))
					.setScale(new Vector3f(0.05f, 0.05f, 0.05f)).updateMatrix();
		});
		this.cache.addInstanceEmitter(this.parts);

		Material mat = new Material("main", shader1);
		this.cache.addMaterial(mat);
		Mesh plane = ObjLoader.loadMesh("chest", mat.getId(), "./resources/models/plane.obj");
		this.cache.addMesh(plane);
		this.plane = new Model("plane", plane);
		this.cache.addModel(this.plane);
		this.partsModel = new InstanceEmitterModel("parts", this.parts);
		this.cache.addInstanceEmitterModel(this.partsModel);

		TextMesh textMesh = new TextMesh(64);
		this.cache.addMesh(textMesh);
		FillTextShader textShader = new FillTextShader();
		this.cache.addShader(textShader);
		Texture rasterTxt = new Texture("raster", "./resources/textures/fonts/font1.png", GL11.GL_NEAREST);
		this.cache.addTexture(rasterTxt);
		Texture rasterIndexTxt = new Texture("rasterIndex", "./resources/textures/fonts/font1indices.png", GL11.GL_NEAREST);
		this.cache.addTexture(rasterIndexTxt);
		this.textMaterial = new FillTextMaterial("fill", textShader, "raster", "rasterIndex", new Vector4f(0.2f, 0.5f, 0.8f, 1));
		this.cache.addMaterial(this.textMaterial);
		TextModel txtModel = new TextModel("text", this.textMaterial, "abcdefghijklmnop", new Vector2f(0.2f));
		this.cache.addTextModel(txtModel);

		this.scene = new Scene3D("main-scene");
		// this.scene.addEntity("mesh", new Entity(new
		// MeshComponent(partCube))).setActive(false);
		this.planeEntity = this.scene.addEntity("model", new Entity(new ModelComponent(this.plane),
				new Transform3DComponent(new Transform3D(new Vector3f(1, 1, 1), new Quaternionf(), new Vector3f(0.2f)))));
		this.partsModelEntity = this.scene.addEntity("parts",
				new Entity(new InstanceEmitterModelComponent(this.partsModel), new Transform3DComponent()));
		this.txtModelEntity = this.scene.addEntity("text", new Entity(new TextModelComponent(txtModel), new Transform3DComponent()));

		Gizmo gizmoXYZ = ObjLoader.loadGizmo("gizmoXYZ", "./resources/models/gizmos/grid_xyz.obj");
		this.cache.addGizmo(gizmoXYZ);
		GizmoModel gizmoXYZModel = new GizmoModel("gizmoXYZ", gizmoXYZ.getId());
		this.cache.addGizmoModel(gizmoXYZModel);
		this.scene.addEntity("gizmoXYZ", new Entity(new GizmoModelComponent(gizmoXYZModel), new Transform3DComponent()));

		this.cache.addRenderer(new Scene3DRenderer());
		this.cache.addRenderer(new Scene2DRenderer());
		this.cache.addRenderer(new ModelRenderer());
		this.cache.addRenderer(new GizmoModelRenderer());
		this.cache.addRenderer(new MeshRenderer());
		this.cache.addRenderer(new InstanceEmitterModelRenderer());
		this.cache.addRenderer(new InstanceEmitterRenderer());
		this.cache.addRenderer(new TextModelRenderer());
		this.cache.addRenderer(new UIModelRenderer());

		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", this.scene);
		this.cache.addRenderLayer(sceneRender);

		BackgroundShader genShader = new BackgroundShader(0);
		this.cache.addShader(genShader);
		this.genMat = new BackgroundMaterial(0, genShader);
		this.cache.addMaterial(this.genMat);
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", this.genMat.getId());
		this.cache.addRenderLayer(genLayer);

		this.ui = new Scene2D("ui", Camera.orthographicCamera3D());
		this.cache.addScene(this.ui);
		Shader slotShader = new Shader("slot", true, new ShaderPart("./resources/shaders/ui/plain.vert"),
				new ShaderPart("./resources/shaders/ui/txt1.frag")) {
			@Override
			public void createUniforms() {
				this.createUniform(Shader.TRANSFORMATION_MATRIX);
				this.createUniform(Shader.VIEW_MATRIX);
				this.createUniform(Shader.PROJECTION_MATRIX);

				this.createUniform(BG_COLOR);
				this.createUniform(FG_COLOR);
			}
		};
		this.cache.addShader(slotShader);
		Texture slotTexture = new Texture("slot", "./resources/textures/ui/single_slot.png");
		this.cache.addTexture(slotTexture);
		Material slotMaterial = new TextureMaterial("slot", slotShader, new HashMap<String, String>() {
			{
				this.put("txt1", slotTexture.getId());
			}
		});
		this.cache.addMaterial(slotMaterial);
		Mesh slot = ObjLoader.loadMesh("slot", slotMaterial.getId(), "./resources/models/cube.obj");
		this.cache.addMesh(slot);
		this.slotModel = new UIModel("slot", slot);
		this.cache.addUIModel(this.slotModel);
		this.slotModelEntity = this.ui.addEntity("slot", new Entity(new UIModelComponent(this.slotModel), new Transform2DComponent()));

		SceneRenderLayer uiRender = new SceneRenderLayer("ui", this.ui);
		this.cache.addRenderLayer(uiRender);

		this.compositor = new Compositor();
		this.compositor.addRenderLayer(0, genLayer);
		this.compositor.addRenderLayer(1, sceneRender);
		this.compositor.addRenderLayer(2, uiRender);

		this.scene.getCamera().getProjection().setPerspective(true);
		this.engine.getWindow().onResize((w, h) -> {
			this.scene.getCamera().getProjection().update(w, h);
			this.ui.getCamera().getProjection().update(w, h);
		});
		this.engine.getWindow().setBackground(new Vector4f(1, 1, 1, 1));

		Camera3D cam = (Camera3D) this.scene.getCamera();
		cam.getProjection().setPerspective(true);
		cam.getProjection().setFov((float) Math.toRadians(60));
		// cam.setPosition(new Vector3f(5, 0, 0)).updateMatrix();

		Camera3D uiCam = (Camera3D) this.ui.getCamera();
		uiCam.setPosition(new Vector3f(0, 0, 1));
		uiCam.setProjection(new Projection(0.01f, 1000f, -1, 1, -1, 1));
		uiCam.setRotation(new Quaternionf().lookAlong(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0)));
		uiCam.updateMatrix();

		this.genMatInterpolation = new CallbackValueInterpolation<BackgroundMaterial, Vector4f>(this.genMat, new Vector4f(0, 1, 0, 1),
				new Vector4f(1, 0, 1, 1), Interpolators.BOUNCE_IN_OUT) {
			@Override
			public Vector4f evaluate(float progress) {
				return new Vector4f(this.start).lerp(this.end, progress);
			}

			@Override
			public void callback(BackgroundMaterial object, Vector4f value) {
				object.setColor(value);
			}
		};
	}

	float GX = 0;

	protected float camSpeed = 0.1f;
	protected float camRotSpeed = (float) Math.toRadians(2.5);

	private float applyThreshold(float axes, float f) {
		return Math.abs(axes) < f ? 0 : axes;
	}

	@Override
	public void input(float dTime) {
		if (this.engine.getWindow().isJoystickPresent()) {
			if (this.engine.getWindow().updateGamepad(0)) {
				GLFWGamepadState gps = this.engine.getWindow().getGamepad();

				float ax = this.applyThreshold(gps.axes(0), 0.01f);
				float ay = this.applyThreshold(gps.axes(1), 0.01f) * (-1);

				float bx = this.applyThreshold(gps.axes(2), 0.01f);
				float by = this.applyThreshold(gps.axes(3), 0.01f) * (-1);

				Camera3D cam = (Camera3D) this.scene.getCamera();

				cam.roll(by * this.camRotSpeed);

				float cy = (this.engine.getWindow().isKeyPressed(KeyOptions.FORWARD) ? 1 : 0)
						- (this.engine.getWindow().isKeyPressed(KeyOptions.BACKWARD) ? 1 : 0);
				float cx = (this.engine.getWindow().isKeyPressed(KeyOptions.RIGHT) ? 1 : 0)
						- (this.engine.getWindow().isKeyPressed(KeyOptions.LEFT) ? 1 : 0);

				System.err.println(ax + " " + ay + " : " + bx + " " + by + " : " + cx + " " + cy);

				cam.move(cx, cy, this.camSpeed);

				cam.move(ax, ay, bx, by, this.camSpeed, this.camRotSpeed);

				Camera3D uiCam = (Camera3D) this.ui.getCamera();
				// uiCam.setRotation(uiCam.getRotation().rotateXYZ(0.1f, 0.1f, 0.1f));
				uiCam.updateMatrix();

				// uiCam.getProjection().setSize((float) (org.joml.Math.clamp(0.5f, 150f,
				// cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				// uiCam.getProjection().setSize(GX%1*150f);
				// System.out.println("NEW: " + uiCam.getProjection().getSize());
				// uiCam.getProjection().update();
				// uiCam.move(ax, ay, bx, by, camSpeed, camRotSpeed).updateMatrix();

				System.err.println(cam.getViewMatrix());

				// cam.getRotation().rotateZ(0.1f);
				cam.updateMatrix();

				this.planeEntity.getComponent(Transform3DComponent.class).getTransform().rotate(0.01f, 0.01f, 0.01f).updateMatrix();

				// this.genMat.setColor(new Vector4f(this.GX % 1, this.GX % 1, this.GX % 1, 1));
				// this.engine.getWindow().setBackground(new Vector4f(this.GX % 1, this.GX % 1,
				// this.GX % 1, 1));

				// cam.getProjection().setSize((float) (org.joml.Math.clamp(0, 150,
				// cam.getProjection().getSize()+engine.getWindow().getScroll().y*scrollSpeed)));
				// cam.getProjection().update();
			}
		}
	}

	@Override
	public void update(float dTime) {
		this.GX += 0.01f;

		/*
		 * cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial()).
		 * setProperty(BG_COLOR, new Vector3f(0.112f / 255, 0.112f / 255, 0.112f /
		 * 255)); cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial()).
		 * setProperty(FG_COLOR, new Vector3f(1 * (GX % 3) / 3, 1 * ((GX + 1) % 3) / 3,
		 * 1 * ((GX + 2) % 3) / 3));
		 * cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial() +
		 * "2").setProperty(BG_COLOR, new Vector3f(0.112f / 255, 0.112f / 255, 0.112f /
		 * 255)); cache.getMaterial(cache.getMesh(slotModel.getMesh()).getMaterial() +
		 * "2").setProperty(FG_COLOR, new Vector3f(1 * (GX % 3) / 3, 1 * ((GX + 1) % 3)
		 * / 3, 1 * ((GX + 2) % 3) / 3));
		 */

		this.genMatInterpolation.set(this.GX % 1);

		// slotModelEntity.getComponent(Transform3DComponent.class).getTransform().rotate(GX%1,
		// GX%1, GX%1).updateMatrix();
		this.slotModelEntity.getComponent(Transform2DComponent.class).getTransform().rotate((this.GX % 1) * 0.01f).updateMatrix();
		// slotModelEntity.setActive(false);

		this.textMaterial.setColor(new Vector4f(this.GX % 1, this.GX % 1, this.GX % 1, 1));
		this.textMaterial.setProperty(TextShader.SIZE, this.GX % 1);

		/*
		 * ((Transform2D)
		 * slotModelEntity.getComponent(Transform2DComponent.class).getTransform())
		 * .translateMul(new Vector2f((float) Math.cos(2 * Math.PI * (GX % 1)), (float)
		 * Math.sin(2 * Math.PI * (GX % 1))).normalize().mul(0.1f), 0.95f, 0.95f)
		 * .rotate(-5).updateMatrix();
		 */

		this.partsModel.getEmitter(this.cache).update((part) -> {
			((Transform3D) part.getTransform()).rotate(0, 0, 0.01f).updateMatrix();
		});
		this.partsModelEntity.getComponent(Transform3DComponent.class).getTransform().rotate(0, 0, -0.1f).updateMatrix();
	}

	@Override
	public void render(float dTime) {
		this.compositor.render(this.engine.getCache(), this.engine);
	}

}
