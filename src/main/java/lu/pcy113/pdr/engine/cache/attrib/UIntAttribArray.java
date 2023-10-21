package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public class UIntAttribArray extends AttribArray {
	
	private int[] data;
	
	public UIntAttribArray(String name, int index, int dataSize, int[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public UIntAttribArray(String name, int index, int dataSize, int[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}
	public UIntAttribArray(String name, int index, int dataSize, int[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}
	
	@Override
	public void init() {
		GL40.glBufferData(bufferType, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		if(bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER) {
			GL40.glVertexAttribPointer(index, dataSize, GL40.GL_UNSIGNED_INT, false, 0, 0);
			System.err.println(name+" is "+bufferType+" vertex attrib true");
		}else {
			System.err.println(name+" is "+bufferType+" vertex attrib false");
		}
	}
	
	public int[] getData() {return data;}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
	public void update(int[] nPos) {
		if(!iStatic && nPos.length != data.length)
			return;
		data = nPos;
		
		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, data);
	}

}
