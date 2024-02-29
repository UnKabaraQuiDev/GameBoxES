package lu.pcy113.pdr.client.game.four;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL41;

import lu.pcy113.pdr.client.game.four.PlainShader.PlainMaterial;
import lu.pcy113.pdr.client.game.three.BoxBlurShader;
import lu.pcy113.pdr.client.game.three.BoxBlurShader.BoxBlurMaterial;
import lu.pcy113.pdr.client.game.three.FillShader;
import lu.pcy113.pdr.client.game.three.FillShader.FillMaterial;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.utils.ObjLoader;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.Framebuffer;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.render.GizmoRenderer;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene2DRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.render.TextEmitterRenderer;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.scene.camera.Projection;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.MemImage;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.Ray;
import lu.pcy113.pdr.engine.utils.consts.FrameBufferAttachment;
import lu.pcy113.pdr.engine.utils.interpolation.Interpolators;

public class PDRClientGame4 extends GameLogic {

	CallbackValueInterpolation<FillMaterial, Vector4f> backgroundMaterialInterpolation;
	FillMaterial backgroundMaterial;

	Camera3D camera, cameraUi;
	Scene3D scene;
	Scene2D ui;

	Entity defaultCube;

	Compositor compositor;

	@Override
	public void init(GameEngine e) {
		System.err.println("Cache: " + cache);

		GameEngine.DEBUG.wireframe = true;
		GameEngine.DEBUG.wireframeColor = new Vector4f(1f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = true;

		ui = new Scene2D("ui", Camera.orthographicCamera3D());
		scene = new Scene3D("main");

		PlainMaterial cubeMat = (PlainMaterial) cache.loadMaterial(PlainShader.PlainMaterial.class);
		cubeMat.setColor(new Vector4f(1));
		Mesh cube = Mesh.newCube("cube", cubeMat, new Vector3f(1)); // cache.loadMesh("cube", cubeMat,
																	// "./resources/models/cube2.obj");
		cache.addMesh(cube);
		defaultCube = scene.addEntity("defaultCube", new MeshComponent(cube), new Transform3DComponent());

		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		scene.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent());

		Gizmo cone = ObjLoader.loadGizmo("cone", "./resources/models/gizmos/Ycone.obj");
		cache.addGizmo(cone);
		scene.addEntity("cone", new GizmoComponent(cone), new Transform3DComponent());

		cache.addRenderer(new Scene3DRenderer());
		cache.addRenderer(new Scene2DRenderer());
		cache.addRenderer(new MeshRenderer());
		cache.addRenderer(new InstanceEmitterRenderer());
		cache.addRenderer(new TextEmitterRenderer());
		cache.addRenderer(new GizmoRenderer());
		/*
		 * TODO: Compute shaders TODO: Compositor + Compute shader
		 */

		this.backgroundMaterial = (FillMaterial) cache.loadMaterial(FillShader.FillMaterial.class);
		this.backgroundMaterialInterpolation = new CallbackValueInterpolation<FillMaterial, Vector4f>(
				this.backgroundMaterial, new Vector4f(0, 0.1f, 0, 1), new Vector4f(0.1f, 0.5f, 1, 1),
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
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", this.backgroundMaterial);
		cache.addRenderLayer(genLayer);

		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", this.scene);
		cache.addRenderLayer(sceneRender);

		SceneRenderLayer uiRender = new SceneRenderLayer("ui", this.ui);
		cache.addRenderLayer(uiRender);

		BoxBlurMaterial boxBlurMaterial = (BoxBlurMaterial) cache.loadMaterial(BoxBlurShader.BoxBlurMaterial.class);
		// cache.addRenderShader(boxBlurMaterial.getRenderShader());
		PassRenderLayer boxBlurPass = new PassRenderLayer("boxBlurPass", boxBlurMaterial);
		cache.addRenderLayer(boxBlurPass);

		this.compositor = new Compositor();
		this.compositor.addRenderLayer(0, genLayer);
		this.compositor.addRenderLayer(1, sceneRender);
		this.compositor.addRenderLayer(2, uiRender);
		// this.compositor.addPassLayer(0, boxBlurPass);

		camera = (Camera3D) this.scene.getCamera();
		camera.getProjection().setPerspective(true);
		camera.getProjection().setFov((float) Math.toRadians(70));

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

		// exporting meshes as bin format
		/*
		 * CodecManager cm = CodecManager.base(); cm.register(new MeshEncoder(), (short)
		 * 0x22); cm.register(new AttribArrayEncoder(), (short) 0x23); cm.register(new
		 * UIntAttribArrayEncoder(), (short) 0x24);
		 * 
		 * System.out.println(ArrayUtils.byteBufferToHexString(cm.encode(true, cube)));
		 */
	}

	private boolean previousF = false;

	@Override
	public void input(float dTime) {
		// System.out.println(camera.getPosition());
		camera.getPosition().add(
				(window.isKeyPressed(GLFW.GLFW_KEY_Q) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_D) ? 0.1f : 0),
				(window.isKeyPressed(GLFW.GLFW_KEY_R) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_F) ? 0.1f : 0),
				(window.isKeyPressed(GLFW.GLFW_KEY_Z) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_S) ? 0.1f : 0));
		camera.updateMatrix();

		if (window.isKeyPressed(GLFW.GLFW_KEY_M) && !previousF) {
			previousF = true;
			createTask(GameEngine.QUEUE_RENDER).exec((s) -> {
				System.err.println("Thread: " + Thread.currentThread().getName());
				Framebuffer fb = compositor.getFramebuffer();
				fb.unbind();
				SingleTexture color0 = (SingleTexture) fb.getAttachmedTexture(FrameBufferAttachment.COLOR_FIRST, 0);
				MemImage img = color0.getStoredImage();
				boolean success = FileUtils.STBISaveIncremental("./logs/screenshot.png", img);
				img.free();
				return success ? 1 : 0;
			}).then((s) -> {
				previousF = false;
				return 1;
			}).push();
		}

		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {

			int[] viewport = new int[4];
			createTask(GameEngine.QUEUE_RENDER).exec((s) -> {
				GL41.glGetIntegerv(GL41.GL_VIEWPORT, viewport);
				PDRUtils.checkGlError("GetIntegerV(GL_VIEWPORT)");

				return 1;
			}).then((s) -> {
				if (s == 0)
					return 0;

				Ray ray = camera.projectRay(window.getMousePos(), viewport);

				System.err.println(camera.getPosition());
				System.err.println(ray.getOrigin() + " : " + ray.getDir());

				Vector3f pos = ((Camera3D) scene.getCamera()).projectPlane(ray, GameEngine.RIGHT, GameEngine.UP);

				System.err.println(pos);

				defaultCube.getComponent(Transform3DComponent.class).getTransform().setTranslation(pos).updateMatrix();

				return 1;
			}).push();
		}
	}

	@Override
	public void update(float dTime) {
		backgroundMaterialInterpolation.add(0.01f).mod();
		defaultCube.getComponent(Transform3DComponent.class).getTransform().rotate(0.1f, -0.1f, 0.05f).updateMatrix();
	}

	@Override
	public void render(float dTime) {
		compositor.render(cache, engine);
		// GL40.glClear(GL40.GL_DEPTH_BUFFER_BIT | GL40.GL_COLOR_BUFFER_BIT);
		// ((Scene3DRenderer) cache.getRenderer(Scene3D.NAME)).render(cache, engine,
		// scene);
	}
}
