package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

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
		GL_W.glBufferData(bufferType, data, iStatic ? GL_W.GL_STATIC_DRAW : GL_W.GL_DYNAMIC_DRAW);
		PDRUtils.checkGL_WError("BufferData(" + bufferType + ", " + Arrays.toString(data) + ", " + iStatic + ")");
		if (bufferType != GL_W.GL_ELEMENT_ARRAY_BUFFER && bufferType != GL_W.GL_UNIFORM_BUFFER) {
			GL_W.glVertexAttribPointer(index, dataSize, GL_W.GL_INT, false, 0, 0);
			PDRUtils.checkGL_WError("VertexAttribPointer(" + index + ", " + dataSize + ", INT, FALSE, 0, 0)");
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

		GL_W.glBufferSubData(bufferType, 0, data);

		return GL_W.glGetError() == GL_W.GL_NO_ERROR;
	}

}
