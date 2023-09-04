package lu.pcy113.pdr.engine.scene;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public abstract class Scene implements UniqueID, Cleanupable {
	
	protected final String name;
	protected Camera camera;
	
	public Scene(String name, Camera cam) {
		this.name = name;
		this.camera = cam;
	}
	
	@Override
	public String getId() {return name;}
	public Camera getCamera() {return camera;}
	
}
