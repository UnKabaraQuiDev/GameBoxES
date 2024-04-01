package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UISceneStartMenuState extends UISceneState {

	private static final float THRESHOLD_LOW = 0.2f; // Example threshold values
	private static final float THRESHOLD_HIGH = 0.5f;

	private static final float X_POS_START = 0f;
	private static final float X_POS_END = -1f;

	private static final String TEXT_PLAY = "PLAY !", TEXT_OPTIONS = "OPTIONS", TEXT_QUIT = "QUIT";

	private Entity play, options, quit;
	private Entity mode1, mode2;

	public UISceneStartMenuState(UIScene3D scene) {
		super(scene);

		play = addText("playText", 10, TEXT_PLAY, new Vector3f(0, 0.8f, 0));
		options = addText("optionsText", 10, TEXT_OPTIONS, new Vector3f(0, 0, 0));
		quit = addText("quitText", 10, TEXT_QUIT, new Vector3f(0, -0.8f, 0));
	}

	private Entity addText(String name, int length, String txt, Vector3f pos) {
		TextEmitter text = new TextEmitter(name, (TextMaterial) cache.getMaterial(TextShader.TextMaterial.NAME), length, txt, new Vector2f(0.5f));
		text.setAlignment(Alignment.ABSOLUTE_RIGHT);
		text.createDrawBuffer();
		text.updateText();
		cache.addTextEmitter(text);
		return scene.addEntity(name, new Entity(new Transform3DComponent(pos), new TextEmitterComponent(text)));
	}

	int verticalIndex = 0;
	int horizontalIndex = 0;

	@Override
	public void input(float dTime) {
		if (!window.isJoystickPresent())
			return;

		float[] jsaxis = window.getJoystickAxis(0);
		int dir = MathUtils.greatestAbsIndex(jsaxis);
		switch (dir) {
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_X:
			if (Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X]) <= THRESHOLD_LOW) {
				// disable ui element
				placeElements(0);
			} else if (Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X]) >= THRESHOLD_HIGH) {
				// enable ui element
				placeElements(1);
			} else {
				placeElements((Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X]) - THRESHOLD_LOW) * (THRESHOLD_HIGH - THRESHOLD_LOW));
			}
			break;
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y:
			if (Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y]) <= THRESHOLD_LOW) {
				// disable ui element
			} else if (Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y]) >= THRESHOLD_HIGH) {
				// enable ui element

				verticalIndex += jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y] < 0 ? -1 : 1;
				verticalIndex = org.joml.Math.clamp(0, 2, verticalIndex);

				chooseElement();
			} else {
				placeElements((Math.abs(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y]) - THRESHOLD_LOW) * (THRESHOLD_HIGH - THRESHOLD_LOW));
			}
		}
	}

	private void chooseElement() {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER)
		.exec((t) -> {
			switch (verticalIndex) {
			case 0:
				play.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + TEXT_PLAY + "<").updateText();
				options.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_OPTIONS).updateText();
				quit.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_QUIT).updateText();
				break;
			case 1:
				play.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_PLAY).updateText();
				options.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + TEXT_OPTIONS + "<").updateText();
				quit.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_QUIT).updateText();
				break;
			case 2:
				play.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_PLAY).updateText();
				options.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXT_OPTIONS).updateText();
				quit.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + TEXT_QUIT + "<").updateText();
				break;
			}
			return null;
		}).push();
		
	}

	private void placeElements(float playMenu) {

	}

}
