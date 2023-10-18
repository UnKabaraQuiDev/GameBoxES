package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class Scene3DRenderer extends Renderer<GameEngine, Scene3D> {
	
	public Scene3DRenderer() {
		super(Scene3D.class);
	}

	@Override
	public void render(CacheManager cache, GameEngine ge, Scene3D scene) {
		Logger.log(Level.INFO, "Scene3D : "+scene.getId());
		
		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		ModelRenderer modelRenderer = (ModelRenderer) cache.getRenderer(Model.NAME);
		GizmoModelRenderer gizmoModelRenderer = (GizmoModelRenderer) cache.getRenderer(GizmoModel.NAME);
		GizmoRenderer gizmoRenderer = (GizmoRenderer) cache.getRenderer(Gizmo.NAME);
		
		for(Entity e : scene.getEntities().values()) {
			Component c = null;
			if((c = e.getComponent(ModelComponent.class)) != null) {
				modelRenderer.render(cache, scene, (ModelComponent) c);
			} else if((c = e.getComponent(MeshComponent.class)) != null) {
				meshRenderer.render(cache, scene, ((MeshComponent) c).getMesh(cache));
			}
			if((c = e.getComponent(GizmoModelComponent.class)) != null) {
				gizmoModelRenderer.render(cache, scene, ((GizmoModelComponent) c).getGizmoModel(cache));
			}else if((c = e.getComponent(GizmoComponent.class)) != null) {
				gizmoRenderer.render(cache, scene, ((GizmoComponent) c).getGizmo(cache));
			}
		}
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
}
