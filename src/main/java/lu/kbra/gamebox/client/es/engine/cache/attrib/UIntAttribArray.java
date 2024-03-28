package lu.kbra.gamebox.client.es.engine.cache.attrib;

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
		GL40.glBufferData(bufferType, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		if (bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER)
			GL40.glVertexAttribIPointer(index, dataSize, GL40.GL_UNSIGNED_INT, 0, 0);
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

		GL40.glBufferSubData(bufferType, 0, data);
		return GL40.glGetError() == GL40.GL_NO_ERROR;
	}

}
