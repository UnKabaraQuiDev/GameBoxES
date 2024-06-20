package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum FrameBufferAttachment implements GLConstant {

	DEPTH(GLES30.GL_DEPTH_ATTACHMENT), STENCIL(GLES30.GL_STENCIL_ATTACHMENT),

	DEPTH_STENCIL(GLES30.GL_DEPTH_STENCIL_ATTACHMENT),

	COLOR_FIRST(GLES30.GL_COLOR_ATTACHMENT0), COLOR_LAST(GLES30.GL_COLOR_ATTACHMENT31);

	private int glId;

	private FrameBufferAttachment(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
