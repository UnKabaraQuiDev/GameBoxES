package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum FaceMode implements GLConstant {

	FRONT_AND_BACK(GL_W.GL_FRONT_AND_BACK), FRONT(GL_W.GL_FRONT), BACK(GL_W.GL_BACK);

	private int glId;

	private FaceMode(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
