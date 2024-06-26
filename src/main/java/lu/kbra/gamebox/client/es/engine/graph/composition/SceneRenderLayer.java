package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.function.Supplier;
import java.util.logging.Level;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.render.Renderer;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene2D;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.pcy113.pclib.logger.GlobalLogger;

public class SceneRenderLayer extends RenderLayer<GameEngine, Framebuffer, Scene> {
	
	private Supplier<CacheManager> cache;
	
	public SceneRenderLayer(String name, Scene target, Supplier<CacheManager> cache) {
		super(name, target);
		this.cache = cache;
	}

	@Override
	public void render(GameEngine parent, Framebuffer fb) {
		CacheManager cc = cache.get();
		
		Renderer<GameEngine, Scene> renderer = null;
		if (this.target instanceof Scene3D) {
			renderer = (Renderer<GameEngine, Scene>) cc.getRenderer(Scene3D.NAME);
			if (renderer == null) {
				GlobalLogger.log(Level.SEVERE, "No renderer found for: " + Scene3D.NAME);
				return;
			}
		} else if (this.target instanceof Scene2D) {
			renderer = (Renderer<GameEngine, Scene>) cc.getRenderer(Scene2D.NAME);
			if (renderer == null) {
				GlobalLogger.log(Level.SEVERE, "No renderer found for: " + Scene2D.NAME);
				return;
			}
		}

		if (renderer != null) {
			renderer.render(cc, parent, this.target);
		}
	}

}
