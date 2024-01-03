package lu.pcy113.pdr.engine.graph.material.wireframe;

import lu.pcy113.pdr.engine.graph.material.Material;

public class WireframeMaterial extends Material {

	public static final String NAME = WireframeMaterial.class.getName();

	public WireframeMaterial(WireframeShader shader) {
		super(NAME, shader);
	}

}
