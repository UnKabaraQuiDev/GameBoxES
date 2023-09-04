package lu.pcy113.pdr.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import lu.pcy113.pdr.engine.impl.UniqueID;

public class Material implements UniqueID {
	
	protected final String name;
	protected Map<String, Object> properties;
	protected String shader;
	
	public Material(String name, String shader) {
		this.name = name;
		this.properties = new HashMap<>();
		this.shader = shader;
	}
	
	public void bindProperties(Shader shader) {
		for(Entry<String, Object> eso : properties.entrySet()) {
			shader.setUniform(eso.getKey(), eso.getValue());
		}
	}
	
	public void setProperty(String name, Matrix4f value) {
		properties.put(name, value);
	}
	
	@Override
	public String getId() {return name;}
	public Map<String, Object> getProperties() {return properties;}
	public void setProperties(Map<String, Object> properties) {this.properties = properties;}
	public String getShader() {return shader;}
	public void setShader(String shader) {this.shader = shader;}

}
