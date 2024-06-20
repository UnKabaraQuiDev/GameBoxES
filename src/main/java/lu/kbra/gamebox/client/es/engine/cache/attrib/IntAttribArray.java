package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.lwjgl.opengles.GLES30;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;

public class IntAttribArray extends AttribArray {

	protected int[] data;

	public IntAttribArray(String name, int index, int dataSize, int[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public IntAttribArray(String name, int index, int dataSize, int[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public IntAttribArray(String name, int index, int dataSize, int[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}

	public IntAttribArray(String name, int index, int dataSize, int[] data, boolean _static, int divisor) {
		super(name, index, dataSize, _static, divisor);
		this.data = data;
	}

	public IntAttribArray(String name, int index, int dataSize, int[] data, int bufferType, boolean _static, int divisor) {
		super(name, index, dataSize, bufferType, _static, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, data, iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		PDRUtils.checkGlESError("BufferData(" + bufferType + ", " + Arrays.toString(data) + ", " + iStatic + ")");
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER && bufferType != GLES30.GL_UNIFORM_BUFFER) {
			GLES30.glVertexAttribPointer(index, dataSize, GLES30.GL_INT, false, 0, 0);
			PDRUtils.checkGlESError("VertexAttribPointer(" + index + ", " + dataSize + ", INT, FALSE, 0, 0)");
		}
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

	public boolean update() {
		return update(data);
	}

	public boolean update(int[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GLES30.glBufferSubData(bufferType, 0, data);

		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
	}

}
