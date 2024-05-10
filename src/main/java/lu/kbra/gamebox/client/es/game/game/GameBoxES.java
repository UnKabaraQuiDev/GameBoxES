package lu.kbra.gamebox.client.es.game.game;

import java.io.IOException;
import java.util.logging.Level;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.data.PlayerData;
import lu.kbra.gamebox.client.es.game.game.debug.DebugUIElements;
import lu.kbra.gamebox.client.es.game.game.render.compositing.AdvancedCompositor;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.WorldScene3D;
import lu.kbra.gamebox.client.es.game.game.utils.GameMode;
import lu.kbra.gamebox.client.es.game.game.utils.GameState;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalOptions;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class GameBoxES extends GameLogic {

	public DebugUIElements debug;

	public AdvancedCompositor compositor;

	public UIScene3D uiScene;
	public WorldScene3D worldScene;

	public SceneRenderLayer worldSceneRenderLayer;
	public SceneRenderLayer uiSceneRenderLayer;

	public PlayerData playerData;

	public GameState gameState = GameState.START_MENU;

	@Override
	public void init(GameEngine e) {
		GameEngine.DEBUG.wireframe = true;
		GameEngine.DEBUG.gizmos = true;

		GlobalUtils.init(this, super.engine);

		GlobalLogger.getLogger().setMinForwardLevel(Level.INFO);

		GlobalUtils.registerRenderers();

		try {
			GlobalOptions.load();
			GlobalLogger.log("Loaded lang: " + GlobalOptions.LANGUAGE + " gets: " + GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE]);
			GlobalLang.load(GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE]);
			GlobalLogger.log("Loaded volume: " + GlobalLang.get("menu.options.volume"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		cache.loadOrGetMaterial(TextShader.TextMaterial.NAME, TextShader.TextMaterial.class, cache.loadOrGetSingleTexture(GlobalConsts.TEXT_TEXTURE, "./resources/textures/fonts/font1row.png", TextureFilter.NEAREST));

		loadWorldScene("world1");
		loadUiScene("ui");

		debug = new DebugUIElements(cache, engine, uiScene, new Vector3f(-4f, 1.5f, 0), new Quaternionf());

		compositor = new AdvancedCompositor();

		worldSceneRenderLayer = (SceneRenderLayer) new SceneRenderLayer("worldScene", worldScene, () -> worldScene.getWorld() != null ? worldScene.getWorld().getCache() : worldScene.getCache()).setVisible(false);
		cache.addRenderLayer(worldSceneRenderLayer);
		compositor.addRenderLayer(0, worldSceneRenderLayer);

		uiSceneRenderLayer = new SceneRenderLayer("uiScene", uiScene, () -> uiScene.getState() != null ? uiScene.getState().getCache() : uiScene.getCache());
		cache.addRenderLayer(uiSceneRenderLayer);
		compositor.addRenderLayer(1, uiSceneRenderLayer);

		engine.getWindow().onResize((w, h) -> {
			GlobalLogger.info("Resized to: "+w+":"+h);
			/*worldScene.getCamera().getProjection().update(w, h);
			uiScene.getCamera().getProjection().update(w, h);*/
		});
		
		GlobalUtils.setFixedRatio(worldScene.getCamera());
		GlobalUtils.setFixedRatio(uiScene.getCamera());
		
		engine.getWindow().setBackground(new Vector4f(0.0f));

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

	public void eventStop() {
		try {
			GlobalOptions.save();
			GlobalUtils.requestQuit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void input(float dTime) {
		if (GameState.START_MENU.equals(gameState)) {
			uiScene.input(dTime);
		} else {
			worldScene.input(dTime);
		}

		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			((Camera3D) worldScene.getCamera()).dump(System.err);
		}
	}

	@Override
	public void update(float dTime) {
		if(!GameState.START_MENU.equals(gameState)) {
			worldScene.update(dTime);
		}
		uiScene.update(dTime);
	}

	@Override
	public void render(float dTime) {
		if (debug != null) {
			debug.update();
		}
		compositor.render(engine);
	}

	public void startGame(GameMode mode) {
		GlobalLogger.info("Starting: " + mode);
		gameState = GameState.valueOf("LOADING_" + mode.name());
		playerData = new PlayerData();
		uiScene.clearMainMenu();
		GlobalUtils.pushRender(() -> {
			uiScene.setupGame();
			uiScene.showUpgradeTree(true);
			worldScene.setupGame();
			worldSceneRenderLayer.setVisible(true);
		});
	}

}
