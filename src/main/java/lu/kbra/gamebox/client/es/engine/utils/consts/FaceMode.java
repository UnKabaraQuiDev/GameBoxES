package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum FaceMode implements GLConstant {

	FRONT_AND_BACK(GLES30.GL_FRONT_AND_BACK), FRONT(GLES30.GL_FRONT), BACK(GLES30.GL_BACK);

	private int glId;

	private FaceMode(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
