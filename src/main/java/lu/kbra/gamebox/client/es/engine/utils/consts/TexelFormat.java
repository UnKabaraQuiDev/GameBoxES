package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum TexelFormat implements GLConstant {

	// Base color
	RED(GLES30.GL_RED, null), GREEN(GLES30.GL_GREEN, null), BLUE(GLES30.GL_BLUE, null), ALPHA(GLES30.GL_ALPHA, null), RG(GLES30.GL_RG, null), RGB(GLES30.GL_RGB, null), RGBA(GLES30.GL_RGBA, null),

	RED_INTEGER(GLES30.GL_RED_INTEGER, RED), RG_INTEGER(GLES30.GL_RG_INTEGER, RG), RGB_INTEGER(GLES30.GL_RGB_INTEGER, RGB), RGBA_INTEGER(GLES30.GL_RGBA_INTEGER, RGBA),

	// Non-color
	DEPTH(GLES30.GL_DEPTH_COMPONENT, null), DEPTH_STENCIL(GLES30.GL_DEPTH_STENCIL, null);

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
