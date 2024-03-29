package lu.kbra.gamebox.client.es.game.game;

import org.joml.Vector2i;

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
import lu.kbra.gamebox.client.es.game.game.scenes.WorldScene3D;

public class GameBoxES extends GameLogic {
	
	private Compositor compositor;
	
	private Material generateRenderLayerMaterial;
	
	private WorldScene3D worldScene;
	
	@Override
	public void init(GameEngine e) {
		registerRenderers();
		
		loadWorldScene(null);
		
		compositor = new Compositor();
		compositor.setResize(false);
		compositor.setOutResolution(new Vector2i(400, 640));
		
		// compositor.addRenderLayer(0, new GenerateRenderLayer("bg", generateRenderLayerMaterial));
		compositor.addRenderLayer(0, new SceneRenderLayer("worldScene", worldScene));
		
	}

	private void loadWorldScene(String path) {
		worldScene = new WorldScene3D(path);
		worldScene.setupScene();
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

	}

}
