package lu.pcy113.pdr.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Material implements UniqueID {
	
	protected final String name;
	//protected final String lights, lightCount;
	protected Map<String, Object> properties;
	protected String shader;
	
	public Material(String name, String shader) {
		this.name = name;
		/*this.lights = lights;
		this.lightCount = lightCount;*/
		this.properties = new HashMap<>();
		this.shader = shader;
	}
	
	public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
		for(Entry<String, Object> eso : properties.entrySet()) {
			shader.setUniform(eso.getKey(), eso.getValue());
			//System.out.println(name+" prop: "+eso.getKey()+" = "+eso.getValue());
		}
	}
	
	/*public void bindLights(CacheManager cache, List<String> pointLights) {
		if(lights == null || lightCount == null)
			return;
		//System.err.println(name+" lights not null: "+pointLights);
		
		int i = 0;
		for(String pLight : pointLights) {
			PointLight pointLight = cache.getPointLight(pLight);
			if(pointLight != null)
				pointLight.bind(this, lights, i++);
		}
		properties.put(lightCount, pointLights.size());
	}*/
	
	public void setProperty(String name, Object value) {
		properties.put(name, value);
	}
	public Object getProperty(String name) {
		return properties.get(name);
	}
	
	@Override
	public String getId() {return name;}
	public Map<String, Object> getProperties() {return properties;}
	public void setProperties(Map<String, Object> properties) {this.properties = properties;}
	public String getShader() {return shader;}
	public void setShader(String shader) {this.shader = shader;}

}
