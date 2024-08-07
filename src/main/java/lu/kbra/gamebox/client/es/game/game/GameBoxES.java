package lu.kbra.gamebox.client.es.game.game;

import java.io.IOException;
import java.util.logging.Level;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.data.PlayerData;
import lu.kbra.gamebox.client.es.game.game.debug.DebugUIElements;
import lu.kbra.gamebox.client.es.game.game.render.compositing.AdvancedCompositor;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.WorldScene3D;
import lu.kbra.gamebox.client.es.game.game.utils.GameState;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
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
		GlobalUtils.init(this, super.engine);

		GlobalLogger.getLogger().setMinForwardLevel(Level.INFO);

		GlobalUtils.registerRenderers();
		GlobalUtils.registerCodecs();

		GameEngine.DEBUG.wireframe = GlobalOptions.DEBUG && GlobalOptions.WIREFRAME;
		GameEngine.DEBUG.gizmos = GlobalOptions.DEBUG && GlobalOptions.GIZMOS;
		GlobalLogger.getLogger().setMinForwardLevel(Level.parse(GlobalOptions.LOGS_MIN_FORWARD_LEVEL));
		GlobalLogger.getLogger().setForwardContent(GlobalOptions.LOGS_FORWARD);

		cache.loadOrGetMaterial(TextShader.TextMaterial.NAME, TextShader.TextMaterial.class, cache.loadOrGetSingleTexture(GlobalConsts.TEXT_TEXTURE_NAME, GlobalConsts.TEXT_TEXTURE_PATH, TextureFilter.NEAREST));

		loadWorldScene("world1");
		loadUiScene("ui");

		if (GlobalOptions.DEBUG) {
			debug = new DebugUIElements(cache, engine, uiScene, new Vector3f(-4f, 1.5f, 0), new Quaternionf());
		}
		
		compositor = new AdvancedCompositor();

		worldSceneRenderLayer = (SceneRenderLayer) new SceneRenderLayer("worldScene", worldScene, () -> worldScene.getWorld() != null ? worldScene.getWorld().getCache() : worldScene.getCache());
		worldSceneRenderLayer.setVisible(false);
		cache.addRenderLayer(worldSceneRenderLayer);
		compositor.addRenderLayer(0, worldSceneRenderLayer);

		uiSceneRenderLayer = new SceneRenderLayer("uiScene", uiScene, () -> uiScene.getState() != null ? uiScene.getState().getCache() : uiScene.getCache());
		cache.addRenderLayer(uiSceneRenderLayer);
		compositor.addRenderLayer(1, uiSceneRenderLayer);

		engine.getWindow().onResize((w, h) -> {
			GlobalLogger.info("Resized to: " + w + ":" + h);
			worldScene.getCamera().getProjection().update(w, h);
			/* uiScene.getCamera().getProjection().update(w, h); */
		});

		// GlobalUtils.setFixedRatio(worldScene.getCamera());
		GlobalUtils.setFixedRatio(uiScene.getCamera());

		engine.getWindow().setBackground(new Vector4f(0.0f));

		GlobalUtils.compileMeshes(cache);

		cache.dump(GlobalLogger.getLogger().getFileWriter());
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
	}

	@Override
	public void update(float dTime) {
		if (GameState.PLAYING.equals(gameState)) {
			worldScene.update(dTime);
		}
		uiScene.update(dTime);
	}

	@Override
	public void render(float dTime) {
		if (debug != null) {
			debug.update();
		}
		if (worldScene != null) {
			worldScene.render(dTime);
		}
		uiScene.render(dTime);
		compositor.render(engine);
	}

	public void startGame() {
		GlobalLogger.info("Starting game");
		gameState = GameState.LOADING;
		playerData = new PlayerData();
		uiScene.clearMainMenu();

		GlobalUtils.pushRender(() -> {
			GlobalUtils.enableWorkers();

			uiScene.setupGame();
			uiScene.showUpgradeTree(false);
			worldScene.setupGame();
			worldSceneRenderLayer.setVisible(true);

			gameState = GameState.PLAYING;
			playerData.startMarkCount();
		});
	}

}
