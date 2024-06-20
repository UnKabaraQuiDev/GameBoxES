package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.io.IOException;
import java.util.function.Function;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.SelectableUITextLabel;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UIEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UIInteractRunnable;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UITextButton;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UITextLabel;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.GameMode;
import lu.kbra.gamebox.client.es.game.game.utils.QuaternionArrayCallbackValueInterpolator;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalOptions;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UISceneStartMenuState extends UISceneState {

	private static final float MAIN_X_POS_START = 0f;
	private static final float MAIN_X_POS_END = -10f;

	private static final float OTHER_X_POS_START = 0f;
	private static final float OTHER_X_POS_END = 10f;

	private static String[] MAIN_MENU_TEXTS = { GlobalLang.get("menu.main.play"), GlobalLang.get("menu.main.options"), GlobalLang.get("menu.main.credits"), GlobalLang.get("menu.main.quit") };

	private UIEntity[] entitiesMainMenu;
	private Vector3f[] entitiesMainMenupos;
	private UIEntity play, options, credits, quit;

	private static String[] PLAY_MENU_TEXTS = { GlobalLang.get("menu.play.evolution"), GlobalLang.get("menu.play.infection") };

	private UIEntity[] entitiesPlayMenu;
	private Vector3f[] entitiesPlayMenupos;
	private UIEntity playMode1, playMode2;

	private static String[] OPTION_MENU_TEXTS = { GlobalLang.LANGUAGES[0], GlobalLang.get("menu.options.volume") + ": 0" + GlobalOptions.VOLUME };

	private UIEntity[] entitiesOptionMenu;
	private Vector3f[] entitiesOptionMenupos;
	private UIEntity optionLanguage, optionVolume;

	private static String[] CREDITS_MENU_TEXTS = { GlobalLang.get("credits.list") };

	private UIEntity[] entitiesCreditsMenu;
	private Vector3f[] entitiesCreditsMenupos;
	private UIEntity creditsText;

	private static final int MI_NONE = 0;
	private static final int MI_MAIN = 1;
	private static final int MI_OPTIONS = 2;
	private static final int MI_PLAY = 3;
	private static final int MI_CREDITS = 4;

	private static final int VI_MAIN_PLAY = 0;
	private static final int VI_MAIN_OPTIONS = 1;
	private static final int VI_MAIN_CREDITS = 2;
	private static final int VI_MAIN_QUIT = 3;

	private static final int VI_PLAY_EVOLUTION = 0;
	private static final int VI_PLAY_INFECTION = 1;

	private static final int VI_OPTIONS_OPTION_LANGUAGE = 0;
	private static final int VI_OPTIONS_OPTION_VOLUME = 1;

	private int menuIndex = MI_MAIN;
	private int mainVerticalIndex = VI_MAIN_PLAY;
	private int otherVerticalIndex = mainVerticalIndex;

	private static final int MENU_TRANSITION_IDLE = 0;
	private static final int MENU_TRANSITION_ONGOING = 1;

	private int menuTransition = MENU_TRANSITION_IDLE;

	private static final float MENU_TRANSITION_INC = 0.1f;
	private static final float ANIMATION_IDLE_SPEED = 0.9f;
	private static final float ANIMATION_IDLE_ANGLE = 3f; // degrees

	private float menuTransitionValue = 0;

	private int menuTransitionTarget = MI_NONE;
	private int menuTransitionBase = MI_NONE;

	private ControllerInputWatcher cic = new ControllerInputWatcher();
	private QuaternionArrayCallbackValueInterpolator rotInterpolator;

	public UISceneStartMenuState(UIScene3D scene) {
		super("MainMenu", scene);

		// play = addText("playText", MAIN_MENU_TEXTS[0], new Vector3f(0, 0.8f, 0));
		play = addTextLabel("playText", MAIN_MENU_TEXTS[0], new Vector3f(0, 0.8f, 0), Alignment.TEXT_CENTER);
		options = addTextLabel("optionsText", MAIN_MENU_TEXTS[1], new Vector3f(0, 0, 0), Alignment.TEXT_CENTER);
		credits = addTextLabel("creditsText", MAIN_MENU_TEXTS[2], new Vector3f(0, -0.8f, 0), Alignment.TEXT_CENTER);
		quit = addTextLabel("quitText", MAIN_MENU_TEXTS[3], new Vector3f(0, -1.6f, 0), Alignment.TEXT_CENTER);

		entitiesMainMenu = new UIEntity[] { play, options, credits, quit };
		entitiesMainMenupos = new Vector3f[] { new Vector3f(0, 0.8f, 0), new Vector3f(0, 0, 0), new Vector3f(0, -0.8f, 0), new Vector3f(0, -1.6f, 0) };

		playMode1 = addTextLabel("playMode1", PLAY_MENU_TEXTS[0], new Vector3f(0f, 0.4f, 0), Alignment.TEXT_CENTER, this::interact_playMode1);
		playMode2 = addTextLabel("playMode2", PLAY_MENU_TEXTS[1], new Vector3f(0f, -0.4f, 0), Alignment.TEXT_CENTER, this::interact_playMode2);

		entitiesPlayMenu = new UIEntity[] { playMode1, playMode2 };
		entitiesPlayMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0) };

		optionLanguage = addTextLabel("optionslanguage", GlobalLang.getLongestLang(), new Vector3f(0f, 0.4f, 0), Alignment.TEXT_CENTER, (Entity entity, boolean direction, Direction dir, Button button) -> {
			if (direction) {
				if (dir.equals(Direction.EAST)) {
					GlobalOptions.LANGUAGE++;
				}
				GlobalOptions.LANGUAGE %= GlobalLang.LANGUAGES.length;
				TextEmitter te = entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache);
				OPTION_MENU_TEXTS[0] = GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE];
				((UITextLabel) optionLanguage).setText(OPTION_MENU_TEXTS[0]);
				GlobalUtils.updateText(te);
			}
		});

		optionVolume = addTextLabel("optionvolumetext", OPTION_MENU_TEXTS[1] + ": 0" + GlobalOptions.VOLUME, new Vector3f(0, -0.4f, 0), Alignment.TEXT_CENTER, (Entity entity, boolean direction, Direction dir, Button button) -> {
			if (direction) {
				if (dir.equals(Direction.WEST)) {
					GlobalOptions.VOLUME--;
				} else if (dir.equals(Direction.EAST)) {
					GlobalOptions.VOLUME++;
				}
				TextEmitter te = entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache);
				OPTION_MENU_TEXTS[1] = GlobalLang.get("menu.options.volume") + ": " + MathUtils.fillPrefix(2, '0', GlobalOptions.VOLUME + "");
				((UITextLabel) optionVolume).setText(OPTION_MENU_TEXTS[1]);
				GlobalUtils.updateText(te);
			}
		});
		((UITextLabel) optionVolume).interact(true, Direction.NONE, Button.NONE);

		entitiesOptionMenu = new UIEntity[] { optionLanguage, optionVolume };
		entitiesOptionMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0) };

		creditsText = addInertTextLabel("creditsList", CREDITS_MENU_TEXTS[0], new Vector3f(0, 2f, 0), Alignment.TEXT_CENTER);
		creditsText.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setCharOffset(new Vector2f(0, 0.2f));
		creditsText.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setCorrectTransform(true);
		creditsText.getComponent(TextEmitterComponent.class).getTextEmitter(cache).updateText();
		creditsText.getComponent(Transform3DComponent.class).getTransform().setScale(0.85f).updateMatrix();

		entitiesCreditsMenu = new UIEntity[] { creditsText };
		entitiesCreditsMenupos = new Vector3f[] { new Vector3f(0, 2f, 0) };

		updateLanguge();

		chooseMenuElement(entitiesMainMenu, mainVerticalIndex);
		chooseMenuElement(entitiesPlayMenu, otherVerticalIndex);
		chooseMenuElement(entitiesOptionMenu, otherVerticalIndex);
		chooseMenuElement(entitiesCreditsMenu, otherVerticalIndex);

		menuTransitionValue = 1; // set to inactive
		setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		setPos(entitiesCreditsMenu, entitiesCreditsMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		menuTransitionValue = 0; // set to active
		setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_START, MAIN_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));

		rotInterpolator = new QuaternionArrayCallbackValueInterpolator(new Entity[] { play, options, quit, playMode1, playMode2, optionLanguage, optionVolume, credits }, new Quaternionf().rotateLocalZ(Math.toRadians(-ANIMATION_IDLE_ANGLE)),
				new Quaternionf().rotateLocalZ(Math.toRadians(ANIMATION_IDLE_ANGLE)), Interpolators.CUBIC_IN_OUT);
	}

	private void interact_playMode1(Entity entity1, boolean boolean2, Direction direction3, Button button4) {
		if (button4.equals(Button.EAST))
			GlobalUtils.INSTANCE.startGame(GameMode.EVOLUTION);
	}

	private void interact_playMode2(Entity entity1, boolean boolean2, Direction direction3, Button button4) {
		if (button4.equals(Button.EAST))
			GlobalUtils.INSTANCE.startGame(GameMode.INFECTION);
	}

	private UISliderEntity addSlider(String name) { // need to also add text, create TextSlideBar entity or smth
		UISliderEntity slider = new UISliderEntity(name, cache, new Vector2f(2, 0.3f), new Vector2f(0, 100), 0.05f, 0.5f, new Transform3D());
		return (UISliderEntity) scene.addEntity(name, slider);
	}

	private UITextButton addTextButton(String name, String txt, String btnType, Vector3f pos) {
		return (UITextButton) scene.addEntity(name, new UITextButton(cache, name, txt, pos, Alignment.TEXT_CENTER, btnType));
	}

	private SelectableUITextLabel addTextLabel(String name, String txt, Vector3f pos, Alignment alignment) {
		return (SelectableUITextLabel) scene.addEntity(name, new SelectableUITextLabel(cache, name, txt, pos, alignment));
	}

	private SelectableUITextLabel addTextLabel(String name, String txt, Vector3f pos, Alignment alignment, UIInteractRunnable run) {
		return (SelectableUITextLabel) scene.addEntity(name, new SelectableUITextLabel(cache, name, txt, pos, alignment, run));
	}

	private UITextLabel addInertTextLabel(String name, String txt, Vector3f pos, Alignment alignment) {
		return (UITextLabel) scene.addEntity(name, new UITextLabel(cache, name, txt, pos, alignment));
	}

	@Override
	public void input(float dTime) {
		chooseMenuElement(entitiesMainMenu, mainVerticalIndex);
		chooseMenuElement(entitiesOptionMenu, otherVerticalIndex);
		chooseMenuElement(entitiesPlayMenu, otherVerticalIndex);

		if (menuTransition != MENU_TRANSITION_IDLE) {
			updateMenuTransition();
			return;
		}

		if (window.isJoystickPresent()) {
			if (!window.updateGamepad(0))
				return;

			GLFWGamepadState gps = window.getGamepad();

			cic.updateButton(gps);
			
			
			if (Button.EAST.equals(cic.getButton()) && menuIndex == MI_MAIN) {
				if (mainVerticalIndex == VI_MAIN_QUIT) {
					GlobalUtils.requestQuit();
					return;
				}
				startTransition(MI_MAIN, mainVerticalIndex == VI_MAIN_PLAY ? MI_PLAY : (mainVerticalIndex == VI_MAIN_OPTIONS ? MI_OPTIONS : (mainVerticalIndex == VI_MAIN_CREDITS ? MI_CREDITS : MI_NONE)));
				otherVerticalIndex = 0;
				return;
			}else if (Button.SOUTH.equals(cic.getButton()) && menuIndex != MI_MAIN) {
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
		} else { // keyboard
			if (window.isKeyPressed(GLFW.GLFW_KEY_KP_MULTIPLY) && menuIndex == MI_MAIN) {
				if (mainVerticalIndex == VI_MAIN_QUIT) {
					GlobalUtils.requestQuit();
					return;
				}
				startTransition(MI_MAIN, mainVerticalIndex == VI_MAIN_PLAY ? MI_PLAY : (mainVerticalIndex == VI_MAIN_OPTIONS ? MI_OPTIONS : (mainVerticalIndex == VI_MAIN_CREDITS ? MI_CREDITS : MI_NONE)));
				otherVerticalIndex = 0;
				return;
			}
			if (window.isKeyPressed(GLFW.GLFW_KEY_KP_DIVIDE) && menuIndex != MI_MAIN) {
				startTransition(menuIndex, MI_MAIN);
				return;
			}

			cic.updateDirection(window);
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
		}

		if (menuIndex == MI_MAIN) {
			mainVerticalIndex = org.joml.Math.clamp(0, 3, mainVerticalIndex);
		} else if (menuIndex == MI_OPTIONS) {
			otherVerticalIndex = org.joml.Math.clamp(0, 1, otherVerticalIndex);
		} else if (menuIndex == MI_PLAY) {
			otherVerticalIndex = org.joml.Math.clamp(0, 1, otherVerticalIndex);
		}
	}

	private float time = 0;

	@Override
	public void update(float dTime) {
		time += dTime;
		rotInterpolator.set(ANIMATION_IDLE_SPEED * time).zigzag().exec();
	}

	@Override
	public void render(float dTime) {
	}

	private void interact(int verticalIndex, boolean direction, Direction dir, Button button) {
		Entity entity = getSelectedEntity();
		if (direction) { // analog input (slider, etc)
			if (entity instanceof UISliderEntity) {
				if (dir.equals(Direction.EAST)) {
					((UISliderEntity) entity).getSliderComponent().increment();
				} else if (dir.equals(Direction.WEST)) {
					((UISliderEntity) entity).getSliderComponent().decrement();
				}
				((UISliderEntity) entity).updateUI();
			} else if (entity instanceof UITextLabel) { // interaction button
				((UITextLabel) entity).interact(direction, dir, button);
			}
		} else if (!Button.NONE.equals(button)) { // button
			if (entity instanceof UISliderEntity) {
				// ignore
			} else if (entity instanceof UITextLabel) { // interaction button
				((UITextLabel) entity).interact(direction, dir, button);
			}
		}
	}

	private Entity getSelectedEntity() {
		if (menuIndex == MI_MAIN) {
			return entitiesMainMenu[mainVerticalIndex];
		} else if (menuIndex == MI_OPTIONS) {
			return entitiesOptionMenu[otherVerticalIndex];
		} else if (menuIndex == MI_PLAY) {
			return entitiesPlayMenu[otherVerticalIndex];
		} else if (menuIndex == MI_CREDITS) {
			return entitiesCreditsMenu[otherVerticalIndex];
		}
		return null;
	}

	private void setFGColor(Entity entity, Vector4f color) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			if (entity.hasComponent(TextEmitterComponent.class)) {
				TextMaterial mat = ((TextMaterial) entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getInstances().getParticleMesh().getMaterial());
				mat.setFgColor(color);
			}
			return null;
		}).push();
	}

	private void startTransition(int from, int to) {
		this.menuTransitionTarget = to;
		this.menuTransitionBase = from;

		menuTransition = MENU_TRANSITION_ONGOING;
	}

	private void updateMenuTransition() {
		if (menuTransition == MENU_TRANSITION_ONGOING) {
			menuTransitionValue += MENU_TRANSITION_INC;
		}

		menuTransitionValue = Math.clamp(0, 1, menuTransitionValue);

		if (menuTransitionBase == MI_MAIN) {
			setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_START, MAIN_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		} else if (menuTransitionTarget == MI_MAIN) {
			setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_END, MAIN_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		}

		if (menuTransitionBase == MI_PLAY) {
			setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		} else if (menuTransitionTarget == MI_PLAY) {
			setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_END, OTHER_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		}

		if (menuTransitionBase == MI_OPTIONS) {
			setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
			updateLanguge();
		} else if (menuTransitionTarget == MI_OPTIONS) {
			setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_END, OTHER_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		}

		if (menuTransitionBase == MI_CREDITS) {
			setPos(entitiesCreditsMenu, entitiesCreditsMenupos, OTHER_X_POS_START, OTHER_X_POS_END, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
			updateLanguge();
		} else if (menuTransitionTarget == MI_CREDITS) {
			setPos(entitiesCreditsMenu, entitiesCreditsMenupos, OTHER_X_POS_END, OTHER_X_POS_START, i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue * (i + 1)));
		}

		if (menuTransitionValue >= 1) {
			menuTransition = MENU_TRANSITION_IDLE;
			menuIndex = menuTransitionTarget;

			menuTransitionTarget = MI_NONE;
			menuTransitionBase = MI_NONE;

			menuTransitionValue = 0;
		}
	}

	private void updateLanguge() {
		String langName = GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE];
		try {
			GlobalLang.load(langName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MAIN_MENU_TEXTS = new String[] { GlobalLang.get("menu.main.play"), GlobalLang.get("menu.main.options"), GlobalLang.get("menu.main.credits"), GlobalLang.get("menu.main.quit") };
		PLAY_MENU_TEXTS = new String[] { GlobalLang.get("menu.play.evolution"), GlobalLang.get("menu.play.infection") + "" };
		OPTION_MENU_TEXTS = new String[] { langName, GlobalLang.get("menu.options.volume") + ": " + MathUtils.fillPrefix(2, '0', GlobalOptions.VOLUME + "") };
		CREDITS_MENU_TEXTS = new String[] { GlobalLang.get("credits.list") };

		updateMenuTexts(entitiesMainMenu, MAIN_MENU_TEXTS);
		updateMenuTexts(entitiesPlayMenu, PLAY_MENU_TEXTS);
		updateMenuTexts(entitiesOptionMenu, OPTION_MENU_TEXTS);
		updateMenuTexts(entitiesCreditsMenu, CREDITS_MENU_TEXTS);
	}

	private void setPos(Entity[] entities, Vector3f[] offset, float start, float end, Function<Integer, Float> intFun) {
		for (int i = 0; i < entities.length; i++) {
			Entity te = entities[i];
			Transform3D transform = te.getComponent(Transform3DComponent.class).getTransform();
			if (te instanceof UITextButton) {
				((UITextButton) te).setPosition(new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))), transform.getTranslation().y, transform.getTranslation().z));
			} else {
				transform.setTranslation(new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))) + offset[i].x, offset[i].y, offset[i].z)).updateMatrix();
			}
		}
	}

	private void updateMenuTexts(UIEntity[] entities, String[] txt) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			for (int i = 0; i < entities.length; i++) {
				UIEntity te = entities[i];

				if (te.hasComponent(TextEmitterComponent.class)) {
					if (te instanceof UITextLabel) {
						((UITextLabel) te).setText(txt[i]);
					} else {
						GlobalLogger.warning("Unsupported UIEntity: " + te.getClass().getName());
					}
					te.updateUI();
				}
			}
			return null;
		}).push();
	}

	private void chooseMenuElement(UIEntity[] entities, int verticalIndex) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			for (int i = 0; i < entities.length; i++) {
				UIEntity te = entities[i];

				if (te.hasComponent(TextEmitterComponent.class)) {
					te.setSelected(i == verticalIndex);
					te.updateUI();
				}
			}
			return null;
		}).push();
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + getClass().getName());

		scene.getEntities().clear();

		super.cleanup();
	}

}
