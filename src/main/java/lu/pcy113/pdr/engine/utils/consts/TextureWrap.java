package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TextureWrap implements GLConstant {
	
	CLAMP_TO_EDGE(GL40.GL_CLAMP_TO_EDGE),
	CLAMP_TO_BORDER(GL40.GL_CLAMP_TO_BORDER),
	MIRRORED_REPEAT(GL40.GL_MIRRORED_REPEAT),
	REPEAT(GL40.GL_REPEAT);
	//MIRRORED_CLAMP_TO_EDGE(GL40.GL_MIRROR_CLAMP_TO_EDGE);
	
	private int glId;
	
	private TextureWrap(int id) {
		this.glId = id;
	}
	
	@Override
	public int getGlId() {return glId;}
	
}
