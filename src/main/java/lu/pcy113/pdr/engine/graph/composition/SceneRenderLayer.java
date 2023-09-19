package lu.pcy113.pdr.engine.graph.composition;

import java.util.logging.Level;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.render.Renderer;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class SceneRenderLayer extends RenderLayer<GameEngine, Scene> {
	
	public SceneRenderLayer(String name, Scene target) {
		super(name, target);
	}
	
	@Override
	public void render(CacheManager cache, GameEngine parent) {
		Renderer<GameEngine, Scene> renderer = null;
		if(target instanceof Scene3D) {
			renderer = (Renderer<GameEngine, Scene>) cache.getRenderer(Scene3D.NAME);
			if(renderer == null) {
				Logger.log(Level.SEVERE, "No renderer found for: "+Scene3D.NAME);
				return;
			}
		}
		
		if(renderer != null)
			renderer.render(cache, parent, (Scene3D) target);
	}
	
}
