package lu.pcy113.pdr.engine.objs.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
	public boolean hasComponent(Class<? extends Component> clazz) {
		return components
				.keySet()
				.stream()
				.map(t -> clazz.isAssignableFrom(t))
				.collect(Collectors.reducing((a, b) -> a || b))
				.get();
	}
	
	public Map<Class<? extends Component>, Component> getComponents() {return components;}
	public void setComponents(Map<Class<? extends Component>, Component> components) {this.components = components;}
	
}
