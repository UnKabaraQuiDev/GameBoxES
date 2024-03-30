package lu.kbra.gamebox.client.es.game.game;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL40;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.render.GizmoRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.InstanceEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.MeshRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene2DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene3DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.TextEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.geo.Ray;
import lu.kbra.gamebox.client.es.game.game.debug.DebugUIElements;
import lu.kbra.gamebox.client.es.game.game.scenes.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.WorldScene3D;

public class GameBoxES extends GameLogic {

	private DebugUIElements debug;

	private AdvancedCompositor compositor;

	private UIScene3D uiScene;
	private WorldScene3D worldScene;

	private SceneRenderLayer worldSceneRenderLayer;
	private SceneRenderLayer uiSceneRenderLayer;

	@Override
	public void init(GameEngine e) {
		GameEngine.DEBUG.wireframe = false;
		GameEngine.DEBUG.gizmos = false;

		// GlobalLogger.getLogger().setForwardContent(false);

		registerRenderers();

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

	private void registerRenderers() {
		cache.addRenderer(new MeshRenderer());
		cache.addRenderer(new GizmoRenderer());
		cache.addRenderer(new InstanceEmitterRenderer());
		cache.addRenderer(new Scene2DRenderer());
		cache.addRenderer(new Scene3DRenderer());
		cache.addRenderer(new TextEmitterRenderer());
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			System.err.println("press");
			int[] viewport = new int[4];
			createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
				GL40.glGetIntegerv(GL40.GL_VIEWPORT, viewport);
				return null;
			}).then((t) -> {
				Ray ray = uiScene.getCamera().projectRay(new Vector2f(window.getMousePos()), viewport);

				Vector3f pos = uiScene.getCamera().projectPlane(ray, GeoPlane.YZ);

				System.err.println(pos);
				return null;
			}).push();
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
