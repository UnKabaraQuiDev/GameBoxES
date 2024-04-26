package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MathUtil;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UITextButton;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UITextLabel;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;
import lu.pcy113.pclib.Pair;

public class UISceneStartMenuState extends UISceneState {

	private static final float MAIN_X_POS_START = 0f;
	private static final float MAIN_X_POS_END = -8f;

	private static final float OTHER_X_POS_START = 0f;
	private static final float OTHER_X_POS_END = 10f;

	private static String[] MAIN_MENU_TEXTS = { GlobalLang.get("menu.main.play"), GlobalLang.get("menu.main.options"),
			GlobalLang.get("menu.main.quit") };

	private Entity[] entitiesMainMenu;
	private Vector3f[] entitiesMainMenupos;
	private Entity play, options, quit;

	private static String[] PLAY_MENU_TEXTS = { GlobalLang.get("menu.play.evolution"),
			GlobalLang.get("menu.play.infection") };

	private Entity[] entitiesPlayMenu;
	private Vector3f[] entitiesPlayMenupos;
	private Entity playMode1, playMode2;

	private static String[] OPTION_MENU_TEXTS = { GlobalLang.LANGUAGES[0], GlobalLang.get("menu.options.volume") };

	private Entity[] entitiesOptionMenu;
	private Vector3f[] entitiesOptionMenupos;
	private Entity optionLanguage, optionVolumeText;

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
	private static final int MENU_TRANSITION_ONGOING = 1;

	private int menuTransition = MENU_TRANSITION_IDLE;

	private static final float MENU_TRANSITION_INC = 0.1f;

	private float menuTransitionValue = 0;

	private int menuTransitionTarget = MI_NONE;
	private int menuTransitionBase = MI_NONE;

	private ControllerInputWatcher cic = new ControllerInputWatcher();

