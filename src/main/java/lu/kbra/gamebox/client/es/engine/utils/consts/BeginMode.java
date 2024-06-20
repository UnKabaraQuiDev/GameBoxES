package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum BeginMode implements GLConstant {

	POINTS(GLES30.GL_POINTS), LINES(GLES30.GL_LINES), TRIANGLES(GLES30.GL_TRIANGLES);

	private int glId;

	private BeginMode(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
