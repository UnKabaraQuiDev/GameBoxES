package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL46;

public enum FaceMode implements GLConstant {

	FRONT_AND_BACK(GL46.GL_FRONT_AND_BACK), FRONT(GL46.GL_FRONT), BACK(GL46.GL_BACK);

	private int glId;

	private FaceMode(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