	public UISceneStartMenuState(UIScene3D scene) {
		super(scene);

		// play = addText("playText", MAIN_MENU_TEXTS[0], new Vector3f(0, 0.8f, 0));
		play = addTextLabel("playText", MAIN_MENU_TEXTS[0], new Vector3f(0, 0.8f, 0), Alignment.TEXT_CENTER);
		options = addTextLabel("optionsText", MAIN_MENU_TEXTS[1], new Vector3f(0, 0, 0), Alignment.TEXT_CENTER);
		quit = addTextLabel("quitText", MAIN_MENU_TEXTS[2], new Vector3f(0, -0.8f, 0), Alignment.TEXT_CENTER);

		entitiesMainMenu = new Entity[] { play, options, quit };
		entitiesMainMenupos = new Vector3f[] { new Vector3f(0, 0.8f, 0), new Vector3f(0, 0, 0),
				new Vector3f(0, -0.8f, 0) };

		playMode1 = addTextLabel("playMode1", PLAY_MENU_TEXTS[0], new Vector3f(0f, 0.4f, 0), Alignment.TEXT_CENTER, this::interact_playMode1);
		playMode2 = addTextLabel("playMode2", PLAY_MENU_TEXTS[1], new Vector3f(0f, -0.4f, 0), Alignment.TEXT_CENTER, this::interact_playMode2);

		entitiesPlayMenu = new Entity[] { playMode1, playMode2 };
		entitiesPlayMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0) };

		optionLanguage = addTextLabel("optionslanguage", GlobalLang.getLongestLang(), new Vector3f(0f, 0.4f, 0),
				Alignment.TEXT_CENTER, (Entity entity, boolean direction, Direction dir, Button button) -> {
					if (direction) {
						if (dir.equals(Direction.WEST)) {
							entity.getComponent(IntValueComponent.class)
									.setValue(entity.getComponent(IntValueComponent.class).getValue() - 1);
						} else if (dir.equals(Direction.EAST)) {
							entity.getComponent(IntValueComponent.class)
									.setValue(entity.getComponent(IntValueComponent.class).getValue() + 1);
						}
						TextEmitter te = entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache);
						OPTION_MENU_TEXTS[0] = GlobalLang.LANGUAGES[entity.getComponent(IntValueComponent.class).getValue()];
						te.setText(OPTION_MENU_TEXTS[0]);
						GlobalUtils.updateText(te);
					}
				});
		optionLanguage.addComponent(new IntValueComponent(
				Arrays.binarySearch(GlobalLang.LANGUAGES, GlobalLang.getCURRENT_LANG()), GlobalLang.LANGUAGES.length));

		optionVolumeText = addTextLabel("optionvolumetext", OPTION_MENU_TEXTS[1]+": 05", new Vector3f(0, -0.4f, 0),
				Alignment.TEXT_CENTER, (Entity entity, boolean direction, Direction dir, Button button) -> {
					System.out.println("callback "+direction+" "+dir+" "+button+" "+entity.getComponent(IntValueComponent.class).getValue());
					if (direction) {
						if (dir.equals(Direction.WEST)) {
							entity.getComponent(IntValueComponent.class)
									.setValue(entity.getComponent(IntValueComponent.class).getValue() - 1);
						} else if (dir.equals(Direction.EAST)) {
							entity.getComponent(IntValueComponent.class)
									.setValue(entity.getComponent(IntValueComponent.class).getValue() + 1);
						}
						TextEmitter te = entity.getComponent(TextEmitterComponent.class)
								.getTextEmitter(cache);
						OPTION_MENU_TEXTS[1] = GlobalLang.get("menu.options.volume")+": " + MathUtils.fillPrefix(2, '0', entity.getComponent(IntValueComponent.class).getValue()+"");
						te.setText(OPTION_MENU_TEXTS[1]);
						GlobalUtils.updateText(te);
						System.err.println(": "+te.getText()+" "+te.getInstances().getParticleCount());
					}
				});
		optionVolumeText.addComponent(new IntValueComponent(5, 0, 10));
		((UITextLabel) optionVolumeText).interact(true, Direction.NONE, Button.NONE);


		entitiesOptionMenu = new Entity[] { optionLanguage, optionVolumeText };
		entitiesOptionMenupos = new Vector3f[] { new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0)};

		chooseMenuElement(entitiesMainMenu, MAIN_MENU_TEXTS, mainVerticalIndex);
		chooseMenuElement(entitiesPlayMenu, PLAY_MENU_TEXTS, otherVerticalIndex);
		chooseMenuElement(entitiesOptionMenu, OPTION_MENU_TEXTS, otherVerticalIndex);

		menuTransitionValue = 1; // set to inactive
		setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_START, OTHER_X_POS_END,
				i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_START, OTHER_X_POS_END,
				i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
		menuTransitionValue = 0; // set to active
		setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_START, MAIN_X_POS_END,
				i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue));
	}

	private void interact_playMode1(Entity entity1, boolean boolean2, Direction direction3, Button button4) {
		GlobalUtils.INSTANCE.startGame(GameMode.EVOLUTION);
	}

	private void interact_playMode2(Entity entity1, boolean boolean2, Direction direction3, Button button4) {
		GlobalUtils.INSTANCE.startGame(GameMode.EVOLUTION);
	}
	
	private Entity addSlider(String name) { // need to also add text, create TextSlideBar entity or smth
		UISliderEntity slider = new UISliderEntity(cache, new Vector2f(2, 0.3f), new Vector2f(0, 100), 0.05f, 0.5f,
				new Transform3D());
		return scene.addEntity(name, slider);
	}

	private UITextButton addTextButton(String name, String txt, String btnType, Vector3f pos) {
		return (UITextButton) scene.addEntity(name,
				new UITextButton(cache, name, txt, pos, Alignment.TEXT_CENTER, btnType));
	}

	private UITextLabel addTextLabel(String name, String txt, Vector3f pos, Alignment alignment) {
		return (UITextLabel) scene.addEntity(name, new UITextLabel(cache, name, txt, pos, alignment));
	}

	private UITextLabel addTextLabel(String name, String txt, Vector3f pos, Alignment alignment,
			UIInteractRunnable run) {
		return (UITextLabel) scene.addEntity(name, new UITextLabel(cache, name, txt, pos, alignment, run));
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
			startTransition(MI_MAIN, mainVerticalIndex == VI_MAIN_PLAY ? MI_PLAY
					: (mainVerticalIndex == VI_MAIN_OPTIONS ? MI_OPTIONS : MI_NONE));
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
			mainVerticalIndex = org.joml.Math.clamp(0, 2, mainVerticalIndex);
		} else if (menuIndex == MI_OPTIONS) {
			otherVerticalIndex = org.joml.Math.clamp(0, 1, otherVerticalIndex);
		} else if (menuIndex == MI_PLAY) {
			otherVerticalIndex = org.joml.Math.clamp(0, 1, otherVerticalIndex);
		}
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
				((UISliderEntity) entity).update();
			} else if (entity instanceof UITextLabel) { // interaction button
				((UITextLabel) entity).interact(direction, dir, button);
			}
		} else if(!Button.NONE.equals(button)){ // button
		}
	}

	private Entity getSelectedEntity() {
		if (menuIndex == MI_MAIN) {
			return entitiesMainMenu[mainVerticalIndex];
		} else if (menuIndex == MI_OPTIONS) {
			return entitiesOptionMenu[otherVerticalIndex];
		} else if (menuIndex == MI_PLAY) {
			return entitiesPlayMenu[otherVerticalIndex];
		}
		return null;
	}

	private void setFGColor(Entity entity, Vector4f color) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			if (entity.hasComponent(TextEmitterComponent.class)) {
				TextMaterial mat = ((TextMaterial) entity.getComponent(TextEmitterComponent.class).getTextEmitter(cache)
						.getInstances().getParticleMesh().getMaterial());
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
			setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_START, MAIN_X_POS_END,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
		} else if (menuTransitionTarget == MI_MAIN) {
			setPos(entitiesMainMenu, entitiesMainMenupos, MAIN_X_POS_END, MAIN_X_POS_START,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
		}

		if (menuTransitionBase == MI_PLAY) {
			setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_START, OTHER_X_POS_END,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
		} else if (menuTransitionTarget == MI_PLAY) {
			setPos(entitiesPlayMenu, entitiesPlayMenupos, OTHER_X_POS_END, OTHER_X_POS_START,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
		}

		if (menuTransitionBase == MI_OPTIONS) {
			setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_START, OTHER_X_POS_END,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
			updateLanguge();
		} else if (menuTransitionTarget == MI_OPTIONS) {
			setPos(entitiesOptionMenu, entitiesOptionMenupos, OTHER_X_POS_END, OTHER_X_POS_START,
					i -> Interpolators.QUINT_IN_OUT.evaluate(menuTransitionValue*(i+1)));
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
		String langName = OPTION_MENU_TEXTS[0];
		try {
			GlobalLang.load(langName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MAIN_MENU_TEXTS = new String[] { GlobalLang.get("menu.main.play"), GlobalLang.get("menu.main.options"),
				GlobalLang.get("menu.main.quit") };

		PLAY_MENU_TEXTS = new String[] { GlobalLang.get("menu.play.evolution"), GlobalLang.get("menu.play.infection") };

		OPTION_MENU_TEXTS = new String[] { langName, GlobalLang.get("menu.options.volume") };
	}

	private void setPos(Entity[] entities, Vector3f[] offset, float start, float end, Function<Integer, Float> intFun) {
		for (int i = 0; i < entities.length; i++) {
			Entity te = entities[i];
			Transform3D transform = te.getComponent(Transform3DComponent.class).getTransform();
			if (te instanceof UITextButton) {
				((UITextButton) te).setPosition(new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))),
						transform.getTranslation().y, transform.getTranslation().z));
			} else {
				transform.setTranslation(
						new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))) + offset[i].x,
								offset[i].y, offset[i].z))
						.updateMatrix();
				/*if (te.hasComponent(SubEntitiesComponent.class)) {
					transform = te.getComponent(SubEntitiesComponent.class).getEntities().get(0)
							.getComponent(Transform3DComponent.class).getTransform();
					transform.setTranslation(
							new Vector3f(Math.lerp(start, end, Math.clamp(0, 1, intFun.apply(i))) + offset[i].x + 1.5f,
									offset[i].y, offset[i].z))
							.updateMatrix();
				}*/
			}
		}
	}

	private void chooseMenuElement(Entity[] entities, String[] texts, int verticalIndex) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			for (int i = 0; i < entities.length; i++) {
				Entity te = entities[i];

				if (te.hasComponent(TextEmitterComponent.class)) {
					TextMaterial mat = ((TextMaterial) te.getComponent(TextEmitterComponent.class).getTextEmitter(cache)
							.getInstances().getParticleMesh().getMaterial());
					if (i == verticalIndex) {
						te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(">" + texts[i] + "<")
								.updateText();
						mat.setFgColor(GlobalConsts.HIGHLIGHT);
					} else {
						te.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(texts[i])
								.updateText();
						mat.setFgColor(GlobalConsts.NEUTRAL);
					}
				}
			}
			return null;
		}).push();
	}

}
