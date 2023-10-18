package lu.pcy113.pdr.engine.scene;

import java.util.LinkedList;
import java.util.List;

import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.scene.camera.Camera;

public class Scene3D extends Scene {
	
	public static final String NAME = Scene3D.class.getName();
	
	protected List<Entity> entities = new LinkedList<>();
	
	public Scene3D(String name) {
		super(name, Camera.camera3D());
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
	public List<Entity> getEntities() {return entities;}
	public void setEntities(List<Entity> entities) {this.entities = entities;}
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

}
