package lu.pcy113.pdr.engine.graph;

import java.util.ArrayList;
import java.util.List;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.scene.Entity;

public class Model implements Cleanupable {

	private final String id;
	
	private List<Entity> entities;
	private List<Mesh> meshes;
	
	public Model(String id, List<Mesh> mesh) {
		this.id = id;
		this.meshes = mesh;
		this.entities = new ArrayList<>();
	}
	
	@Override
	public void cleanup() {
		meshes.forEach(Mesh::cleanup);
	}
	
	public List<Entity> getEntities() {return entities;}
	public String getId() {return id;}
	public List<Mesh> getMeshes() {return meshes;}
	
}
