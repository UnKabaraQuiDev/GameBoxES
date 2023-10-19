package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public abstract class AttribArray {
	
	protected int vbo;
	protected boolean iStatic = true;;
	
	protected String name;
	protected int index;
	protected int dataSize;
	
	public AttribArray(String name, int index, int dataSize) {
		this.name = name;
		this.index = index;
		this.dataSize = dataSize;
	}
	public AttribArray(String name, int index, int dataSize, boolean iStatic) {
		this.name = name;
		this.index = index;
		this.dataSize = dataSize;
		this.iStatic = iStatic;
	}
	
	public String getName() {return name;}
	public int getIndex() {return index;}
	public int getDataSize() {return dataSize;}
	
	public int getDataCount() {
		return getLength()/getDataSize();
	}
	
	public abstract int getLength();
	
	public void bind() {
		GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, vbo);
	}
	public void unbind() {
		GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, 0);
	}
	
	public void setVbo(int vbo) {this.vbo = vbo;}
	public int getVbo() {return vbo;}
	public boolean isStatic() {return iStatic;}
	
}
