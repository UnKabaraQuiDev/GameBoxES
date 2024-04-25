package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.util.function.Function;

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
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UISceneStartMenuState extends UISceneState {

	private static final float MAIN_X_POS_START = 0f;
	private static final float MAIN_X_POS_END = -8f;

	private static final float OTHER_X_POS_START = 0f;
	private static final float OTHER_X_POS_END = 10f;

	private static final String TEXT_PLAY = "PLAY !", TEXT_OPTIONS = "OPTIONS", TEXT_QUIT = "QUIT";
	private static final String[] MAIN_MENU_TEXTS = { TEXT_PLAY, TEXT_OPTIONS, TEXT_QUIT };

	private Entity[] entitiesMainMenu;
	private Vector3f[] entitiesMainMenupos;
	private Entity play, options, quit;

	private static final String TEXT_PLAY_MODE_1 = "PLAY MODE 1!", TEXT_PLAY_MODE_2 = "PLAY MODE 2!";
	private static final String[] PLAY_MENU_TEXTS = { TEXT_PLAY_MODE_1, TEXT_PLAY_MODE_2 };

	private Entity[] entitiesPlayMenu;
	private Vector3f[] entitiesPlayMenupos;
	private Entity playMode1, playMode2;

	private static final String TEXT_OPTION_1 = "OPTION 1", TEXT_OPTION_2 = "OPTION 2";
	private static final String[] OPTION_MENU_TEXTS = { TEXT_OPTION_1, TEXT_OPTION_2 };

	private Entity[] entitiesOptionMenu;
	private Vector3f[] entitiesOptionMenupos;
	private Entity option1, option2;

	private static final int MI_NONE = 0;
	private static final int MI_MAIN = 1;
	private static final int MI_OPTIONS = 2;
	private static final int MI_PLAY = 3;

	private static final int VI_MAIN_PLAY = 0;
	private static final int VI_MAIN_OPTIONS = 1;
	private static final int VI_MAIN_QUIT = 2;

	private static final int VI_OPTIONS_OPTION_1 = 0;
	private static final int VI_OPTIONS_OPTION_2 = 1;

	private int menuIndex = MI_MAIN;
	private int mainVerticalIndex = VI_MAIN_PLAY;
	private int otherVerticalIndex = mainVerticalIndex;

	private static final int MENU_TRANSITION_IDLE = 0;
	private static final int MENU_TRANSITION_ONGOIG = 1;

	private int menuTransition = MENU_TRANSITION_IDLE;

	private static final float MENU_TRANSITION_INC = 0.1f;

	private float menuTransitionValue = 0;

	private int menuTransitionTarget = MI_NONE;
	private int menuTransitionBase = MI_NONE;

	private ControllerInputWatcher cic = new ControllerInputWatcher();

	public UISceneStartMenuState(UIScene3D scene) {
		super(scene);

		play = addText("playText", 10, TEXT_PLAY, new Vector3f(0, 0.8f, 0));
		options = addText("optionsText", 10, TEXT_OPTIONS, new Vector3f(0, 0, 0));
		quit = addText("quitText", 10, TEXT_QUIT, new Vector3f(0, -0.8f, 0));

		entitiesMainMenu = new Entity[] { play, options, quit };
		entitiesMainMenupos = new Vector3f[] { new Vector3f(0, 0.8f, 0), new Vector3f(0, 0, 0), new Vector3f(0, -0.8f, 0) };

		playMode1 = addText("playMode1", 15, TEXT_PLAY_MODE_1, new Vector3f(0f, 0.4f, 0));
		playMode2 = addText("playMode2", 15, TEXT_PLAY_MODE_2, new Vector3f(0f, -0.4f, 0));

		entitiesPlayMenu = new Entity[] { playMode1, playMode2 };
		entitiesPlayMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0) };

		option1 = addText("option1", 15, TEXT_OPTION_1, new Vector3f(0f, 0.4f, 0));
		option2 = addSlider("option2");

		entitiesOptionMenu = new Entity[] { option1, option2 };
		entitiesOptionMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0) };

		chooseMenuElement(entitiesMainMenu, MAIN_MENU_TEXTS, mainVerticalIndex);
		chooseMenuElement(entitiesPlayMenu, PLAY_MENU_TEXTS, otherVerticalIndex);
		chooseMenuElement(entitiesOptionMenu, OPTION_MENU_TEXTS, otherVerticalIndex);

		menuTransitionValue = 1; // set to inactive
		setPos(entitiesPlayMenu, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		setPos(entitiesOptionMenu, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		menuTransitionValue = 0; // set to active
		setPos(entitiesMainMenu, MAIN_X_POS_START, MAIN_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
	}

	private Entity addSlider(String name) { // need to also add text, create TextSlideBar entity or smth
		UISliderEntity slider = new UISliderEntity(cache, new Vector2f(2, 0.3f), new Vector2f(0, 100), 0.05f, 0.5f, new Transform3D());
		return scene.addEntity(name, slider);
	}

	private Entity addButton(String name, String txt, Vector3f pos) {
		TextMaterial mat = new TextMaterial("TextMaterial-" + GameBoxES.TEXT_TEXTURE + "-" + name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GameBoxES.TEXT_TEXTURE));
		cache.addMaterial(mat);
		TextEmitter text = new TextEmitter(name, mat, length, txt, new Vector2f(0.35f, 0.5f));
		text.setAlignment(Alignment.ABSOLUTE_RIGHT);
		text.createDrawBuffer();
		text.updateText();
		cache.addTextEmitter(text);
		return scene.addEntity(name, new Entity(new Transform3DComponent(pos), new TextEmitterComponent(text)));
	}
	
	private Entity addText(String name, int length, String txt, Vector3f pos) {
		TextMaterial mat = new TextMaterial("TextMaterial-" + GameBoxES.TEXT_TEXTURE + "-" + name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GameBoxES.TEXT_TEXTURE));
		cache.addMaterial(mat);
		TextEmitter text = new TextEmitter(name, mat, txt.length()+2, txt, new Vector2f(0.35f, 0.5f));
		text.setAlignment(Alignment.ABSOLUTE_RIGHT);
		text.createDrawBuffer();
		text.updateText();
		cache.addTextEmitter(text);
		return scene.addEntity(name, new Entity(new Transform3DComponent(pos), new TextEmitterComponent(text)));
	}

	@Override
	public void input(float dTime) {
		if (!window.isJoystickPresent())
			return;
		if (!window.updateGamepad(0))
			return;

		GLFWGamepadState gps = window.getGamepad();

		chooseMenuElement(entitiesMainMenu, MAIN_MENU_TEXTS, mainVerticalIndex);
		chooseMenuElement(entitiesPlayMenu, PLAY_MENU_TEXTS, otherVerticalIndex);
		chooseMenuElement(entitiesOptionMenu, OPTION_MENU_TEXTS, otherVerticalIndex);

		if (menuTransition != MENU_TRANSITION_IDLE) {
			updateMenuTransition();
			return;
		}

		if (gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER) == GLFW.GLFW_PRESS && menuIndex == MI_MAIN) {
			if (mainVerticalIndex == VI_MAIN_QUIT) {
				GlobalUtils.requestQuit();
				return;
			}
			startTransition(MI_MAIN, mainVerticalIndex == VI_MAIN_PLAY ? MI_PLAY : (mainVerticalIndex == VI_MAIN_OPTIONS ? MI_OPTIONS : MI_NONE));
			otherVerticalIndex = 0;
			return;
		}
		if (gps.buttons(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) == GLFW.GLFW_PRESS && menuIndex != MI_MAIN) {
			startTransition(menuIndex, MI_MAIN);
			return;
		}

		Pair<Direction, Float> dirf = cic.getSoftDirection(gps);
		Direction ddir = dirf.getKey();
		float progress = dirf.getValue();
		if (Direction.SOUTH.equals(ddir) || Direction.NORTH.equals(ddir) || Direction.NONE.equals(ddir)) {
			// smooth animations ?
			// placeMainMenuElements(Direction.NORTH.equals(ddir) ? progress : -progress);
		}

		cic.updateButton(gps);
		if (cic.hasNextButton()) {
			interact(otherVerticalIndex, false, null, cic.consumeButton());
		}

		cic.updateDirection(gps);
		if (cic.hasNextDirection()) {
			Direction dir = cic.consumeDirection();
			switch (dir) {
			case NORTH:
				if (menuIndex == MI_MAIN) {
					mainVerticalIndex--;
				} else {
					otherVerticalIndex--;
				}
				break;

			case SOUTH:
				if (menuIndex == MI_MAIN) {
					mainVerticalIndex++;
				} else {
					otherVerticalIndex++;
				}
				break;

			case EAST:
			case WEST:
				if (menuIndex != MI_MAIN) {
					interact(otherVerticalIndex, true, dir, Button.NONE);
				}
				return;

			default:
				return;
			}
		}

		if (menuIndex == MI_MAIN) {
			otherVerticalIndex = org.joml.Math.clamp(0, 2, otherVerticalIndex);
		}
	}

	private void interact(int verticalIndex, boolean direction, Direction dir, Button button) {
		Entity entity = getSelectedEntity();
		if (direction) { // analog input (slider, etc)
			// do nothing
			if (entity instanceof UISliderEntity) {
				if(dir.equals(Direction.EAST)) {
					((UISliderEntity) entity).getSliderComponent().increment();
				}else if(dir.equals(Direction.WEST)) {
					((UISliderEntity) entity).getSliderComponent().decrement();
				}
				((UISliderEntity) entity).update();
			}
		} else { // button
			setFGColor(entity, GlobalConsts.PRIMARY_LIGHT);
		}
	}

	private Entity getSelectedEntity() {
		if (menuIndex == MI_MAIN) {
			return entitiesMainMenu[mainVerticalIndex];
		} else if (menuIndex == MI_OPTIONS) {
			return entitiesOptionMenu[otherVerticalIndex];
		}else if (menuIndex == MI_PLAY) {
			return entitiesPlayMenu[otherVerticalIndex];
		}
		return null;
	}

	private void setFGColor(Entity entity, Vector4f primaryLight) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			if (entity.hasComponent(TextEmitterComponent.class)) {
				TextMaterial mat = ((TextMaterial) entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getInstances().getParticleMesh().getMaterial());
				mat.setFgColor(GlobalConsts.HIGHLIGHT);
			}
			return null;
		}).push();
	}

	private void startTransition(int from, int to) {
		this.menuTransitionTarget = to;
		this.menuTransitionBase = from;

		menuTransition = MENU_TRANSITION_ONGOIG;
	}

	private void updateMenuTransition() {
		if (menuTransition == MENU_TRANSITION_ONGOIG) {
			menuTransitionValue += MENU_TRANSITION_INC;
		}

		menuTransitionValue = Math.clamp(0, 1, menuTransitionValue);

		if (menuTransitionBase == MI_MAIN) {
			setPos(entitiesMainMenu, MAIN_X_POS_START, MAIN_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		} else if (menuTransitionTarget == MI_MAIN) {
			setPos(entitiesMainMenu, MAIN_X_POS_END, MAIN_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		}

		if (menuTransitionBase == MI_PLAY) {
			setPos(entitiesPlayMenu, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		} else if (menuTransitionTarget == MI_PLAY) {
			setPos(entitiesPlayMenu, OTHER_X_POS_END, OTHER_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		}

		if (menuTransitionBase == MI_OPTIONS) {
			setPos(entitiesOptionMenu, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		} else if (menuTransitionTarget == MI_OPTIONS) {
			setPos(entitiesOptionMenu, OTHER_X_POS_END, OTHER_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		}

		if (menuTransitionValue >= 1) {
			menuTransition = MENU_TRANSITION_IDLE;
			menuIndex = menuTransitionTarget;

			menuTransitionTarget = MI_NONE;
			menuTransitionBase = MI_NONE;

			menuTransitionValue = 0;
		}
	}

	private void setPos(Entity[] entities, float start, float end, Function<Integer, Float> intFun) {
		for (int i = 0; i < entities.length; i++) {
			Entity te = entities[i];
			Transform3D transform = te.getComponent(Transform3DComponent.class).getTransform();
			transform.setTranslation(new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))), transform.getTranslation().y, transform.getTranslation().z)).updateMatrix();
		}
	}

	private void chooseMenuElement(Entity[] entities, String[] texts, int verticalIndex) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			for (int i = 0; i < entities.length; i++) {
				Entity te = entities[i];

				if (te.hasComponent(TextEmitterComponent.class)) {
					TextMaterial mat = ((TextMaterial) te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getInstances().getParticleMesh().getMaterial());
					if (i == verticalIndex) {
						te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + texts[i] + "<").updateText();
						mat.setFgColor(GlobalConsts.HIGHLIGHT);
					} else {
						te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(texts[i]).updateText();
						mat.setFgColor(GlobalConsts.NEUTRAL);
					}
				}
			}
			return null;
		}).push();
	}

}
