package lu.pcy113.pdr.engine.graph.material.gizmo;

import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class GizmoShader extends RenderShader {

	public static final String NAME = GizmoShader.class.getName();

	public GizmoShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/gizmo/gizmo.vert"), AbstractShaderPart.load("./resources/shaders/gizmo/gizmo.frag"));
	}

	@Override
	public void createUniforms() {
		// vert
		getUniform(RenderShader.PROJECTION_MATRIX);
		getUniform(RenderShader.VIEW_MATRIX);
		getUniform(RenderShader.TRANSFORMATION_MATRIX);
		// frag
		getUniform(RenderShader.VIEW_POSITION);
	}

	public static class GizmoMaterial extends Material {

		public static final String NAME = GizmoMaterial.class.getName();

		public GizmoMaterial() {
			super(NAME, new GizmoShader());
		}

	}

}
