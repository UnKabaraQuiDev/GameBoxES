package lu.pcy113.pdr.engine.graph.material.text;

import java.util.Map;

import lu.pcy113.pdr.engine.graph.material.TextureMaterial;

public class TextMaterial
		extends
		TextureMaterial {

	public TextMaterial(String name, TextShader tShader, Map<String, String> textures) {
		super(name, tShader, textures);
	}

}
