package lu.pcy113.pdr.engine.graph.shader;

import lu.pcy113.pdr.engine.impl.shader.AbstractShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;
import lu.pcy113.pdr.engine.utils.consts.BeginMode;
import lu.pcy113.pdr.engine.utils.consts.FaceMode;
import lu.pcy113.pdr.engine.utils.consts.RenderType;

public abstract class RenderShader extends AbstractShader {

	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String VIEW_MATRIX = "viewMatrix";
	public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
	public static final String VIEW_POSITION = "viewPos";

	protected boolean transparent;
	protected RenderType renderType = RenderType.FILL;
	protected BeginMode beginMode = BeginMode.TRIANGLES;
	protected FaceMode faceMode = FaceMode.FRONT_AND_BACK;

	public RenderShader(String name, AbstractShaderPart... parts) {
		this(name, false, parts);
	}

	public RenderShader(String name, boolean transparent, AbstractShaderPart... parts) {
		super(name, parts);
		this.transparent = transparent;
	}

	public void createSceneUniforms() {
		// verts
		createUniform(PROJECTION_MATRIX);
		createUniform(VIEW_MATRIX);
		createUniform(TRANSFORMATION_MATRIX);
		createUniform(VIEW_POSITION);
	}

	public boolean isTransparent() {
		return this.transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public RenderType getRenderType() {
		return renderType;
	}

	public BeginMode getBeginMode() {
		return beginMode;
	}

	public void setRenderType(RenderType renderType) {
		this.renderType = renderType;
	}

	public void setBeginMode(BeginMode beginMode) {
		this.beginMode = beginMode;
	}

	public FaceMode getFaceMode() {
		return faceMode;
	}

	public void setFaceMode(FaceMode faceMode) {
		this.faceMode = faceMode;
	}

}
