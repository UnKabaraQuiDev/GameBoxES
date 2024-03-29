package lu.kbra.gamebox.client.es.game.game;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.composition.Compositor;
import lu.kbra.gamebox.client.es.engine.graph.composition.SceneRenderLayer;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.render.GizmoRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.InstanceEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.MeshRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene2DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene3DRenderer;
import lu.kbra.gamebox.client.es.engine.impl.GameLogic;
import lu.kbra.gamebox.client.es.game.game.scenes.UIScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.WorldScene3D;

public class GameBoxES extends GameLogic {

	private Compositor compositor;

	private Material generateRenderLayerMaterial;
	
	private UIScene3D uiScene;
	private WorldScene3D worldScene;
	
	private SceneRenderLayer worldSceneRenderLayer;
	private SceneRenderLayer uiSceneRenderLayer;

	@Override
	public void init(GameEngine e) {
		registerRenderers();

		loadWorldScene("not world");
		loadUiScene("not ui");

		compositor = new Compositor();

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
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		
	}

	@Override
	public void render(float dTime) {
		compositor.render(worldScene.getCache(), engine);
	}

}
