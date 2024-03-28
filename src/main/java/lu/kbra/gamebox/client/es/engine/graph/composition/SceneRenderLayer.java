package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.render.Renderer;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene2D;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;

public class SceneRenderLayer extends RenderLayer<GameEngine, Framebuffer, Scene> {

	public SceneRenderLayer(String name, Scene target) {
		super(name, target);
	}

	@Override
	public void render(CacheManager cache, GameEngine parent, Framebuffer fb) {
		Renderer<GameEngine, Scene> renderer = null;
		if (this.target instanceof Scene3D) {
			renderer = (Renderer<GameEngine, Scene>) cache.getRenderer(Scene3D.NAME);
			if (renderer == null) {
				GlobalLogger.log(Level.SEVERE, "No renderer found for: " + Scene3D.NAME);
				return;
			}
		} else if (this.target instanceof Scene2D) {
			renderer = (Renderer<GameEngine, Scene>) cache.getRenderer(Scene2D.NAME);
			if (renderer == null) {
				GlobalLogger.log(Level.SEVERE, "No renderer found for: " + Scene2D.NAME);
				return;
			}
		}

		if (renderer != null)
			renderer.render(cache, parent, this.target);
	}

}
