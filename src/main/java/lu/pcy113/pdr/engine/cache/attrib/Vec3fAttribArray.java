package lu.pcy113.pdr.engine.cache.attrib;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

public class Vec3fAttribArray extends AttribArray {

	private Vector3f[] data;
	
	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}
	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}
	
	@Override
	public void init() {
		GL40.glBufferData(bufferType, toFlatArray(), iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		if(bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER)
			GL40.glVertexAttribPointer(index, dataSize, GL40.GL_FLOAT, false, 0, 0);
	}
	
	public void update(Vector3f[] nPos) {
		if(!iStatic && nPos.length != data.length)
			return;
		data = nPos;
		
		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, toFlatArray());
	}
	
	public float[] toFlatArray() {
		float[] flatArray = new float[data.length * 3];
		for(int i = 0; i < data.length; i++) {
			float[] dat = new float[3];
			data[i].get(FloatBuffer.allocate(3)).get(dat);
			System.arraycopy(dat, 0, flatArray, i*3, 3);
		}
		return flatArray;
	}
	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize*3, toFlatArray(), bufferType, iStatic);
	}
	
	@Override
	public int getLength() {
		return data.length;
	}
	public Vector3f[] getData() {return data;}
	
}