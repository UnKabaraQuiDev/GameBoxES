package lu.pcy113.pdr.engine.scene;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lu.pcy113.pdr.engine.geom.Entity;
import lu.pcy113.pdr.engine.impl.Renderable;

public class Scene3D extends Scene implements Renderable {
	
	@Getter @Setter
	private Camera camera;
	
	private Map<String, Entity> entities = new HashMap<>();
	
	public Scene3D(Camera cam) {
		this.camera = cam;
	}
	
	public void addEntity(Entity entity) {entities.put(entity.getID(), entity);}
	public Map<String, Entity> getEntities() {return entities;}
	
}
