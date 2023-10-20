package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public class UIntAttribArray extends AttribArray {
	
	private int[] data;
	
	public UIntAttribArray(String name, int index, int dataSize, int[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public UIntAttribArray(String name, int index, int dataSize, int[] data, boolean s) {
		super(name, index, dataSize, s);
		this.data = data;
	}
	
	@Override
	public void init() {
		GL40.glBufferData(GL40.GL_ARRAY_BUFFER, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		GL40.glVertexAttribPointer(index, dataSize, GL40.GL_UNSIGNED_INT, false, 0, 0);
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
