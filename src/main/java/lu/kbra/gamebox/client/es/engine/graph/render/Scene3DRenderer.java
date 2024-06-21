package lu.kbra.gamebox.client.es.engine.graph.render;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderConditionComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalOptions;

public class Scene3DRenderer extends Renderer<GameEngine, Scene3D> {

	private MeshRenderer meshRenderer;
	private GizmoRenderer gizmoRenderer;
	private InstanceEmitterRenderer instanceEmitterRenderer;
	private TextEmitterRenderer textEmitterRenderer;

	public Scene3DRenderer() {
		super(Scene3D.class);
	}

	private static final Comparator<Entry<String, Entity>> COMPARATOR = (a, b) -> {
		return Float.compare(a.getValue().hasComponent(RenderComponent.class) ? a.getValue().getComponent(RenderComponent.class).getPriority() : 0,
				b.getValue().hasComponent(RenderComponent.class) ? b.getValue().getComponent(RenderComponent.class).getPriority() : 0);
	};

	private static final Comparator<Entity> COMPARATOR_ENTITY = (a, b) -> {
		return Float.compare(a.hasComponent(RenderComponent.class) ? a.getComponent(RenderComponent.class).getPriority() : 0, b.hasComponent(RenderComponent.class) ? b.getComponent(RenderComponent.class).getPriority() : 0);
	};

	@Override
	public void render_in(CacheManager cache, GameEngine ge, Scene3D scene) {
		GlobalLogger.log(Level.FINE, "Scene3D : " + scene.getId());

		GameEngine.DEBUG.start("r_scene3d");

		meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		instanceEmitterRenderer = (InstanceEmitterRenderer) cache.getRenderer(InstanceEmitter.NAME);
		textEmitterRenderer = (TextEmitterRenderer) cache.getRenderer(TextEmitter.NAME);

		GameEngine.DEBUG.start("r_sort");
		LinkedHashMap<String, Entity> sortedMap = scene.getEntities().entrySet().stream().sorted(COMPARATOR).collect(LinkedHashMap::new, (linkedHashMap, entry) -> linkedHashMap.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
		scene.setEntities(sortedMap);
		// System.err.println("Render order:");
		// sortedMap.values().forEach(System.err::println);
		GameEngine.DEBUG.end("r_sort");

		GameEngine.DEBUG.start("r_for");
		Collection<Entity> ce = scene.getEntities().values();
		ce.forEach((e) -> {
			GameEngine.DEBUG.start("r_for_single");
			if (!e.isActive()) {
				return;
			}

			/*
			 * if(e.hasComponent(RenderConditionComponent.class)) { System.out.println(e+" = "+e.getComponent(RenderConditionComponent.class).get()); }
			 */

			if (e.hasComponent(RenderConditionComponent.class) && !e.getComponent(RenderConditionComponent.class).get() && GlobalOptions.CULLING_OPTIMISATION) {
				return;
			}

			render(cache, scene, e);

			GameEngine.DEBUG.end("r_for_single");
		});
		GameEngine.DEBUG.end("r_for");

		GameEngine.DEBUG.end("r_scene3d");
	}

	private void render(CacheManager cache, Scene3D scene, Entity e) {
		if (meshRenderer != null && e.hasComponent(MeshComponent.class)) {
			meshRenderer.render(cache, scene, (MeshComponent) e.getComponent(MeshComponent.class));
		}
		if (gizmoRenderer != null && e.hasComponent(GizmoComponent.class)) {
			gizmoRenderer.render(cache, scene, (GizmoComponent) e.getComponent(GizmoComponent.class));
		}
		if (instanceEmitterRenderer != null && e.hasComponent(InstanceEmitterComponent.class)) {
			instanceEmitterRenderer.render(cache, scene, (InstanceEmitterComponent) e.getComponent(InstanceEmitterComponent.class));
		}
		if (textEmitterRenderer != null && e.hasComponent(TextEmitterComponent.class)) {
			textEmitterRenderer.render(cache, scene, (TextEmitterComponent) e.getComponent(TextEmitterComponent.class));
		}

		if (e.hasComponent(SubEntitiesComponent.class)) {
			e.getComponent(SubEntitiesComponent.class).getEntities().sort(COMPARATOR_ENTITY);
			for (Entity en : e.getComponent(SubEntitiesComponent.class).getEntities()) {
				if (!en.isActive()) {
					return;
				}

				render(cache, scene, en);
			}
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
