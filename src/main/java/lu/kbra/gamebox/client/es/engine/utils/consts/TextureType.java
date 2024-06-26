package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TextureType implements GLConstant {

	// Base textures
	TXT2D(GL_W.GL_TEXTURE_2D, null), TXT3D(GL_W.GL_TEXTURE_3D, null),

	ARRAY2D(GL_W.GL_TEXTURE_2D_ARRAY, TXT2D),

	// Cubemaps
	CM_PX(GL_W.GL_TEXTURE_CUBE_MAP_POSITIVE_X, null), CM_NX(GL_W.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, null), CM_PY(GL_W.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, null), CM_NY(GL_W.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, null),
	CM_PZ(GL_W.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, null), CM_NZ(GL_W.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, null), CUBE_MAP(GL_W.GL_TEXTURE_CUBE_MAP, null);

	private int glId;
	private TextureType base;

	private TextureType(int id, TextureType base) {
		this.glId = id;
		this.base = base;
	}

	@Override
	public int getGlId() {
		return glId;
	}

	public TextureType getBase() {
		return base;
	}

	public boolean hasBase() {
		return base != null;
	}

	public static boolean isArray(TextureType txtType) {
		return txtType == ARRAY2D;
	}

	public static boolean isCubemap(TextureType txtType) {
		return txtType == CM_PX || txtType == CM_NX || txtType == CM_PY || txtType == CM_NY || txtType == CM_PZ || txtType == CM_NZ;
	}

}
