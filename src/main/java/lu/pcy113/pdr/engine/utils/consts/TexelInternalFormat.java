package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TexelInternalFormat implements GLConstant {
	
	RGBA(GL40.GL_RGBA), RGB(GL40.GL_RGB), R3G3B2(GL40.GL_R3_G3_B2), RGB10(GL40.GL_RGB10), R(GL40.GL_R),
	DEPTH(GL40.GL_DEPTH), DEPTH32FSTENCIL8(GL40.GL_DEPTH32F_STENCIL8), DEPTH24STENCIL8(GL40.GL_DEPTH24_STENCIL8);
	
	private int glId;
	
	private TexelInternalFormat(int id) {
		this.glId = id;
	}
	
	@Override
	public int getGlId() {return glId;}
	
}
