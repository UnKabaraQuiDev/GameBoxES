package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum FrameBufferAttachment implements GLConstant {

	DEPTH(GL_W.GL_DEPTH_ATTACHMENT), STENCIL(GL_W.GL_STENCIL_ATTACHMENT),

	DEPTH_STENCIL(GL_W.GL_DEPTH_STENCIL_ATTACHMENT),

	COLOR_FIRST(GL_W.GL_COLOR_ATTACHMENT0), COLOR_LAST(GL_W.GL_COLOR_ATTACHMENT31);

	private int glId;

	private FrameBufferAttachment(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
