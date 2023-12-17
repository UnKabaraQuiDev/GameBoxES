package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.InstanceEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextModelComponent;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene2D;

public class Scene2DRenderer extends Renderer<GameEngine, Scene2D> {

	public Scene2DRenderer() {
		super(Scene2D.class);
	}

	@Override
	public void render(CacheManager cache, GameEngine engine, Scene2D scene) {
		GlobalLogger.log(Level.INFO, "Scene2D : " + scene.getId());

		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		ModelRenderer modelRenderer = (ModelRenderer) cache.getRenderer(Model.NAME);
		GizmoModelRenderer gizmoModelRenderer = (GizmoModelRenderer) cache.getRenderer(GizmoModel.NAME);
		GizmoRenderer gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		TextModelRenderer textModelRenderer = (TextModelRenderer) cache.getRenderer(TextModel.NAME);
		InstanceEmitterRenderer instanceEmitterRenderer = (InstanceEmitterRenderer) cache
				.getRenderer(InstanceEmitter.NAME);
		InstanceEmitterModelRenderer instanceEmitterModelRenderer = (InstanceEmitterModelRenderer) cache
				.getRenderer(InstanceEmitterModel.NAME);

		for (Entity e : scene.getEntities().values()) {
			if (!e.isActive())
				continue;

			Component c = null;
			if ((c = e.getComponent(ModelComponent.class)) != null) {
				modelRenderer.render(cache, scene, (ModelComponent) c);
			} else if ((c = e.getComponent(MeshComponent.class)) != null) {
				meshRenderer.render(cache, scene, (MeshComponent) c);
			} else if ((c = e.getComponent(GizmoModelComponent.class)) != null) {
				gizmoModelRenderer.render(cache, scene, (GizmoModelComponent) c);
			} else if ((c = e.getComponent(GizmoComponent.class)) != null) {
				gizmoRenderer.render(cache, scene, (GizmoComponent) c);
			} else if ((c = e.getComponent(InstanceEmitterModelComponent.class)) != null) {
				instanceEmitterModelRenderer.render(cache, scene, (InstanceEmitterModelComponent) c);
			} else if ((c = e.getComponent(InstanceEmitterComponent.class)) != null) {
				instanceEmitterRenderer.render(cache, scene, (InstanceEmitterComponent) c);
			} else if ((c = e.getComponent(TextModelComponent.class)) != null) {
				textModelRenderer.render(cache, scene, (TextModelComponent) c);
			}
		}
	};

}
