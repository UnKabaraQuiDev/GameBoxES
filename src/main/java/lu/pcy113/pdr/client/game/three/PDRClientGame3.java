package lu.pcy113.pdr.client.game.three;

import java.util.Arrays;
import java.util.HashMap;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pclib.Pair;
import lu.pcy113.pdr.client.game.three.BoxBlurShader.BoxBlurMaterial;
import lu.pcy113.pdr.client.game.three.FillShader.FillMaterial;
import lu.pcy113.pdr.client.game.three.SlotInstanceShader.SlotInstanceMaterial;
import lu.pcy113.pdr.client.game.three.SlotShader.SlotMaterial;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
import lu.pcy113.pdr.engine.anim.Vec4fCallbackValueInterpolation;
import lu.pcy113.pdr.engine.anim.skeletal.Animation;
import lu.pcy113.pdr.engine.anim.skeletal.ArmatureAnimation;
import lu.pcy113.pdr.engine.audio.AudioMaster;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.geom.utils.ColladaeLoader;
import lu.pcy113.pdr.engine.geom.utils.ObjLoader;
import lu.pcy113.pdr.engine.graph.composition.Compositor;
import lu.pcy113.pdr.engine.graph.composition.GenerateRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;
import lu.pcy113.pdr.engine.graph.composition.SceneRenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.graph.material.text.TextShader.TextMaterial;
import lu.pcy113.pdr.engine.graph.render.InstanceEmitterRenderer;
import lu.pcy113.pdr.engine.graph.render.MeshRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene2DRenderer;
import lu.pcy113.pdr.engine.graph.render.Scene3DRenderer;
import lu.pcy113.pdr.engine.graph.render.TextEmitterRenderer;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.graph.texture.CubemapTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.GameLogic;
import lu.pcy113.pdr.engine.impl.nexttask.NextTask;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.ArmatureAnimationComponent;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.RenderComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;
import lu.pcy113.pdr.engine.scene.camera.Projection;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.consts.TextureFilter;
import lu.pcy113.pdr.engine.utils.consts.TextureType;
import lu.pcy113.pdr.engine.utils.interpolation.Interpolators;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class PDRClientGame3 extends GameLogic {

	SlotMaterial slotMaterial;
	FillMaterial backgroundMaterial;
	SlotInstanceMaterial slotInstMaterial;

	CallbackValueInterpolation<FillMaterial, Vector4f> backgroundMaterialInterpolation;
	Vec4fCallbackValueInterpolation<TextMaterial> textMaterialInterpolation;

	InstanceEmitter slotInstancer;

	Entity slotEntity, slotEntityUi, textEntity;

	Camera3D camera;
	Camera3D cameraUi;
	Scene3D scene;
	Scene2D ui;

	Compositor compositor;

	TextEmitter debugInfo;
	JoystickState leftJoystick, rightJoystick;
	FloatButtonState leftZButton, rightZButton;
	BooleanButtonState leftButton, rightButton;
	FourButtonState dirButtons, xyabButtons;

	Mesh skeleMesh;
	Entity skeleEntity;

	AudioMaster audio;

	@Override
	public void init(GameEngine e) {
		cache = e.getCache();
		window = e.getWindow();
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.wireframeColor = new Vector4f(1f, 0.2f, 0.2f, 0.2f);
		GameEngine.DEBUG.gizmos = false;

		Texture slotTexture = cache.loadSingleTexture("single_slot", "./resources/textures/ui/single_slot.png");
		slotMaterial = (SlotMaterial) cache.loadMaterial(SlotShader.SlotMaterial.class, slotTexture);
		Mesh slotMesh = ObjLoader.loadMesh("slot", slotMaterial, "./resources/models/plane.obj");
		this.cache.addMesh(slotMesh);

		FloatAttribArray faa = new FloatAttribArray("hover", 7, 1, new float[3 * 5], false, 1);
		slotInstMaterial = (SlotInstanceMaterial) cache.loadMaterial(SlotInstanceShader.SlotInstanceMaterial.class, slotTexture);
		Mesh slotInstMesh = ObjLoader.loadMesh("slotInst", slotInstMaterial, "./resources/models/plane.obj");
		this.cache.addMesh(slotInstMesh);
		slotInstancer = cache.loadInstanceEmitter("slotEmitter", slotInstMesh, 3 * 5, new Transform3D(), faa);
		slotInstancer.update((inst) -> {
			((Transform3D) inst.getTransform()).setTranslation(new Vector3f(inst.getIndex() % 3 - 1f, (inst.getIndex() / 3) - 2f, 0f).mul(1.1f));
			System.out.println("instance: " + inst.getIndex() + " > " + ((Transform3D) inst.getTransform()).getTranslation());
			((Transform3D) inst.getTransform()).updateMatrix();
		});
		slotEntityUi = new Entity(new Transform3DComponent(new Transform3D().setTranslation(new Vector3f(4, 0, 0))), new InstanceEmitterComponent(slotInstancer));
		// slotEntityUi.getComponent(Transform3DComponent.class).getTransform().getRotation().rotateAxis((float)
		// Math.toRadians(90), GameEngine.UP);
		slotEntityUi.getComponent(Transform3DComponent.class).getTransform().updateMatrix();

		Texture txt1 = cache.loadSingleTexture("txt1", "./resources/textures/fonts/font1row.png", TextureFilter.NEAREST, TextureType.TXT2D);
		TextMaterial textMaterial = (TextMaterial) cache.loadMaterial(TextShader.TextMaterial.class, txt1);
		debugInfo = new TextEmitter("debug_infos", textMaterial, 20, "FPS: ", new Vector2f(0.1f));
		debugInfo.updateText();
		cache.addTextEmitter(debugInfo);

		ui = new Scene2D("ui", Camera.orthographicCamera3D());
		ui.addEntity("slotUi", slotEntityUi);
		ui.addEntity("debug_info", new Entity(new Transform3DComponent(new Vector3f(-6.1f, -3.3f, 0), new Quaternionf(), new Vector3f(1)), new TextEmitterComponent(debugInfo)));

		ui.addEntity("left_joy", leftJoystick = new JoystickState(cache, new Vector3f(-5.5f, -2f, 0)));
		leftJoystick.setColor(new Vector4f(0, 1, 0, 1));
		ui.addEntity("right_joy", rightJoystick = new JoystickState(cache, new Vector3f(-4.5f, -2f, 0)));
		rightJoystick.setColor(new Vector4f(1, 0, 0, 1));
		/*
		 * ui.addEntity("dir_joy", dirJoystick = new JoystickState(cache, new
		 * Vector3f(-5.5f, -1f, 0))); dirJoystick.setColor(new Vector4f(1, 0, 1, 1));
		 */

		ui.addEntity("left_float", leftZButton = new FloatButtonState(cache, new Vector3f(-6.2f, -2f, 0)));
		leftZButton.setColor(new Vector4f(0, 1, 1, 1));
		ui.addEntity("right_float", rightZButton = new FloatButtonState(cache, new Vector3f(-3.8f, -2f, 0)));
		rightZButton.setColor(new Vector4f(1, 0, 1, 1));

		ui.addEntity("dir_btn", dirButtons = new FourButtonState(cache, new Vector3f(-5.5f, -1f, 0)));
		dirButtons.setButtons(new Vector4f(0));
		ui.addEntity("xyab_btn", xyabButtons = new FourButtonState(cache, new Vector3f(-4.5f, -1f, 0)));
		xyabButtons.setButtons(new Vector4f(0));

		ui.addEntity("left_bumper", leftButton = new BooleanButtonState(cache, new Vector3f(-5.5f, -2.6f, 0)));
		leftButton.setColor(new Vector4f(0, 1, 1, 1));
		ui.addEntity("right_bumper", rightButton = new BooleanButtonState(cache, new Vector3f(-4.5f, -2.6f, 0)));
		rightButton.setColor(new Vector4f(1, 0, 1, 1));

		scene = new Scene3D("main");
		slotEntity = new Entity(new Transform3DComponent(), new MeshComponent(slotMesh), new RenderComponent(0));
		scene.addEntity("slot", this.slotEntity).setActive(true);

		TextEmitter textEmitter = new TextEmitter("text", textMaterial, 32, "text\nnew line\n\ttab", new Vector2f(0.1f));
		textEmitter.updateText();
		cache.addTextEmitter(textEmitter);

		textEntity = new Entity(new Transform3DComponent(), new TextEmitterComponent(textEmitter), new RenderComponent(1));
		scene.addEntity("text", textEntity);

		this.audio = engine.getAudioMaster();

		this.cache.addRenderer(new Scene3DRenderer());
		this.cache.addRenderer(new Scene2DRenderer());
		this.cache.addRenderer(new MeshRenderer());
		this.cache.addRenderer(new InstanceEmitterRenderer());
		this.cache.addRenderer(new TextEmitterRenderer());

		this.backgroundMaterial = (FillMaterial) cache.loadMaterial(FillShader.FillMaterial.class);
		GenerateRenderLayer genLayer = new GenerateRenderLayer("gen", this.backgroundMaterial);
		this.cache.addRenderLayer(genLayer);

		SceneRenderLayer sceneRender = new SceneRenderLayer("scene", this.scene);
		this.cache.addRenderLayer(sceneRender);

		SceneRenderLayer uiRender = new SceneRenderLayer("ui", this.ui);
		this.cache.addRenderLayer(uiRender);

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
		camera.getProjection().setFov((float) Math.toRadians(60));

		cameraUi = (Camera3D) this.ui.getCamera();
		cameraUi.setProjection(new Projection(0.01f, 100f, -0.5f, 0.5f, 0.5f, -0.5f));
		cameraUi.setPosition(new Vector3f(0, 0, -1f));
		cameraUi.updateMatrix();

		this.ui.getCamera().getProjection().update(1920, 1080);
		this.scene.getCamera().getProjection().setPerspective(true);
		this.engine.getWindow().onResize((w, h) -> {
			System.out.println("resize update: " + w + "x" + h);
			this.scene.getCamera().getProjection().update(w, h);
			// this.ui.getCamera().getProjection().update(w, h);
		});
		this.engine.getWindow().setBackground(new Vector4f(0.1f));

		this.backgroundMaterialInterpolation = new CallbackValueInterpolation<FillMaterial, Vector4f>(this.backgroundMaterial, new Vector4f(0, 0.1f, 0, 1), new Vector4f(0.1f, 0, 1, 1), Interpolators.BOUNCE_IN_OUT) {
			@Override
			public Vector4f evaluate(float progress) {
				return new Vector4f(this.start).lerp(this.end, progress);
			}

			@Override
			public void callback(FillMaterial object, Vector4f value) {
				object.setColor(value);
			}
		};

		this.textMaterialInterpolation = new Vec4fCallbackValueInterpolation<TextMaterial>(textMaterial, new Vector4f(1, 0.1f, 0, 1), new Vector4f(0.1f, 1, 1, 1), Interpolators.QUINT_IN_OUT) {
			@Override
			public void callback(TextMaterial object, Vector4f value) {
				object.setFgColor(value);
			}
		};

		/* CUBE MAP */
		CubemapTexture cmtxt = cache.loadCubemapTexture("skybox", "./resources/textures/skybox/.jpg");
		final String skyboxUniform = "skybox";
		RenderShader shader = new RenderShader("skybox", AbstractShaderPart.load("./resources/shaders/plain.vert"), AbstractShaderPart.load("./resources/shaders/skybox/skybox.frag")) {
			@Override
			public void createUniforms() {
				createUniform(RenderShader.PROJECTION_MATRIX);
				createUniform(RenderShader.VIEW_MATRIX);
				createUniform(RenderShader.TRANSFORMATION_MATRIX);
				createUniform(skyboxUniform);
			}
		};
		cache.addRenderShader(shader);
		TextureMaterial material = new TextureMaterial("skybox", shader, new HashMap<String, Texture>(1) {
			{
				put("skybox", cmtxt);
			}
		});
		cache.addMaterial(material);
		Mesh cube = cache.loadMesh("skybox", material, "./resources/models/cube2.obj");

		scene.addEntity("skybox", new Entity(new Transform3DComponent(), new MeshComponent(cube))).setActive(true).getComponent(Transform3DComponent.class).getTransform().scaleMul(new Vector3f(10)).updateMatrix();

		RenderShader skeleShader = new RenderShader("skeleShader", AbstractShaderPart.load("./resources/shaders/animation/skeletal.vert"), AbstractShaderPart.load("./resources/shaders/animation/skeletal_diffuse.frag")) {
			@Override
			public void createUniforms() {
				super.createSceneUniforms();

				createUniform(RenderShader.PROJECTION_MATRIX);
				createUniform(RenderShader.VIEW_MATRIX);
				createUniform(RenderShader.TRANSFORMATION_MATRIX);

				createUniform(ArmatureAnimation.BONE_UNIFORM);
				createUniform("diffuseColor");
			}
		};
		cache.addRenderShader(skeleShader);
		Material skeleMat = new Material("skeleMat", skeleShader);
		cache.addRenderShader(skeleShader);

		// Mesh pdrasset1 = ObjLoader.loadMesh("pdrasset1", skeleMat,
		// "./resources/models/scene_pdrassets_1.obj");
		// cache.addMesh(pdrasset1);
		// scene.addEntity("pdrasset", new MeshComponent(pdrasset1), new
		// Transform3DComponent());

		Mesh model = ObjLoader.loadMesh("model", slotMaterial, "./resources/models/anims/model.obj");
		cache.addMesh(model);
		scene.addEntity("model", new MeshComponent(model), new Transform3DComponent());

		Pair<Mesh, ArmatureAnimation> pmaa = ColladaeLoader.loadMeshArmature("skeleMesh", skeleMat, "./resources/models/anims/model.dae");
		skeleMesh = pmaa.getKey();
		System.err.println("start data: " + skeleMesh.getVertices().getName() + " = " + Arrays.toString(((Vec3fAttribArray) skeleMesh.getVertices()).getData()));
		System.err.println("start data: " + skeleMesh.getIndices().getName() + " = " + Arrays.toString(((UIntAttribArray) skeleMesh.getIndices()).getData()));
		System.err.println("start data: " + skeleMesh.getAttribs()[2].getName() + " = " + Arrays.toString(((Vec3fAttribArray) skeleMesh.getAttribs()[2]).getData()));
		System.err.println("start data: " + skeleMesh.getAttribs()[3].getName() + " = " + Arrays.toString(((Vec3fAttribArray) skeleMesh.getAttribs()[3]).getData()));
		ArmatureAnimation armatureAnimation = pmaa.getValue();
		System.err.println(armatureAnimation.getRootBone().toString(0));
		cache.addMesh(skeleMesh);
		Animation animation = ColladaeLoader.loadAnimation("./resources/models/anims/model.dae");
		armatureAnimation.getAnimator().doAnimation(animation);
		armatureAnimation.getAnimator().update(0.1f);

		skeleEntity = scene.addEntity("skelet", new Entity(new MeshComponent(skeleMesh), new Transform3DComponent(), new ArmatureAnimationComponent(armatureAnimation))).setActive(true)
		/*
		 * .getComponent(Transform3DComponent.class).getTransform().scaleAdd(2, 2,
		 * 2).updateMatrix()
		 */;

		/* DUMP */
		cache.dump(System.out);
	}

	int col = 0, row = 0;
	float threshold = 0.1f;

	@Override
	public void input(float dTime) {
		camera.getPosition().add((window.isKeyPressed(GLFW.GLFW_KEY_Q) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_D) ? 0.1f : 0), (window.isKeyPressed(GLFW.GLFW_KEY_R) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_F) ? 0.1f : 0),
				(window.isKeyPressed(GLFW.GLFW_KEY_Z) ? 0.1f : 0) - (window.isKeyPressed(GLFW.GLFW_KEY_S) ? 0.1f : 0));
		camera.updateMatrix();

		if (window.isJoystickPresent()) {
			if (!window.updateGamepad(0))
				return;

			GLFWGamepadState gps = window.getGamepad();

			float ax = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X), threshold);
			float ay = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y), threshold);
			float abtn = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB);

			leftJoystick.setPosition(new Vector2f(ax, ay));
			leftJoystick.setButton(abtn);
			leftJoystick.setThreshold(threshold);

			float bx = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X), threshold);
			float by = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y), threshold);
			float bbtn = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB);

			rightJoystick.setPosition(new Vector2f(bx, by));
			rightJoystick.setButton(bbtn);
			rightJoystick.setThreshold(threshold);

			float lzb = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER), threshold);
			float rzb = PDRUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER), threshold);

			leftZButton.setValue((float) (lzb / 2 + 0.5));
			rightZButton.setValue((float) (rzb / 2 + 0.5));

			float lb = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER);
			float rb = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER);

			leftButton.setValue(lb);
			rightButton.setValue(rb);

			float btn_y = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_Y);
			float btn_b = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_B);
			float btn_a = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_A);
			float btn_x = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_X);

			xyabButtons.setButtons(new Vector4f(btn_b, btn_a, btn_x, btn_y));

			float dir_up = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP);
			float dir_down = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN);
			float dir_left = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT);
			float dir_right = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT);

			dirButtons.setButtons(new Vector4f(dir_right, dir_down, dir_left, dir_up));
		}
	}

	float GX = 0f;
	float hover = 0f;
	long frame = 0;

	@Override
	public void update(float dTime) {
		GX = (GX + 0.01f) % 1;

		this.backgroundMaterialInterpolation.set(this.GX);
		this.textMaterialInterpolation.set(this.GX);

		slotEntity.getComponent(Transform3DComponent.class).getTransform().getRotation().rotateXYZ(0.01f, 0.01f, 0.01f);
		slotEntity.getComponent(Transform3DComponent.class).getTransform().updateMatrix();

		slotEntityUi.getComponent(Transform3DComponent.class).getTransform().updateMatrix();

		textEntity.getComponent(Transform3DComponent.class).getTransform().getRotation().rotateXYZ(0, 0, -0.01f);
		textEntity.getComponent(Transform3DComponent.class).getTransform().updateMatrix();

		if ((frame %= 120) > 60) {
			hover -= 0.1f;
		} else {
			hover += 0.2f;
		}
		hover = org.joml.Math.clamp(0, 1, hover);

		NextTask nt = createTask(GameEngine.QUEUE_RENDER);
		nt.exec((status) -> {
			slotInstancer.update((inst) -> {
				if (inst.getIndex() == 0) {
					inst.getBuffers()[0] = hover;
				}
				debugInfo.setText("FPS: " + PDRUtils.round(engine.getCurrentFps(), 3) + "\n").updateText();
				textEntity.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText("updated... " + debugInfo.getText()).updateText();
			});
			return status;
		}).push();
		// pushTask(nt);

		skeleEntity.getComponent(Transform3DComponent.class).getTransform().setScale(new Vector3f(GX * 0.1f)).updateMatrix();

		/*
		 * long time = System.nanoTime(); boolean cc = waitForFrameEnd();
		 * System.err.println(Thread.currentThread().getName()+"> WAITED: "+(System.
		 * nanoTime()-time)+"ns for "+cc);
		 */

		// System.out.println("GX: "+GX+" int: "+backgroundMaterial.getColor());
	}

	@Override
	public void render(float dTime) {
		compositor.render(cache, engine);

		/*
		 * TextureRenderer txtRenderer = new TextureRenderer("txtRenderer", cache, new
		 * Vector2i(800, 800), 1); txtRenderer.bind();
		 * 
		 * ((Scene3DRenderer) cache.getRenderer(Scene3D.NAME)).render(cache, engine,
		 * scene);
		 * 
		 * txtRenderer.unbind(); txtRenderer.bind(GL40.GL_READ_FRAMEBUFFER);
		 * 
		 * GL40.glBlitFramebuffer( 0, 0, 800, 800, 0, 0, engine.getWindow().getWidth(),
		 * engine.getWindow().getHeight(), GL40.GL_COLOR_BUFFER_BIT,GL40.GL_NEAREST);
		 * 
		 * txtRenderer.cleanup();
		 */

		frame++;
	}

}
