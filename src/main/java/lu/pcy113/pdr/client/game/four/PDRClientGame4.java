package lu.pcy113.pdr.client.game.four;

import java.io.IOException;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL41;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.client.game.four.PlainShader.PlainMaterial;
import lu.pcy113.pdr.client.game.three.BoxBlurShader;
import lu.pcy113.pdr.client.game.three.BoxBlurShader.BoxBlurMaterial;
import lu.pcy113.pdr.client.game.three.FillShader;
import lu.pcy113.pdr.client.game.three.FillShader.FillMaterial;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec4fAttribArray;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.utils.ObjLoader;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.Framebuffer;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.graph.material.text.TextShader.TextMaterial;
import lu.pcy113.pdr.engine.graph.render.GizmoRenderer;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene2DRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.render.TextEmitterRenderer;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.impl.nexttask.NextTask;
import lu.pcy113.pdr.engine.impl.nexttask.NextTaskWorker;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Sound3DComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.scene.camera.Projection;
import lu.pcy113.pdr.engine.utils.MemImage;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.Ray;
import lu.pcy113.pdr.engine.utils.consts.FrameBufferAttachment;
import lu.pcy113.pdr.engine.utils.consts.TextureFilter;
import lu.pcy113.pdr.engine.utils.consts.TextureType;
import lu.pcy113.pdr.engine.utils.file.FileUtils;
import lu.pcy113.pdr.engine.utils.file.ShaderManager;
import lu.pcy113.pdr.engine.utils.interpolation.Interpolators;

public class PDRClientGame4 extends GameLogic {

	CallbackValueInterpolation<FillMaterial, Vector4f> backgroundMaterialInterpolation;
	FillMaterial backgroundMaterial;

	Camera3D camera, cameraUi;
	Scene3D scene;
	Scene2D ui;

	TextEmitter debugInfo;

	Entity defaultCube, rayEntity, debugFps;

	Compositor compositor;

	NextTaskWorker worker;

	ShaderManager sm;

