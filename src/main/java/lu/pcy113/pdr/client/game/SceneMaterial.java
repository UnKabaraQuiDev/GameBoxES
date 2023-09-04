package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;

public class SceneMaterial extends Material {
	
	public SceneMaterial(String shader) {
		super("scene", shader);
	}
	
	@Override
	public void bindProperties(Shader shader) {
		properties.put("t", (float) Math.random()/100);
		super.bindProperties(shader);
	}
	
}
