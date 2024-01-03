package lu.pcy113.pdr.engine.graph.material.gizmo;

import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class GizmoShader extends Shader {

	public static final String NAME = GizmoShader.class.getName();

	public GizmoShader() {
		super(NAME, new ShaderPart("./resources/shaders/gizmo/gizmo.vert"), new ShaderPart("./resources/shaders/gizmo/gizmo.frag"));
	}

	@Override
	public void createUniforms() {
		// vert
		getUniform(Shader.PROJECTION_MATRIX);
		getUniform(Shader.VIEW_MATRIX);
		getUniform(Shader.TRANSFORMATION_MATRIX);
		// frag
		getUniform(Shader.VIEW_POSITION);
	}

	public static class GizmoMaterial extends Material {

		public static final String NAME = GizmoMaterial.class.getName();

		public GizmoMaterial() {
			super(NAME, new GizmoShader());
		}

	}

}
