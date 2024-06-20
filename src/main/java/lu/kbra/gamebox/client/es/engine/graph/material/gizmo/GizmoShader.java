package lu.kbra.gamebox.client.es.engine.graph.material.gizmo;

import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.consts.BeginMode;
import lu.kbra.gamebox.client.es.engine.utils.consts.FaceMode;

public class GizmoShader extends RenderShader {

	public static final String NAME = GizmoShader.class.getName();

	public GizmoShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/gizmo/gizmo.vert"), AbstractShaderPart.load("./resources/shaders/gizmo/gizmo.frag"));
		
		super.setBeginMode(BeginMode.LINES);
		super.setFaceMode(FaceMode.FRONT_AND_BACK);
	}

	@Override
	public void createUniforms() {
		// vert
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		// frag
		createUniform(RenderShader.VIEW_POSITION);
	}

	public static class GizmoMaterial extends Material {

		public static final String NAME = GizmoMaterial.class.getName();

		public GizmoMaterial(GizmoShader shader) {
			super(NAME, shader);
		}

		public GizmoMaterial() {
			super(NAME, new GizmoShader());
		}

	}

}
