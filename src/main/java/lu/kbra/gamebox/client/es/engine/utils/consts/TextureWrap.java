package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum TextureWrap implements GLConstant {

	CLAMP_TO_EDGE(GLES30.GL_CLAMP_TO_EDGE), MIRRORED_REPEAT(GLES30.GL_MIRRORED_REPEAT), REPEAT(GLES30.GL_REPEAT);

	private int glId;

	private TextureWrap(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
