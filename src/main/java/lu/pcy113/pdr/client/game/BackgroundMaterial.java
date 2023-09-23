package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Material;

public class BackgroundMaterial extends Material {
	
	public static final String NAME = BackgroundMaterial.class.getName();
	
	public BackgroundMaterial(int index) {
		super(NAME+"-"+index, BackgroundShader.NAME+"-"+index);
	}
	
}
