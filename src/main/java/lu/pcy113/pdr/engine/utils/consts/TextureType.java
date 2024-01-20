package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TextureType implements GLConstant {
	
	// Base textures
	TXT1D(GL40.GL_TEXTURE_1D, null), TXT2D(GL40.GL_TEXTURE_2D, null), TXT3D(GL40.GL_TEXTURE_3D, null),
	
	PROXY1D(GL40.GL_PROXY_TEXTURE_1D, TXT1D), PROXY2D(GL40.GL_PROXY_TEXTURE_2D, TXT2D), PROXY3D(GL40.GL_PROXY_TEXTURE_3D, TXT3D),
	
	ARRAY1D(GL40.GL_TEXTURE_1D_ARRAY, TXT1D), ARRAY2D(GL40.GL_TEXTURE_2D_ARRAY, TXT2D),
	
	PROXYARRAY1D(GL40.GL_PROXY_TEXTURE_1D_ARRAY, ARRAY1D), PROXYARRAY2D(GL40.GL_PROXY_TEXTURE_2D_ARRAY, ARRAY2D),
	
	TXTRECT(GL40.GL_TEXTURE_RECTANGLE, null), PROXYRECT(GL40.GL_PROXY_TEXTURE_RECTANGLE, TXTRECT),
	
	// Cubemaps
	CM_PX(GL40.GL_TEXTURE_CUBE_MAP_POSITIVE_X, null), CM_NX(GL40.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, null),
	CM_PY(GL40.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, null), CM_NY(GL40.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, null),
	CM_PZ(GL40.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, null), CM_NZ(GL40.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, null),
	PROXYCM(GL40.GL_PROXY_TEXTURE_CUBE_MAP, null),
	CUBE_MAP(GL40.GL_TEXTURE_CUBE_MAP, null);
	
	private int glId;
	private TextureType base;
	
	private TextureType(int id, TextureType base) {
		this.glId = id;
		this.base = base;
	}
	
	@Override
	public int getGlId() {return glId;}
	public TextureType getBase() {return base;}
	public boolean hasBase() {return base != null;}
	
}
