package lu.pcy113.pdr.engine.graph.render;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class Scene3DRenderer extends Renderer<GameEngine, Scene3D> {
	
	public Scene3DRenderer() {
		super(Scene3D.class);
	}

	@Override
	public void render(CacheManager cache, GameEngine ge, Scene3D scene) {
		System.out.println("Scene3D : "+scene.getId());
		
		MeshRenderer meshRenderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		for(String meshId : scene.getMeshes()) {
			Mesh mesh = cache.getMesh(meshId);
			if(mesh != null)
				meshRenderer.render(cache, scene, mesh);
		}
		
		ModelRenderer modelRenderer = (ModelRenderer) cache.getRenderer(Model.NAME);
		for(String modelId : scene.getModels()) {
			Model model = cache.getModel(modelId);
			if(model != null)
				modelRenderer.render(cache, scene, model);
		}
		
		GizmoModelRenderer gizmoModelRenderer = (GizmoModelRenderer) cache.getRenderer(GizmoModel.NAME);
		for(String gizmoId : scene.getGizmoModels()) {
			GizmoModel gizmo = cache.getGizmoModel(gizmoId);
			if(gizmo != null)
				gizmoModelRenderer.render(cache, scene, gizmo);
		}
	}
	
	@Override
	public void cleanup() {
		
	}
	
}
