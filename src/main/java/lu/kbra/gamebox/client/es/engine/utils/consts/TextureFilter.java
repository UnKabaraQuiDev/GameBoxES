package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum TextureFilter implements GLConstant {

	NEAREST(GLES30.GL_NEAREST), LINEAR(GLES30.GL_LINEAR),

	NEAREST_MIPMAP_NEAREST(GLES30.GL_NEAREST_MIPMAP_NEAREST), LINEAR_MIPMAP_NEAREST(GLES30.GL_LINEAR_MIPMAP_NEAREST),

	NEAREST_MIPMAP_LINEAR(GLES30.GL_NEAREST_MIPMAP_LINEAR), LINEAR_MIPMAP_LINEAR(GLES30.GL_LINEAR_MIPMAP_LINEAR);

	private int glId;

	private TextureFilter(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
