package lu.pcy113.pdr.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.components.MaterialComponent;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Material
		implements
		UniqueID {

	protected final String name;
	protected Map<String, Object> properties;
	protected String shader;

	public Material(String name, Shader shader) {
		this.name = name;
		this.properties = new HashMap<>();
		shader.getUniforms().keySet().forEach(t -> properties.put(t, null));
		this.shader = shader.getId();
	}

	public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
		for (Entry<String, Object> eso : properties.entrySet()) {
			shader.setUniform(eso.getKey(), eso.getValue());
			// GlobalLogger.log(Level.INFO, ("Material
			// "+name+"."+eso.getKey()+"="+eso.getValue()).replace("\n", " [nl] "));
		}
	}

	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	public void setPropertyIfPresent(String name, Object value) {
		if (properties.containsKey(name)) properties.put(name, value);
	}

	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public String getId() { return name; }

	public Map<String, Object> getProperties() { return properties; }

	public void setProperties(Map<String, Object> properties) { this.properties = properties; }

	public String getShader() { return shader; }

	public void setShader(String shader) { this.shader = shader; }

	/*
	 * COMPONENTS
	 */
	private Map<Class<? extends MaterialComponent>, MaterialComponent> components = new HashMap<>();

	public Material addComponent(MaterialComponent component) {
		if (component.attach(this)) components.put(component.getClass(), component);
		return this;
	}

	public <T extends MaterialComponent> T getComponent(Class<T> componentClass) {
		return (T) components.get(componentClass);
	}

	public boolean hasComponent(Class<? extends MaterialComponent> clazz) {
		return components.keySet().stream().map(t -> clazz.isAssignableFrom(t)).collect(Collectors.reducing((a, b) -> a || b)).get();
	}

	public Map<Class<? extends MaterialComponent>, MaterialComponent> getComponents() { return components; }

	public void setComponents(Map<Class<? extends MaterialComponent>, MaterialComponent> components) { this.components = components; }

}
