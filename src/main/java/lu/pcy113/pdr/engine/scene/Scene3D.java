package lu.pcy113.pdr.engine.scene;

import java.util.ArrayList;
import java.util.List;

public class Scene3D extends Scene {
	
	public static final String NAME = Scene3D.class.getName();
	
	protected List<String> meshes;
	protected List<String> models;
	
	public Scene3D(String name) {
		super(name, Camera.camera3D());
		
		this.meshes = new ArrayList<>();
		this.models = new ArrayList<>();
	}
	
	@Override
	public void cleanup() {}
	
	public List<String> getMeshes() {return meshes;}
	public List<String> getModels() {return models;}

	public void addMesh(String mesh) {
		meshes.add(mesh);
	}
	public void addModel(String model) {
		models.add(model);
	}

}
