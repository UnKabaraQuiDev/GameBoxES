package lu.kbra.gamebox.client.es.engine.graph.render;

import java.util.logging.Level;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene2D;
import lu.pcy113.pclib.logger.GlobalLogger;

public class Scene2DRenderer extends Renderer<GameEngine, Scene2D> {

	public Scene2DRenderer() {
		super(Scene2D.class);
	}

	@Override
	public void render_in(CacheManager cache, GameEngine engine, Scene2D scene) {
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
