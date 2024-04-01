package lu.kbra.gamebox.client.es.game.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.game.game.debug.DebugUIElements;
import lu.kbra.gamebox.client.es.game.game.options.GameOptions;
import lu.kbra.gamebox.client.es.game.game.render.compositing.AdvancedCompositor;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.WorldScene3D;
import lu.kbra.gamebox.client.es.game.game.utils.GameState;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class GameBoxES extends GameLogic {

	public DebugUIElements debug;
	
	public GameOptions gameOptions;
	
	public AdvancedCompositor compositor;

	public UIScene3D uiScene;
	public WorldScene3D worldScene;

	public SceneRenderLayer worldSceneRenderLayer;
	public SceneRenderLayer uiSceneRenderLayer;
	
	public GameState gameState = GameState.START_MENU;

	@Override
	public void init(GameEngine e) {
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.gizmos = true;
		
		GlobalUtils.init(this);
		
		// GlobalLogger.getLogger().setForwardContent(false);

		GlobalUtils.registerRenderers();
		
		cache.loadOrGetMaterial(TextShader.TextMaterial.NAME, TextShader.TextMaterial.class, cache.loadOrGetSingleTexture("text-30px", "./resources/textures/fonts/font1row.png"));

		loadWorldScene("not world");
		loadUiScene("not ui");

		debug = new DebugUIElements(cache, engine, uiScene, new Vector3f(-4f, 1.5f, 0), new Quaternionf());

		compositor = new AdvancedCompositor();

		worldSceneRenderLayer = new SceneRenderLayer("worldScene", worldScene, worldScene.getCache());
		cache.addRenderLayer(worldSceneRenderLayer);
		compositor.addRenderLayer(0, worldSceneRenderLayer);

		uiSceneRenderLayer = new SceneRenderLayer("uiScene", uiScene, uiScene.getCache());
		cache.addRenderLayer(uiSceneRenderLayer);
		compositor.addRenderLayer(1, uiSceneRenderLayer);

		engine.getWindow().onResize((w, h) -> {
			System.out.println("resize update: " + w + "x" + h);
			worldScene.getCamera().getProjection().update(w, h);
			uiScene.getCamera().getProjection().update(w, h);
		});
		engine.getWindow().setBackground(new Vector4f(0.1f));

		cache.dump(System.err);
	}

	private void loadUiScene(String path) {
		uiScene = new UIScene3D(path, cache, window);
		uiScene.setupScene();
		uiScene.setupStartMenu();
		cache.addScene(uiScene);
	}

	private void loadWorldScene(String path) {
		worldScene = new WorldScene3D(path, cache, window);
		worldScene.setupScene();
		cache.addScene(worldScene);
	}

	@Override
	public void input(float dTime) {
		if(GameState.START_MENU.equals(gameState)) {
			uiScene.input(dTime);
		}else {
			// worldScene.input(dTime);
		}
		worldScene.input(dTime);
	}

	@Override
	public void update(float dTime) {
		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			GlobalUtils.projectUI(System.err::println);
		}
	}
	
	@Override
	public void render(float dTime) {
		if (debug != null) {
			debug.update();
		}
		compositor.render(worldScene.getCache(), engine);
	}

}
