package lu.pcy113.pdr.engine.scene.geom;

import java.util.Arrays;
import java.util.List;

import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueId;

public class Model implements Cleanupable, UniqueId {

	private final String id;
	private List<Mesh> meshes;
	private Material material;
	
	private Transform transform;
	
	public Model(String id, List<Mesh> meshes, Material material) {
		this.id = id;
		this.meshes = meshes;
		this.material = material;
		
		this.transform = new Transform();
	}
	
	public UniformsMap bind() {
		UniformsMap map = material.bind();
		if(map.hasUniform("modelMatrix"))
			map.setUniform("modelMatrix", transform.getMatrix());
		return map;
	}
	
	@Override
	public void cleanup() {
		meshes.forEach(Mesh::cleanup);
		material.cleanup();
	}
	
	@Override
	public String getId() {return id;}
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}
	public List<Mesh> getMeshes() {return meshes;}
	public void setMeshes(List<Mesh> meshes) {this.meshes = meshes;}
	public Material getMaterial() {return material;}
	public void setMaterial(Material material) {this.material = material;}

}
