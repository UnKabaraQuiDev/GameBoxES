package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TextureType implements GLConstant {
	
	D1(GL40.GL_TEXTURE_1D), D2(GL40.GL_TEXTURE_2D), D3(GL40.GL_TEXTURE_3D);
	
	private int glId;
	
	private TextureType(int id) {
		this.glId = id;
	}
	
	@Override
	public int getGlId() {return glId;}
	
}
