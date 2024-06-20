package lu.kbra.gamebox.client.es.engine.cache.attrib;

import org.lwjgl.opengles.GLES30;

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
	
	public FloatAttribArray(String name, int index, int dataSize, float[] data, int bufferType, boolean _static, int divisor) {
		super(name, index, dataSize, bufferType, _static, divisor);
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
		GLES30.glBufferData(bufferType, data, iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER && bufferType != GLES30.GL_UNIFORM_BUFFER)
			GLES30.glVertexAttribPointer(index, dataSize, GLES30.GL_FLOAT, false, 0, 0);
	}

	public boolean update(float[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;
		// try (MemoryStack stack = MemoryStack.stackPush()) {
		GLES30.glBufferSubData(bufferType, 0, data);
		// }

		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
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
