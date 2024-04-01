package lu.kbra.gamebox.client.es.game.game;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.game.game.debug.DebugUIElements;
import lu.kbra.gamebox.client.es.game.game.scenes.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.WorldScene3D;

public class GameBoxES extends GameLogic {

	DebugUIElements debug;
	
	GameOptions gameOptiong;
	
	AdvancedCompositor compositor;

	UIScene3D uiScene;
	WorldScene3D worldScene;

	SceneRenderLayer worldSceneRenderLayer;
	SceneRenderLayer uiSceneRenderLayer;

	@Override
	public void init(GameEngine e) {
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.gizmos = false;
		
		GlobalUtils.init(this);
		
		// GlobalLogger.getLogger().setForwardContent(false);

		GlobalUtils.registerRenderers();

		loadWorldScene("not world");
		loadUiScene("not ui");

		debug = new DebugUIElements(cache, engine, uiScene, new Vector3f(0, 1.5f, 4), new Quaternionf().rotateX((float) -Math.PI / 2));

		compositor = new AdvancedCompositor();

		worldSceneRenderLayer = new SceneRenderLayer("worldScene", worldScene);
		cache.addRenderLayer(worldSceneRenderLayer);
		compositor.addRenderLayer(0, worldSceneRenderLayer);

		uiSceneRenderLayer = new SceneRenderLayer("uiScene", uiScene);
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
		uiScene = new UIScene3D(path, cache);
		uiScene.setupScene();
		cache.addScene(uiScene);
	}

	private void loadWorldScene(String path) {
		worldScene = new WorldScene3D(path, cache);
		worldScene.setupScene();
		cache.addScene(worldScene);
	}

	@Override
	public void input(float dTime) {
		Camera3D cam = ((Camera3D) worldScene.getCamera());
		cam.getPosition().add(new Vector3f(
				(window.isCharPress('r') ? 1 : 0) - (window.isCharPress('f') ? 1 : 0),
				(window.isCharPress('z') ? 1 : 0) - (window.isCharPress('s') ? 1 : 0),
				(window.isCharPress('d') ? 1 : 0) - (window.isCharPress('q') ? 1 : 0)
		));
		cam.updateMatrix();
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
