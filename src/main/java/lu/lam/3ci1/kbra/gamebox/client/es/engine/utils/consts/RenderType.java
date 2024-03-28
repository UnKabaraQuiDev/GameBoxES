package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL46;

public enum RenderType implements GLConstant {

	LINE(GL46.GL_LINE), POINT(GL46.GL_POINT), FILL(GL46.GL_FILL);

	private int glId;

	private RenderType(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
