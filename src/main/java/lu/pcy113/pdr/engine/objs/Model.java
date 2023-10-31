package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Model implements UniqueID, Renderable {
	
	public static final String NAME = Model.class.getName();
	
	private final String name;
	private String mesh;
	
	public Model(String name, Mesh mesh) {
		this.name = name;
		this.mesh = mesh.getId();
	}
	
	public String getMesh() {return mesh;}
	public void setMesh(String mesh) {this.mesh = mesh;}
	@Override
	public String getId() {return name;}
	
}
