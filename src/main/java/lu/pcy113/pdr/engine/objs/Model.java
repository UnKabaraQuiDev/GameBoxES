package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class Model implements UniqueID {
	
	public static final String NAME = Model.class.getName();
	
	private final String name;
	private String mesh;
	private Transform transform;
	
	public Model(String name, String mesh, Transform transform) {
		this.name = name;
		this.mesh = mesh;
		this.transform = transform;
	}
	
	public String getMesh() {return mesh;}
	public void setMesh(String mesh) {this.mesh = mesh;}
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}
	@Override
	public String getId() {return name;}
	
}
