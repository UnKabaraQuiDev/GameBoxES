package lu.pcy113.pdr.engine.geom;

import lu.pcy113.pdr.engine.graph.Material;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.scene.Transform;
import lu.pcy113.pdr.engine.scene.geom.Mesh;

public class Entity implements Cleanupable, Renderable, UniqueID {
	
	private final String name;
	
	private Mesh mesh;
	private Material material;
	private Transform transform;
	
	public Entity(String name, Mesh m) {
		this.name = name;
		this.mesh = m;
		this.transform = new Transform();
	}
	
	@Override
	public void cleanup() {
		mesh.cleanup();
	}
	
	@Override
	public String getID() {return name;}
	public Mesh getMesh() {return mesh;}
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}

	public void bind() {
		mesh.bind();
	}
	public void unbind() {
		mesh.unbind();
	}
	
}
