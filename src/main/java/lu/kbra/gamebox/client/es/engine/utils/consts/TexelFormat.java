package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TexelFormat implements GLConstant {

	// Base color
	RED(GL_W.GL_RED, null), GREEN(GL_W.GL_GREEN, null), BLUE(GL_W.GL_BLUE, null), ALPHA(GL_W.GL_ALPHA, null), RG(GL_W.GL_RG, null), RGB(GL_W.GL_RGB, null), RGBA(GL_W.GL_RGBA, null),

	RED_INTEGER(GL_W.GL_RED_INTEGER, RED), RG_INTEGER(GL_W.GL_RG_INTEGER, RG), RGB_INTEGER(GL_W.GL_RGB_INTEGER, RGB), RGBA_INTEGER(GL_W.GL_RGBA_INTEGER, RGBA),

	// Non-color
	DEPTH(GL_W.GL_DEPTH_COMPONENT, null), DEPTH_STENCIL(GL_W.GL_DEPTH_STENCIL, null);

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
