package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TexelFormat implements GLConstant {

	// Base color
	RED(GL40.GL_RED, null), GREEN(GL40.GL_GREEN, null), BLUE(GL40.GL_BLUE, null), ALPHA(GL40.GL_ALPHA, null), RG(GL40.GL_RG, null), RGB(GL40.GL_RGB, null), RGBA(GL40.GL_RGBA, null), BGR(GL40.GL_BGR, null), BGRA(GL40.GL_BGRA, null),

	RED_INTEGER(GL40.GL_RED_INTEGER, RED), GREEN_INTEGER(GL40.GL_GREEN_INTEGER, GREEN), BLUE_INTEGER(GL40.GL_BLUE_INTEGER, BLUE), ALPHA_INTEGER(GL40.GL_ALPHA_INTEGER, ALPHA), RG_INTEGER(GL40.GL_RG_INTEGER, RG), RGB_INTEGER(GL40.GL_RGB_INTEGER, RGB),
	RGBA_INTEGER(GL40.GL_RGBA_INTEGER, RGBA), BGR_INTEGER(GL40.GL_BGR_INTEGER, BGR), BGRA_INTEGER(GL40.GL_BGRA_INTEGER, BGRA),

	// Non-color
	STENCIL(GL40.GL_STENCIL_INDEX, null), DEPTH(GL40.GL_DEPTH_COMPONENT, null), DEPTH_STENCIL(GL40.GL_DEPTH_STENCIL, null);

	private int glId;
	private TexelFormat base;

	private TexelFormat(int id, TexelFormat base) {
		this.glId = id;
		this.base = base;
	}

	@Override
	public int getGlId() {
		return glId;
	}

	public TexelFormat getBase() {
		return base;
	}

	public boolean isBase() {
		return base == null;
	}

}
