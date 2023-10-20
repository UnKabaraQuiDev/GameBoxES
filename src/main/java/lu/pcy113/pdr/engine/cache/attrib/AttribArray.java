package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public abstract class AttribArray {
	
	protected int vbo;
	protected boolean iStatic = true;;
	
	protected int bufferType;
	
	protected String name;
	protected int index;
	protected int dataSize;
	
	public AttribArray(String name, int index, int dataSize) {
		this(name, index, dataSize, GL40.GL_ARRAY_BUFFER, true);
	}
	public AttribArray(String name, int index, int dataSize, int bufferType) {
		this(name, index, dataSize, bufferType, true);
	}
	public AttribArray(String name, int index, int dataSize, boolean iStatic) {
		this(name, index, dataSize, GL40.GL_ARRAY_BUFFER, iStatic);
	}
	public AttribArray(String name, int index, int dataSize, int bufferType, boolean iStatic) {
		this.name = name;
		this.index = index;
		this.dataSize = dataSize;
		this.bufferType = bufferType;
		this.iStatic = iStatic;
	}
	
	public int getDataCount() {
		return getLength()/getDataSize();
	}
	
	public abstract int getLength();
	public abstract void init();
	
	public void enable() {
		GL40.glEnableVertexAttribArray(index);
	}
	public void disable() {
		GL40.glDisableVertexAttribArray(index);
	}
	
	public int gen() {
		return (vbo = GL40.glGenBuffers());
	}
	
	public void bind() {
		GL40.glBindBuffer(bufferType, vbo);
	}
	public void unbind() {
		GL40.glBindBuffer(bufferType, 0);
	}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public int getIndex() {return index;}
	public void setIndex(int index) {this.index = index;}
	public int getDataSize() {return dataSize;}
	public int getBufferType() {return bufferType;}
	public void setBufferType(int bufferType) {this.bufferType = bufferType;}
	public void setVbo(int vbo) {this.vbo = vbo;}
	public int getVbo() {return vbo;}
	public boolean isStatic() {return iStatic;}
	
	@Override
	public String toString() {
		return getVbo()+"|"+getIndex()+") "+getName()+": "+getLength()+"/"+getDataSize()+"="+getDataCount();
	}
	
}
