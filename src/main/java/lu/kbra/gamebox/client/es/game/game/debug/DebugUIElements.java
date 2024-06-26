package lu.kbra.gamebox.client.es.game.game.debug;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class DebugUIElements {

	GameEngine engine;
	
	Scene3D scene;

	JoystickState leftJoystick, rightJoystick;
	FloatButtonState leftZButton, rightZButton;
	BooleanButtonState leftButton, rightButton;
	FourButtonState dirButtons, xyabButtons;
	TextEmitter fpsDebug;

	public DebugUIElements(CacheManager cache, GameEngine engine, Scene3D scene, Vector3f pos, Quaternionf rot) {
		this.scene = scene;
		this.engine = engine;

		final Quaternionf axis = new Quaternionf(); //.rotateXYZ((float) Math.toRadians(180f), (float) Math.toRadians(0), (float) Math.toRadians(0));
		
		fpsDebug = new TextEmitter("fpsDebug",
				(TextMaterial) cache.loadOrGetMaterial(TextMaterial.NAME, TextShader.TextMaterial.class, cache.loadOrGetSingleTexture("text_30px", "./resources/textures/fonts/font1row.png", TextureFilter.NEAREST)),
				32, "FPS: ", new Vector2f(0.1f));
		fpsDebug.setAlignment(Alignment.TEXT_CENTER);
		fpsDebug.setBoxed(true);
		fpsDebug.updateText();
		cache.addTextEmitter(fpsDebug);
		Entity fpsDebugEntity = new Entity("fpsDebug", new TextEmitterComponent(fpsDebug), new Transform3DComponent(new Vector3f(1.3f, 0, 0).add(pos), axis));
		scene.addEntity("fpsDebug", fpsDebugEntity);
		
		this.leftJoystick = new JoystickState(cache, new Transform3D(new Vector3f(-0.5f, 0.5f, 0).add(pos), axis));
		this.rightJoystick = new JoystickState(cache, new Transform3D(new Vector3f(0.5f, 0.5f, 0).add(pos), axis));

		this.leftButton = new BooleanButtonState(cache, new Transform3D(new Vector3f(-0.5f, 1.1f, 0).add(pos), axis));
		this.rightButton = new BooleanButtonState(cache, new Transform3D(new Vector3f(0.5f, 1.1f, 0).add(pos), axis));

		this.dirButtons = new FourButtonState(cache, new Transform3D(new Vector3f(-0.5f, -0.5f, 0).add(pos), axis));
		this.xyabButtons = new FourButtonState(cache, new Transform3D(new Vector3f(0.5f, -0.5f, 0).add(pos), axis));

		this.leftZButton = new FloatButtonState(cache, new Transform3D(new Vector3f(-1f, 0.5f, 0).add(pos), axis));
		this.rightZButton = new FloatButtonState(cache, new Transform3D(new Vector3f(1f, 0.5f, 0).add(pos), axis));

		scene.addEntities(new String[] { "LJoy", "RJoy", "LBtn", "RBtn", "DirBtn", "XYABBtn", "LZBtn", "RZBtn" }, new Entity[] { leftJoystick, rightJoystick, leftButton, rightButton, dirButtons, xyabButtons, leftZButton, rightZButton });
	}

	public void update() {
		fpsDebug.setText("FPS: "+PDRUtils.round(engine.getCurrentFps(), 3)).updateText();
		
		Window window = engine.getWindow();
		
		if (window.isJoystickPresent()) {
			if (!window.updateGamepad(0))
				return;

			GLFWGamepadState gps = window.getGamepad();

			float ax = GlobalUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X));
			float ay = GlobalUtils.applyMinThreshold(-gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y));
			float abtn = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB);

			leftJoystick.setPosition(new Vector2f(ax, ay));
			leftJoystick.setButton(abtn);
			leftJoystick.setThreshold(GlobalUtils.joystickThreshold);

			float bx = GlobalUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X));
			float by = GlobalUtils.applyMinThreshold(-gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y));
			float bbtn = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB);
			
			rightJoystick.setPosition(new Vector2f(bx, by));
			rightJoystick.setButton(bbtn);
			rightJoystick.setThreshold(GlobalUtils.joystickThreshold);

			float lzb = GlobalUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER));
			float rzb = GlobalUtils.applyMinThreshold(gps.axes(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER));

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

			xyabButtons.setButtons(new Vector4f(btn_b, btn_y, btn_x, btn_a));

			float dir_up = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP);
			float dir_down = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN);
			float dir_left = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT);
			float dir_right = gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT);

			dirButtons.setButtons(new Vector4f(dir_right, dir_up, dir_left, dir_down));
		}
	}

}
