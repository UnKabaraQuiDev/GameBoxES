package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.ParticleEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoComponent;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ParticleEmitterComponent;
import lu.pcy113.pdr.engine.objs.entity.components.ParticleEmitterModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextModelComponent;
import lu.pcy113.pdr.engine.objs.text.TextModel;
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
		TextModelRenderer textModelRenderer = (TextModelRenderer) cache.getRenderer(TextModel.NAME);
		ParticleEmitterModelRenderer particleEmitterModelRenderer = (ParticleEmitterModelRenderer) cache.getRenderer(ParticleEmitterModel.NAME);
		
		for(Entity e : scene.getEntities().values()) {
			if(!e.isActive()) {
				System.out.println(e.getComponents().values()+" is not active");
				continue;
			}else {
				System.out.println(e.getComponents().values()+" is active");
			}
			
			Component c = null;
			if((c = e.getComponent(ModelComponent.class)) != null) {
				modelRenderer.render(cache, scene, (ModelComponent) c);
			} else if((c = e.getComponent(MeshComponent.class)) != null) {
				meshRenderer.render(cache, scene, (MeshComponent) c);
			}
			if((c = e.getComponent(GizmoModelComponent.class)) != null) {
				gizmoModelRenderer.render(cache, scene, (GizmoModelComponent) c);
			}else if((c = e.getComponent(GizmoComponent.class)) != null) {
				gizmoRenderer.render(cache, scene, (GizmoComponent) c);
			}
			if((c = e.getComponent(ParticleEmitterModelComponent.class)) != null) {
				particleEmitterModelRenderer.render(cache, scene, (ParticleEmitterModelComponent) c);
			}else if((c = e.getComponent(ParticleEmitterComponent.class)) != null) {
				//particleEmitterRenderer.render(cache, scene, (GizmoComponent) c);
			}
			if((c = e.getComponent(TextModelComponent.class)) != null) {
				System.err.println(c);
				textModelRenderer.render(cache, scene, (TextModelComponent) c);
			}
		}
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
}
