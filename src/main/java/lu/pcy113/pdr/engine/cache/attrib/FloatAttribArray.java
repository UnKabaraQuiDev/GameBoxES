package lu.pcy113.pdr.engine.cache.attrib;

import java.util.Arrays;

import org.lwjgl.opengl.GL40;

public class FloatAttribArray extends AttribArray {
	
	private float[] data;
	
	public FloatAttribArray(String name, int index, int dataSize, float[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public FloatAttribArray(String name, int index, int dataSize, float[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}
	public FloatAttribArray(String name, int index, int dataSize, float[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}
	public FloatAttribArray(String name, int index, int dataSize, float[] data, boolean s) {
		super(name, index, dataSize, s);
		this.data = data;
	}
	
	public float[] getData() {return data;}
	
	@Override
	public void init() {
		GL40.glBufferData(bufferType, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		if(bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER)
			GL40.glVertexAttribPointer(index, dataSize, GL40.GL_FLOAT, false, 0, 0);
	}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
	public void update(float[] nPos) {
		if(!iStatic && nPos.length != data.length)
			return;
		data = nPos;
		
		System.out.println("New data is"+Arrays.toString(data));
		
		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, data);
	}
	
}
