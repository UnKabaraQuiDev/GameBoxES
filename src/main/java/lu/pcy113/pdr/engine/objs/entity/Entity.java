package lu.pcy113.pdr.engine.objs.entity;

import java.util.HashMap;
import java.util.Map;

public class Entity {
	
	private Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	public Entity addComponent(Component component) {
		if(component.attach(this))
			components.put(component.getClass(), component);
		return this;
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass) {
		return (T) components.get(componentClass);
	}
	
}
