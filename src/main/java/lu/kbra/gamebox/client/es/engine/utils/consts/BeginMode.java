package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum BeginMode implements GLConstant {

	POINTS(GL_W.GL_POINTS), LINES(GL_W.GL_LINES), TRIANGLES(GL_W.GL_TRIANGLES);

	private int glId;

	private BeginMode(int glId) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
