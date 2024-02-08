package lu.pcy113.pdr.engine.graph.material;

import lu.pcy113.pdr.engine.impl.AbstractShader;

public abstract class Shader extends AbstractShader {

	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String VIEW_MATRIX = "viewMatrix";
	public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
	public static final String VIEW_POSITION = "viewPos";

	protected boolean transparent;

	public Shader(String name, ShaderPart... parts) {
		this(name, false, parts);
	}

	public Shader(String name, boolean transparent, ShaderPart... parts) {
		super(name, parts);
		this.transparent = transparent;
	}

	public boolean isTransparent() {
		return this.transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

}
