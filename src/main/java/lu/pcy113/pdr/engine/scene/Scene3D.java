package lu.pcy113.pdr.engine.scene;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.LightComponent;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightComponent;
import lu.pcy113.pdr.engine.scene.camera.Camera;

public class Scene3D extends Scene {
	
	public static final String NAME = Scene3D.class.getName();
	
	protected Map<String, Entity> entities = new LinkedHashMap<>();
	protected List<String> lightEmittors = new ArrayList<>();
	
	public Scene3D(String name) {
		super(name, Camera.perspectiveCamera3D());
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
	public Map<String, Entity> getEntities() {return entities;}
	public void setEntities(Map<String, Entity> entities) {this.entities = entities;}
	public Entity addEntity(String str, Entity entity) {
		/*if(this.entities.containsKey(str))
			this.entities.remove(str);*/
		this.entities.put(str, entity);
		if(entity.hasComponent(LightComponent.class)) {
			this.lightEmittors.add(str);
		}
		return entity;
	}
	public Entity getEntity(String str) {
		return this.entities.get(str);
	}
	
	public List<String> getLightEmittors() {return lightEmittors;}
	public void setLightEmittors(List<String> lightEmittors) {this.lightEmittors = lightEmittors;}
	public List<LightComponent> getLights() {
		return getLightEmittors()
				.stream()
				.map((String e) -> (PointLightComponent) entities.get(e).getComponent(PointLightComponent.class))
				.collect(Collectors.toList())
				/*.addAll(getLightEmittors()
					.stream()
					.map((String e) -> (LightComponent) entities.get(e).getComponent(LightComponent.class))
					.collect(Collectors.toList())
					.addAll())*/;
	}

}
