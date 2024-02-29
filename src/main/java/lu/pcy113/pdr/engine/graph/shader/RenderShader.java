package lu.pcy113.pdr.engine.graph.shader;

import lu.pcy113.pdr.engine.impl.shader.AbstractShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public abstract class RenderShader extends AbstractShader {

	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String VIEW_MATRIX = "viewMatrix";
	public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
	public static final String VIEW_POSITION = "viewPos";

	protected boolean transparent;

	public RenderShader(String name, AbstractShaderPart... parts) {
		this(name, false, parts);
	}

	public RenderShader(String name, boolean transparent, AbstractShaderPart... parts) {
		super(name, parts);
		this.transparent = transparent;
	}

	public void createSceneUniforms() {
		// verts
		createUniform(
				PROJECTION_MATRIX);
		createUniform(
				VIEW_MATRIX);
		createUniform(
				TRANSFORMATION_MATRIX);
		createUniform(
				VIEW_POSITION);
	}

	public boolean isTransparent() {
		return this.transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

}
