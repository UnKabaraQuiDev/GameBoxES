package lu.pcy113.pdr.engine.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene3D extends Scene {
	
	public static final String NAME = Scene3D.class.getName();
	
	protected List<String> meshes;
	protected List<String> models;
	protected List<String> pointLights;
	protected List<String> gizmoModels;
	
	public Scene3D(String name) {
		super(name, Camera.camera3D());
		
		this.meshes = new ArrayList<>();
		this.models = new ArrayList<>();
		this.pointLights = new ArrayList<>();
		this.gizmoModels = new ArrayList<>();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
	public List<String> getMeshes() {return meshes;}
	public List<String> getModels() {return models;}
	public List<String> getPointLights() {return pointLights;}
	public List<String> getGizmoModels() {return gizmoModels;}
	
	public void addMesh(String mesh) {
		meshes.add(mesh);
	}
	public void addModel(String model) {
		models.add(model);
	}
	public void addPointLight(String id) {
		pointLights.add(id);
	}
	public void addGizmoModel(String id) {
		gizmoModels.add(id);
	}

}
