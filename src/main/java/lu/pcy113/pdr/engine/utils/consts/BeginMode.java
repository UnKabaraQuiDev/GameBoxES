package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL46;

public enum BeginMode implements GLConstant {

	POINTS(GL46.GL_POINTS, RenderType.POINT), LINES(GL46.GL_LINES, RenderType.LINE), TRIANGLES(GL46.GL_TRIANGLES, RenderType.FILL), POLYGON(GL46.GL_POLYGON, RenderType.FILL);

	private int glId;
	private RenderType renderType;

	private BeginMode(int glId, RenderType renderType) {
		this.glId = glId;
		this.renderType = renderType;
	}

	@Override
	public int getGlId() {
		return glId;
	}

	public RenderType getRenderType() {
		return renderType;
	}

}
