package lu.pcy113.pdr.engine.graph.composition;

import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.render.Renderer;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene2D;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class SceneRenderLayer
		extends
		RenderLayer<GameEngine, Scene> {

	public SceneRenderLayer(String name, Scene target) {
		super(name, target);
	}

	@Override
	public void render(CacheManager cache, GameEngine parent) {
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

		if (renderer != null) renderer.render(cache, parent, this.target);
	}

}
