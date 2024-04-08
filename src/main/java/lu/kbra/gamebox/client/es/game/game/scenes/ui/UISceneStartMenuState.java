package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pclib.Pair;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UISceneStartMenuState extends UISceneState {

	public static final Vector4f HIGHLIGHT_COLOR = new Vector4f(0.1f, 1f, 1f, 1f), IDLE_COLOR = new Vector4f(1);

	private static final float Y_POS_OFFSET = 0.1f;
	private static final float X_POS_START = 0f;
	private static final float X_POS_END = -5f;
	
	private static final float MAIN_MENU_TRANSITION_VALUE_INCREMENT = 0.3f;
	private static final int MAIN_MENU_TRANSITION_STATE_IN = 1;
	private static final int MAIN_MENU_TRANSITION_STATE_OFF = 0;
	private static final int MAIN_MENU_TRANSITION_STATE_OUT = -1;
	
	private static final int MENU_MAIN_MENU = 0;
	private static final int MENU_OPTIONS = 1;
	private static final int MENU_PLAY = 2;

	private static final String TEXT_PLAY = "PLAY !", TEXT_OPTIONS = "OPTIONS", TEXT_QUIT = "QUIT";
	private static final String[] TEXTS = { TEXT_PLAY, TEXT_OPTIONS, TEXT_QUIT };

	private Entity[] entities1;
	private Vector3f[] entities1pos;
	private Entity play, options, quit;
	private Entity mode1, mode2;

	private ControllerInputWatcher cic = new ControllerInputWatcher();

	public UISceneStartMenuState(UIScene3D scene) {
		super(scene);

		play = addText("playText", 10, TEXT_PLAY, new Vector3f(0, 0.8f, 0));
		options = addText("optionsText", 10, TEXT_OPTIONS, new Vector3f(0, 0, 0));
		quit = addText("quitText", 10, TEXT_QUIT, new Vector3f(0, -0.8f, 0));

		entities1 = new Entity[] { play, options, quit };
		entities1pos = new Vector3f[] { new Vector3f(0, 0.8f, 0), new Vector3f(0, 0, 0), new Vector3f(0, -0.8f, 0) };

		chooseMainMenuElement();
	}

	private Entity addText(String name, int length, String txt, Vector3f pos) {
		TextMaterial mat = new TextMaterial("TextMaterial-" + GameBoxES.TEXT_TEXTURE + "-" + name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GameBoxES.TEXT_TEXTURE));
		cache.addMaterial(mat);
		TextEmitter text = new TextEmitter(name, mat, length, txt, new Vector2f(0.35f, 0.5f));
		text.setAlignment(Alignment.ABSOLUTE_RIGHT);
		text.createDrawBuffer();
		text.updateText();
		cache.addTextEmitter(text);
		return scene.addEntity(name, new Entity(new Transform3DComponent(pos), new TextEmitterComponent(text)));
	}

	int verticalIndex = 0;
	int menuIndex = MENU_MAIN_MENU;

	@Override
	public void input(float dTime) {
		if (!window.isJoystickPresent())
			return;
		if (!window.updateGamepad(0))
			return;

		/*
		 * t += 0.025f; System.out.println("current: "+Math.sin(t)); for(Entity e :
		 * entities1) { ((TextMaterial)
		 * e.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getInstances
		 * ().getParticleMesh().getMaterial()).setThickness((float) Math.sin(t)); }
		 */

		GLFWGamepadState gps = window.getGamepad();

		if (mainMenuTransitionState != MAIN_MENU_TRANSITION_STATE_OFF) {
			updateMainMenuTransition();
			return;
		}

		if (gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == GLFW.GLFW_PRESS && menuIndex == MENU_MAIN_MENU) {
			startMainMenuTransitionIn(verticalIndex);
			return;
		}

		Pair<Direction, Float> dirf = cic.getSoftDirection(gps);
		Direction ddir = dirf.getKey();
		float progress = dirf.getValue();
		if (Direction.SOUTH.equals(ddir) || Direction.NORTH.equals(ddir) || Direction.NONE.equals(ddir)) {
			placeMainMenuElements(Direction.NORTH.equals(ddir) ? progress : -progress);
		}

		cic.update(gps);
		if (cic.hasNext()) {
			Direction dir = cic.consume();
			switch (dir) {
			case NORTH:
				verticalIndex--;
				break;

			case SOUTH:
				verticalIndex++;
				break;

			case EAST:
			case WEST:
				return;

			default:
				return;
			}
		}

		verticalIndex = org.joml.Math.clamp(0, 2, verticalIndex);
		chooseMainMenuElement();

	}
	
	private int mainMenuTransitionState = 0;
	private float mainMenuTransitionValue = 0;

	private void updateMainMenuTransition() {
		if (mainMenuTransitionState == MAIN_MENU_TRANSITION_STATE_IN) {
			mainMenuTransitionValue += MAIN_MENU_TRANSITION_VALUE_INCREMENT;
		} else if (mainMenuTransitionState == MAIN_MENU_TRANSITION_STATE_OUT) {
			mainMenuTransitionValue -= MAIN_MENU_TRANSITION_VALUE_INCREMENT;
		}
		
		mainMenuTransitionValue = Math.clamp(0, 1, mainMenuTransitionValue);
		
		for (int i = 0; i <= 2; i++) {
			Entity te = entities1[i];
			Transform3D transform = te.getComponent(Transform3DComponent.class).getTransform();
			transform.setTranslation(new Vector3f(Math.lerp(X_POS_START, X_POS_END, Math.clamp(0, 1, mainMenuTransitionValue-i*0.25f)), transform.getTranslation().y, transform.getTranslation().z)).updateMatrix();
		}

		if (mainMenuTransitionValue == 1 || mainMenuTransitionValue == 0) {
			mainMenuTransitionState = MAIN_MENU_TRANSITION_STATE_OFF;
		}
	}

	private void startMainMenuTransitionIn(int verticalIndex) {
		mainMenuTransitionState = MAIN_MENU_TRANSITION_STATE_IN;
		mainMenuTransitionValue = 0;
	}

	private void chooseMainMenuElement() {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			for (int i = 0; i <= 2; i++) {
				Entity te = entities1[i];
				TextMaterial mat = ((TextMaterial) te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getInstances().getParticleMesh().getMaterial());
				if (i == verticalIndex) {
					te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + TEXTS[i] + "<").updateText();
					mat.setFgColor(HIGHLIGHT_COLOR);
				} else {
					te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(TEXTS[i]).updateText();
					mat.setFgColor(IDLE_COLOR);
				}
			}
			return null;
		}).push();
	}

	private void placeMainMenuElements(float progressPlayMenu) {
		for (int i = 0; i <= 2; i++) {
			Entity te = entities1[i];
			if (i == verticalIndex) {
				te.getComponent(Transform3DComponent.class).getTransform().setTranslation(entities1pos[i].add(Math.lerp(X_POS_START, X_POS_END, mainMenuTransitionValue), Y_POS_OFFSET * progressPlayMenu, 0, new Vector3f())).updateMatrix();
			} else {
				te.getComponent(Transform3DComponent.class).getTransform().setTranslation(entities1pos[i].add(Math.lerp(X_POS_START, X_POS_END, mainMenuTransitionValue), 0, 0, new Vector3f())).updateMatrix();
			}
		}
	}

}
