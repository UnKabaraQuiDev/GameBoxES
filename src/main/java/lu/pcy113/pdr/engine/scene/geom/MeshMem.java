package lu.pcy113.pdr.engine.scene.geom;

import java.util.HashMap;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.ObjLoader;

public class MeshMem implements Cleanupable {
	
	private final String dir;
	private HashMap<String, Mesh> meshes;
	
	public MeshMem() {
		this.dir = "./resources/models/";
	}
	public MeshMem(String dir) {
		this.dir = dir;
		this.meshes = new HashMap<>();
	}
	
	@Override
	public void cleanup() {
		meshes.values().forEach(Mesh::cleanup);		
	}
	
	public void addMesh(Mesh mesh) {meshes.put(mesh.getID(), mesh);}
	public Mesh getMesh(String name) {
		if(!meshes.containsKey(name))
			addMesh(loadMesh(name));
		return meshes.get(name);
	}
	public String getDir() {return dir;}

	public Mesh loadMesh(String name) {
		Mesh m = ObjLoader.loadMesh(name, dir+name+".obj");
		addMesh(m);
		return m;
	}
	public Mesh loadMesh(String name, String path) {
		Mesh m = ObjLoader.loadMesh(name, path);
		addMesh(m);
		return m;
	}
	
}
