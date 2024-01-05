package lu.pcy113.pdr.engine.graph.render;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
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
import lu.pcy113.pdr.engine.objs.entity.components.RenderComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextEmitterComponent;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class Scene3DRenderer extends Renderer<GameEngine, Scene3D> {

	public Scene3DRenderer() {
		super(Scene3D.class);
	}
	
	private static final Comparator<Entry<String, Entity>> COMPARATOR = (a, b) -> {
		return !a.getValue().hasComponent(RenderComponent.class) ? -1 : (!b.getValue().hasComponent(RenderComponent.class) ? 1 : 
			(b.getValue().getComponent(RenderComponent.class).getPriority() - a.getValue().getComponent(RenderComponent.class).getPriority())
		);
	};
	
	@Override
	public void render(CacheManager cache, GameEngine ge, Scene3D scene) {
		GlobalLogger.log(Level.INFO, "Scene3D : " + scene.getId());

		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		GizmoRenderer gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		InstanceEmitterRenderer instanceEmitterRenderer = (InstanceEmitterRenderer) cache.getRenderer(InstanceEmitter.NAME);
		TextEmitterRenderer textEmitterRenderer = (TextEmitterRenderer) cache.getRenderer(TextEmitter.NAME);
		
		LinkedHashMap<String, Entity> sortedMap = scene.getEntities().entrySet()
				.stream()
				.sorted(COMPARATOR)
				.collect(LinkedHashMap::new,
						(linkedHashMap, entry) -> linkedHashMap.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
		scene.setEntities(sortedMap);
		
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
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
