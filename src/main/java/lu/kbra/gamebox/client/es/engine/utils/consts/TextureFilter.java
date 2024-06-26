package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TextureFilter implements GLConstant {

	NEAREST(GL_W.GL_NEAREST), LINEAR(GL_W.GL_LINEAR),

	NEAREST_MIPMAP_NEAREST(GL_W.GL_NEAREST_MIPMAP_NEAREST), LINEAR_MIPMAP_NEAREST(GL_W.GL_LINEAR_MIPMAP_NEAREST),

	NEAREST_MIPMAP_LINEAR(GL_W.GL_NEAREST_MIPMAP_LINEAR), LINEAR_MIPMAP_LINEAR(GL_W.GL_LINEAR_MIPMAP_LINEAR);

	private int glId;

	private TextureFilter(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
