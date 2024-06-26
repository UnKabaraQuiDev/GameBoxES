package lu.kbra.gamebox.client.es.engine.cache.attrib;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

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
		GL_W.glBufferData(bufferType, data, iStatic ? GL_W.GL_STATIC_DRAW : GL_W.GL_DYNAMIC_DRAW);
		if (bufferType != GL_W.GL_ELEMENT_ARRAY_BUFFER && bufferType != GL_W.GL_UNIFORM_BUFFER)
			GL_W.glVertexAttribPointer(index, dataSize, GL_W.GL_FLOAT, false, 0, 0);
	}

	public boolean update(float[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;
		// try (MemoryStack stack = MemoryStack.stackPush()) {
		GL_W.glBufferSubData(bufferType, 0, data);
		// }

		return GL_W.glGetError() == GL_W.GL_NO_ERROR;
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
