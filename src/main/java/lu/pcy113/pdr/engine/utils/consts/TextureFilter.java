package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TextureFilter implements GLConstant {

	NEAREST(GL40.GL_NEAREST), LINEAR(GL40.GL_LINEAR),

	NEAREST_MIPMAP_NEAREST(GL40.GL_NEAREST_MIPMAP_NEAREST), LINEAR_MIPMAP_NEAREST(GL40.GL_LINEAR_MIPMAP_NEAREST),

	NEAREST_MIPMAP_LINEAR(GL40.GL_NEAREST_MIPMAP_LINEAR), LINEAR_MIPMAP_LINEAR(GL40.GL_LINEAR_MIPMAP_LINEAR);

	private int glId;

	private TextureFilter(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
