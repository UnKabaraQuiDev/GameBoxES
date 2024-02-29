package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum FrameBufferAttachment implements GLConstant {

	DEPTH(GL40.GL_DEPTH_ATTACHMENT), STENCIL(GL40.GL_STENCIL_ATTACHMENT),

	DEPTH_STENCIL(GL40.GL_DEPTH_STENCIL_ATTACHMENT),

	COLOR_FIRST(GL40.GL_COLOR_ATTACHMENT0), COLOR_LAST(GL40.GL_COLOR_ATTACHMENT31);

	private int glId;

	private FrameBufferAttachment(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
