package lu.pcy113.pdr.engine.cache.attrib;

import java.util.logging.Level;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;

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

	public FloatAttribArray(String name, int index, int dataSize, float[] data, int bufferType, boolean _static) {
		super(name, index, dataSize, bufferType, _static);
		this.data = data;
	}

	public FloatAttribArray(String name, int index, int dataSize, float[] data, boolean _static) {
		super(name, index, dataSize, _static);
		this.data = data;
	}

	public FloatAttribArray(String name, int index, int dataSize, float[] data, boolean _static, int divisor) {
		super(name, index, dataSize, _static, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GL40.glBufferData(bufferType, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		if (bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER)
			GL40.glVertexAttribPointer(index, dataSize, GL40.GL_FLOAT, false, 0, 0);
	}

	public boolean update(float[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GL40.glBufferSubData(bufferType, 0, data);

		return GL40.glGetError() == GL40.GL_NO_ERROR;
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public float[] getData() {
		return data;
	}

	public Float get(int i) {
		return data[i];
	}

}
