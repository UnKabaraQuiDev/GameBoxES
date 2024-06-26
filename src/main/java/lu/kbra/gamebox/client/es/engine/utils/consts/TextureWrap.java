package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TextureWrap implements GLConstant {

	CLAMP_TO_EDGE(GL_W.GL_CLAMP_TO_EDGE), MIRRORED_REPEAT(GL_W.GL_MIRRORED_REPEAT), REPEAT(GL_W.GL_REPEAT);

	private int glId;

	private TextureWrap(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
