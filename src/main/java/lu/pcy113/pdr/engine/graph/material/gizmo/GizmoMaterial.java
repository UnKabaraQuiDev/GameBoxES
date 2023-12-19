package lu.pcy113.pdr.engine.graph.material.gizmo;

import lu.pcy113.pdr.engine.graph.material.Material;

public class GizmoMaterial
		extends
		Material {

	public static final String NAME = GizmoMaterial.class.getName();

	public GizmoMaterial(GizmoShader shader) {
		super(NAME, shader);
	}

}
