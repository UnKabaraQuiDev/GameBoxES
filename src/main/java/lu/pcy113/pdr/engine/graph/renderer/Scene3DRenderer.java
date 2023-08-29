package lu.pcy113.pdr.engine.graph.renderer;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.engine.scene.geom.ModelInstance;
import lu.pcy113.pdr.utils.Logger;

public class Scene3DRenderer extends Renderer<Scene3D> {
	
	private Renderer<Model> modelRenderer;
	private Renderer<ModelInstance> instanceRenderer;
	
	public Scene3DRenderer() {
		super();
		
		modelRenderer = new ModelRenderer();
		instanceRenderer = new ModelInstanceRenderer();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
	@Override
	public void render(Window window, Scene3D scene) {
		Logger.log();
		
		super.render(window, scene);
		
		for(Model model : scene.getModels().values()) {
			modelRenderer.render(window, model);
		}
		
		for(ModelInstance instance : scene.getInstances().values()) {
			instanceRenderer.render(window, instance);
		}
		
	}
	
	@Override
	public void attach(Scene obj) {
		super.attach(obj);
		
		modelRenderer.attach(obj);
		instanceRenderer.attach(obj);
	}
	
}
