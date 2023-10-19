package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public class FloatAttribArray extends AttribArray {
	
	private float[] data;
	
	public FloatAttribArray(String name, int index, int dataSize, float[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public FloatAttribArray(String name, int index, int dataSize, float[] data, boolean s) {
		super(name, index, dataSize, s);
		this.data = data;
	}
	
	public float[] getData() {return data;}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
	public void update(float[] nPos) {
		if(nPos.length != data.length)
			return;
		data = nPos;
		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, data);
	}
	
}
