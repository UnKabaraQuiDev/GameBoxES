package lu.pcy113.pdr.engine.scene.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.impl.Bindable;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueId;

public class ModelInstance implements Cleanupable, UniqueId, Bindable<UniformsMap> {

	private final String id;
	private List<MeshInstance> meshes;
	private Material material;
	
	private final int count;
	private List<Transform> transforms;
	
	public ModelInstance(String id, List<MeshInstance> meshes, Material material, int count, Function<Integer, Transform> transforms) {
		this.id = id;
		this.meshes = meshes;
		this.material = material;
		
		this.count = count;
		this.transforms = new ArrayList<>();
		for(int i = 0; i < count; i++) {
			this.transforms.add(i, transforms.apply(i));
		}
		
		material.getShader().createUniforms(Arrays.asList("modelMatrix", "projectionMatrix"), (e) -> e.printStackTrace());
		
		/*try {material.getShader().getUniformsMap().createUniform("modelMatrix");}catch(RuntimeException e) {System.err.println("modelMatrix not found in ModelInstance: "+id+" & material: "+material);}
		try {material.getShader().getUniformsMap().createUniform("projectionMatrix");}catch(RuntimeException e) {System.err.println("projectionMatrix not found in ModelInstance: "+id+" & material: "+material);}*/
	}
	
	@Override
	public UniformsMap bind() {
		UniformsMap map = material.bind();
		return map;
	}
	
	@Override
	public void unbind() {}
	
	@Override
	public void cleanup() {
		meshes.forEach(Mesh::cleanup);
		material.cleanup();
	}
	
	@Override
	public String getId() {return id;}
	public List<Transform> getTransforms() {return transforms;}
	public void setTransforms(List<Transform> transforms) {
		this.transforms = transforms;
	}
	public int getCount() {return count;}
	public List<MeshInstance> getMeshes() {return meshes;}
	public void setMeshes(List<MeshInstance> meshes) {this.meshes = meshes;}
	public Material getMaterial() {return material;}
	public void setMaterial(Material material) {this.material = material;}

}
