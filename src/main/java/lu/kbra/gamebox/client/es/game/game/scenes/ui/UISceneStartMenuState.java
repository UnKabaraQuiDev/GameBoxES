package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.VerticesAnimationComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UISceneStartMenuState extends UISceneState {
	
	public static final Vector4f HIGHLIGHT_COLOR = new Vector4f(0.1f, 1f, 1f, 1f), IDLE_COLOR = new Vector4f(1);
	
	private static final float X_POS_START = 0f;
	private static final float X_POS_END = -1f;

	private static final String TEXT_PLAY = "PLAY !", TEXT_OPTIONS = "OPTIONS", TEXT_QUIT = "QUIT";
	private static final String[] TEXTS = { TEXT_PLAY, TEXT_OPTIONS, TEXT_QUIT };
	
	private Entity[] entities1;
	private Entity play, options, quit;
	private Entity mode1, mode2;

	private ControllerInputWatcher cic = new ControllerInputWatcher();

	public UISceneStartMenuState(UIScene3D scene) {
		super(scene);

		play = addText("playText", 10, TEXT_PLAY, new Vector3f(0, 0.8f, 0));
		options = addText("optionsText", 10, TEXT_OPTIONS, new Vector3f(0, 0, 0));
		quit = addText("quitText", 10, TEXT_QUIT, new Vector3f(0, -0.8f, 0));

		entities1 = new Entity[] { play, options, quit };
		
		System.out.println("texts: "+Arrays.toString(TEXTS));
		System.out.println("entit: "+Arrays.toString(entities1));
		
		chooseElement();
	}

	private Entity addText(String name, int length, String txt, Vector3f pos) {
		TextMaterial mat = new TextMaterial("TextMaterial-"+GameBoxES.TEXT_TEXTURE+"-"+name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GameBoxES.TEXT_TEXTURE));
		cache.addMaterial(mat);
		TextEmitter text = new TextEmitter(name, mat, length, txt, new Vector2f(0.5f));
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
		if (!window.updateGamepad(0))
			return;

		GLFWGamepadState gps = window.getGamepad();

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

			case EST:
			case WEST:
				return;

			default:
				return;
			}
		}

		verticalIndex = org.joml.Math.clamp(0, 2, verticalIndex);
		chooseElement();

	}

	private void chooseElement() {
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

	private void placeElements(float playMenu) {

	}

}
