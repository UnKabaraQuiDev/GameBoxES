package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public class IntAttribArray extends AttribArray {
	
	private int[] data;
	
	public IntAttribArray(String name, int index, int dataSize, int[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public IntAttribArray(String name, int index, int dataSize, int[] data, boolean s) {
		super(name, index, dataSize, s);
		this.data = data;
	}
	
	public int[] getData() {return data;}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
	public void update(int[] nPos) {
		if(nPos.length != data.length)
			return;
		data = nPos;
		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, data);
	}

}
