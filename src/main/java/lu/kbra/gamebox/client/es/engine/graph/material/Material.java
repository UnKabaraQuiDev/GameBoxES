package lu.kbra.gamebox.client.es.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.components.MaterialComponent;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;

public class Material implements UniqueID {

	protected final String name;
	protected Map<String, Object> properties;
	protected RenderShader shader;

	public Material(String name, RenderShader shader) {
		this.name = name;
		this.shader = shader;

		this.properties = new HashMap<>();
		if (shader == null)
			throw new IllegalArgumentException("Shader cannot be null!");
		if (shader.getUniforms() == null)
			throw new IllegalArgumentException("Shader uniforms cannot be null!");
		shader.getUniforms().keySet().forEach(t -> properties.put(t, null));
	}

	public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
		GameEngine.DEBUG.start("r_uniforms_bind_for");
		for (Entry<String, Object> eso : properties.entrySet()) {
			GameEngine.DEBUG.start("r_uniforms_bind_single");
			shader.setUniform(eso.getKey(), eso.getValue());
			GameEngine.DEBUG.end("r_uniforms_bind_single");
		}
		GameEngine.DEBUG.end("r_uniforms_bind_for");
	}

	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}

	public void setPropertyIfPresent(String name, Object value) {
		if (properties.containsKey(name))
			properties.put(name, value);
	}

	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	public Object getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public String getId() {
		return name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public RenderShader getRenderShader() {
		return shader;
	}

	public void setShader(RenderShader shader) {
		this.shader = shader;
	}

	/*
	 * COMPONENTS
	 */
	private Map<Class<? extends MaterialComponent>, MaterialComponent> components = new HashMap<>();

	public Material addComponent(MaterialComponent component) {
		if (component.attach(this))
			components.put(component.getClass(), component);
		return this;
	}

	public <T extends MaterialComponent> T getComponent(Class<T> componentClass) {
		return (T) components.get(componentClass);
	}

	public boolean hasComponent(Class<? extends MaterialComponent> clazz) {
		return components.keySet().stream().map(t -> clazz.isAssignableFrom(t)).collect(Collectors.reducing((a, b) -> a || b)).get();
	}

	public Map<Class<? extends MaterialComponent>, MaterialComponent> getComponents() {
		return components;
	}

	public void setComponents(Map<Class<? extends MaterialComponent>, MaterialComponent> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "{" + name + "->" + shader.getId() + "}";
	}

}
