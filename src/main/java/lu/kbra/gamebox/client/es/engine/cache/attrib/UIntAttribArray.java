package lu.kbra.gamebox.client.es.engine.cache.attrib;

import org.lwjgl.opengles.GLES30;

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

	public UIntAttribArray(String name, int index, int dataSize, int[] data, int bufferType, boolean _static) {
		super(name, index, dataSize, bufferType, _static);
		this.data = data;
	}

	public UIntAttribArray(String name, int index, int dataSize, int[] data, boolean _static, int divisor) {
		super(name, index, dataSize, _static, divisor);
		this.data = data;
	}

	public UIntAttribArray(String name, int index, int dataSize, int[] data, int bufferType, boolean _static, int divisor) {
		super(name, index, dataSize, bufferType, _static, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, data, iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER)
			GLES30.glVertexAttribIPointer(index, dataSize, GLES30.GL_UNSIGNED_INT, 0, 0);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public int[] getData() {
		return data;
	}

	public Integer get(int i) {
		return data[i];
	}

	public boolean update(int[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GLES30.glBufferSubData(bufferType, 0, data);
		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
	}

}