	@Override
	public void init(GameEngine e) {
		System.err.println("Cache: " + cache);

		GameEngine.DEBUG.wireframe = true;
		GameEngine.DEBUG.wireframeColor = new Vector4f(1f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = true;
		
		//GlobalLogger.getLogger().setForwardContent(false);

		try {
			sm = new ShaderManager(cache, FileUtils.RESOURCES + FileUtils.SHADERS);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ui = new Scene2D("ui", Camera.orthographicCamera3D());
		scene = new Scene3D("main");

		Gizmo ray = new Gizmo("ray", new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(0, 0, 0), new Vector3f(1, 0, 0) }), new UIntAttribArray("ind", -1, 1, new int[] { 0, 1 }),
				new Vec4fAttribArray("color", 1, 1, new Vector4f[] { new Vector4f(0.5f), new Vector4f(1) }));
		cache.addGizmo(ray);
		rayEntity = scene.addEntity("ray", new GizmoComponent(ray), new Transform3DComponent());

		PlainMaterial cubeMat = (PlainMaterial) cache.loadMaterial(PlainShader.PlainMaterial.class);
		sm.monitorShader(cubeMat.getRenderShader()/* , "./resources/shaders/plain.vert", "./resources/shaders/plain.frag" */);
		cubeMat.setColor(new Vector4f(1));
		Mesh cube = Mesh.newCube("cube", cubeMat, new Vector3f(1)); // cache.loadMesh("cube", cubeMat,
		cube.createDrawBuffer();
		cache.addMesh(cube);
		defaultCube = scene.addEntity("defaultCube", new MeshComponent(cube), new Transform3DComponent());

		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		scene.addEntity("grid_xyz", new GizmoComponent(axis));

		Gizmo cone = ObjLoader.loadGizmo("cone", "./resources/models/gizmos/Ycone.obj");
		cache.addGizmo(cone);
		scene.addEntity("cone", new GizmoComponent(cone), new Transform3DComponent());

		Gizmo Xhill = ObjLoader.loadGizmo("cone", "./resources/models/gizmos/Xhill.obj");
		cache.addGizmo(Xhill);
		scene.addEntity("Xhill", new GizmoComponent(Xhill), new Transform3DComponent());

		Texture txt1 = cache.loadSingleTexture("txt1", "./resources/textures/fonts/font1row.png", TextureFilter.NEAREST, TextureType.TXT2D);
		TextMaterial textMaterial = (TextMaterial) cache.loadMaterial(TextShader.TextMaterial.class, txt1);
		sm.monitorShader(textMaterial.getRenderShader());
		debugInfo = new TextEmitter("debugFps", textMaterial, 32, "FPS: ", new Vector2f(0.1f));
		debugInfo.updateText();
		cache.addTextEmitter(debugInfo);

		debugFps = scene.addEntity("debugFps", new TextEmitterComponent(debugInfo), new Transform3DComponent(new Vector3f(1f, 1f, 0), new Quaternionf().rotateZ((float) Math.toRadians(180f))));

		cache.addRenderer(new Scene3DRenderer());
		cache.addRenderer(new Scene2DRenderer());
		cache.addRenderer(new MeshRenderer());
		cache.addRenderer(new InstanceEmitterRenderer());
		cache.addRenderer(new TextEmitterRenderer());
		cache.addRenderer(new GizmoRenderer());

		this.backgroundMaterial = (FillMaterial) cache.loadMaterial(FillShader.FillMaterial.class);
		this.backgroundMaterialInterpolation = new CallbackValueInterpolation<FillMaterial, Vector4f>(this.backgroundMaterial, new Vector4f(0, 0.1f, 0, 1), new Vector4f(0.1f, 0.5f, 1, 1), Interpolators.BOUNCE_IN_OUT) {
			@Override
			public Vector4f evaluate(float progress) {
				return new Vector4f(this.start).lerp(this.end, progress);
			}

			@Override
			public void callback(FillMaterial object, Vector4f value) {
				object.setColor(value);
			}
		};
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", this.backgroundMaterial);
		cache.addRenderLayer(genLayer);

		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", this.scene);
		cache.addRenderLayer(sceneRender);

		SceneRenderLayer uiRender = new SceneRenderLayer("ui", this.ui);
		cache.addRenderLayer(uiRender);

		BoxBlurMaterial boxBlurMaterial = (BoxBlurMaterial) cache.loadMaterial(BoxBlurShader.BoxBlurMaterial.class);
		sm.monitorShader(boxBlurMaterial.getRenderShader());
		// cache.addRenderShader(boxBlurMaterial.getRenderShader());
		PassRenderLayer boxBlurPass = new PassRenderLayer("boxBlurPass", boxBlurMaterial);
		cache.addRenderLayer(boxBlurPass);

		this.compositor = new Compositor();
		this.compositor.addRenderLayer(0, genLayer);
		this.compositor.addRenderLayer(1, sceneRender);
		// this.compositor.addRenderLayer(1, uiRender);
		// this.compositor.addPassLayer(0, boxBlurPass);

		camera = (Camera3D) this.scene.getCamera();
		camera.getProjection().setPerspective(true);
		camera.getProjection().setFov((float) Math.toRadians(70));
		// camera.getProjection().update();

		cameraUi = (Camera3D) this.ui.getCamera();
		cameraUi.setProjection(new Projection(0.01f, 100f, -0.5f, 0.5f, 0.5f, -0.5f));
		cameraUi.setPosition(new Vector3f(0, 0, -1f));
		cameraUi.updateMatrix();

		this.ui.getCamera().getProjection().update(1920, 1080);
		((Camera3D) this.scene.getCamera()).lookAt(new Vector3f(-5, 0, 0), new Vector3f(0, 0, 0));
		this.scene.getCamera().getProjection().setPerspective(true);
		engine.getWindow().onResize((w, h) -> {
			System.out.println("resize update: " + w + "x" + h);
			this.scene.getCamera().getProjection().update(w, h);
			// this.ui.getCamera().getProjection().update(w, h);
		});
		engine.getWindow().setBackground(new Vector4f(0.1f));

		cache.dump(System.out);

		worker = new NextTaskWorker("worker", 2);

		NextTask<Void, Void, Void> nt = new NextTask<>(GameEngine.QUEUE_RENDER, -1, super.getTaskEnvironnment(), worker);
		nt.exec((Void s) -> {
			System.err.println("SYSOUT Thread exec: " + Thread.currentThread().getName());
			return null;
		}).then((Void s) -> {
			System.err.println("SYSOUT Thread callback: " + Thread.currentThread().getName());
			worker.block();
			System.err.println("SYSOUT Thread callback: blocked " + Thread.currentThread().getName() + " " + worker.isBlocking());
			try {
				worker.shutdown();
				System.err.println("SYSOUT Thread callback: shutdown " + Thread.currentThread().getName() + " " + worker.isActive());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return null;
		}).push();

		worker.closeInput();

	}

	@Override
	public void updateInit() {
		cache.loadSound("bz", "./resources/audio/subnautica_bz_stranger_pings.ogg")
				.setLooping(true)
				.setRolloffFactor(2)
				.play();
		cache.loadSound("buzz", "./resources/audio/wrong_buzz.ogg", false)
				.setLooping(true)
				.setReferenceDistance(3f)
				.setMaxDistance(10)
				//.setPitch(1.5f)
				.setRolloffFactor(1f)
				.setVolume(0.1f)
				.play();
		
		
		audio.setVolume(0.5f);
		audio.setVelocity(GameEngine.ZERO);
		audio.setOrientation(new Vector3f(0, 0, 1), GameEngine.UP);
		
		defaultCube.addComponent(new Sound3DComponent(cache.getSound("buzz")));
		audio.setDistanceModel(AL11.AL_INVERSE_DISTANCE);
	}

	private boolean previousF = false;
	private float gx = 0;

	@Override
	public void input(float dTime) {
		gx += 0.01f;
		
		// System.out.println(camera.getPosition());
		camera.getPosition().add((window.isKeyPressed(GLFW.GLFW_KEY_Q) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_D) ? 0.1f : 0), (window.isKeyPressed(GLFW.GLFW_KEY_R) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_F) ? 0.1f : 0),
				(window.isKeyPressed(GLFW.GLFW_KEY_Z) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_S) ? 0.1f : 0));
		camera.updateMatrix();

		//audio.setPosition(camera.getPosition());
		
		if (window.isKeyPressed(GLFW.GLFW_KEY_T) && !previousF) {
			previousF = true;
			createTask(GameEngine.QUEUE_RENDER).exec((s) -> {
				System.err.println("M Thread exec: " + Thread.currentThread().getName());
				Framebuffer fb = compositor.getFramebuffer();
				fb.unbind();
				SingleTexture color0 = (SingleTexture) fb.getAttachmedTexture(FrameBufferAttachment.COLOR_FIRST, 0);
				MemImage img = color0.getStoredImage();
				boolean success = FileUtils.STBISaveIncremental("./logs/screenshot.png", img);
				img.free();
				return success ? 1 : 0;
			}).then((s) -> {
				System.err.println("M Thread then: " + Thread.currentThread().getName());
				previousF = false;
				return 1;
			}).push();
		}

		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {

			System.out.println("click");

			// audioBridge.replay(cache.getSound("buzz"));
			cache.getSound("buzz").replay();


			int[] viewport = new int[4];
			createTask(GameEngine.QUEUE_RENDER).exec((s) -> {
				GL41.glGetIntegerv(GL41.GL_VIEWPORT, viewport);
				PDRUtils.checkGlError("GetIntegerV(GL_VIEWPORT)");

				return 1;
			}).then((s) -> {
				if ((int) s == 0)
					return 0;

				Ray ray = camera.projectRay(window.getMousePos(), viewport);

				Vector3f direction = ray.getDir().get(new Vector3f());
				direction.normalize();
				Quaternionf eulerQuaternion = new Quaternionf().rotationTo(new Vector3f(1, 0, 0), direction);

				//System.err.println(ray);

				/*
				 * rayEntity.getComponent(Transform3DComponent.class).getTransform()
				 * .setTranslation(ray.getOrigin()) //.setScale(ray.getLength())
				 * .setRotation(eulerQuaternion) .updateMatrix();
				 */

				//System.err.println(camera.getPosition());
				//System.err.println(ray.getOrigin() + " -> " + ray.getDir());

				Vector3f pos = ((Camera3D) scene.getCamera()).projectPlane(ray, GameEngine.FORWARD, GameEngine.RIGHT);

				//System.err.println(pos);

				defaultCube.getComponent(Transform3DComponent.class).getTransform().setTranslation(pos).updateMatrix();

				defaultCube.getComponent(Sound3DComponent.class).update();
				
				return 1;
			}).push();
		}
		
		defaultCube.getComponent(Transform3DComponent.class).getTransform().getTranslation().set(Math.sin(gx), 0, Math.cos(gx));
		defaultCube.getComponent(Transform3DComponent.class).getTransform().updateMatrix();
		
		cache.getSound("buzz").setPosition(defaultCube.getComponent(Transform3DComponent.class).getTransform().getTranslation());
	}

	@Override
	public void update(float dTime) {
		backgroundMaterialInterpolation.add(0.01f).mod();
		// defaultCube.getComponent(Transform3DComponent.class).getTransform().rotate(0.1f,
		// -0.1f, 0.05f).updateMatrix();
	}

	@Override
	public void render(float dTime) {
		debugInfo.setText("FPS: " + PDRUtils.round(engine.getCurrentFps(), 2) + "\nNL");
		debugInfo.setBoxed(true);
		debugInfo.setBoxSize(new Vector2f(1 + (float) Math.sin(backgroundMaterialInterpolation.getProgress()), 1));
		debugInfo.updateText();

		compositor.render(cache, engine);

		//System.err.println("managing file events");
		sm.manageEvents();
		// GL40.glClear(GL40.GL_DEPTH_BUFFER_BIT | GL40.GL_COLOR_BUFFER_BIT);
		// ((Scene3DRenderer) cache.getRenderer(Scene3D.NAME)).render(cache, engine,
		// scene);
	}
}
