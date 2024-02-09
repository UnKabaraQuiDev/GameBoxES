package lu.pcy113.pdr.engine.graph.render;

import java.util.Collection;
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
		
		GameEngine.DEBUG.start("r_scene3d");
		
		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		GizmoRenderer gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		InstanceEmitterRenderer instanceEmitterRenderer = (InstanceEmitterRenderer) cache.getRenderer(InstanceEmitter.NAME);
		TextEmitterRenderer textEmitterRenderer = (TextEmitterRenderer) cache.getRenderer(TextEmitter.NAME);
		
		GameEngine.DEBUG.start("r_sort");
		LinkedHashMap<String, Entity> sortedMap = scene.getEntities().entrySet()
				.stream()
				.sorted(COMPARATOR)
				.collect(LinkedHashMap::new,
						(linkedHashMap, entry) -> linkedHashMap.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
		scene.setEntities(sortedMap);
		GameEngine.DEBUG.end("r_sort");
		
		GameEngine.DEBUG.start("r_for");
		Collection<Entity> ce = scene.getEntities().values();
		//for (Entity e : ce) {
		ce.forEach((e)-> {
			GameEngine.DEBUG.start("r_for_single");
			if (!e.isActive()) {
				return;
			}
			
			//Component c = null;
			if (e.hasComponent(MeshComponent.class)) {
				meshRenderer.render(cache, scene, (MeshComponent) e.getComponent(MeshComponent.class));
			} else if (e.hasComponent(GizmoComponent.class)) {
				gizmoRenderer.render(cache, scene, (GizmoComponent) e.getComponent(GizmoComponent.class));
			} else if (e.hasComponent(InstanceEmitterComponent.class)) {
				instanceEmitterRenderer.render(cache, scene, (InstanceEmitterComponent) e.getComponent(InstanceEmitterComponent.class));
			} else if (e.hasComponent(TextEmitterComponent.class)) {
				textEmitterRenderer.render(cache, scene, (TextEmitterComponent) e.getComponent(TextEmitterComponent.class));
			}
			GameEngine.DEBUG.end("r_for_single");
		});
		GameEngine.DEBUG.end("r_for");
		
		GameEngine.DEBUG.end("r_scene3d");
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
