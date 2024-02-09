package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextEmitterComponent;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene2D;

public class Scene2DRenderer extends Renderer<GameEngine, Scene2D> {

	public Scene2DRenderer() {
		super(Scene2D.class);
	}

	@Override
	public void render(CacheManager cache, GameEngine engine, Scene2D scene) {
		GlobalLogger.log(Level.INFO, "Scene2D : " + scene.getId());
		
		GameEngine.DEBUG.start("r_scene2d");
		
		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		GizmoRenderer gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		InstanceEmitterRenderer instanceEmitterRenderer = (InstanceEmitterRenderer) cache.getRenderer(InstanceEmitter.NAME);
		TextEmitterRenderer textEmitterRenderer = (TextEmitterRenderer) cache.getRenderer(TextEmitter.NAME);

		GameEngine.DEBUG.start("r_for");
		for (Entity e : scene.getEntities().values()) {
			if (!e.isActive())
				continue;

			Component c = null;
			if ((c = e.getComponent(MeshComponent.class)) != null) {
				meshRenderer.render(cache, scene, (MeshComponent) c);
			} else if ((c = e.getComponent(GizmoComponent.class)) != null) {
				gizmoRenderer.render(cache, scene, (GizmoComponent) c);
			} else if ((c = e.getComponent(InstanceEmitterComponent.class)) != null) {
				instanceEmitterRenderer.render(cache, scene, (InstanceEmitterComponent) c);
			} else if ((c = e.getComponent(TextEmitterComponent.class)) != null) {
				textEmitterRenderer.render(cache, scene, (TextEmitterComponent) c);
			}
		}
		GameEngine.DEBUG.end("r_for");
		
		GameEngine.DEBUG.end("r_scene2d");
	}

}
