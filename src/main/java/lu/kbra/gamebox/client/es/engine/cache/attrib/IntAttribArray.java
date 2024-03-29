package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL46;

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
		GL40.glBufferData(bufferType, data, iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
		PDRUtils.checkGlError("BufferData(" + bufferType + ", " + Arrays.toString(data) + ", " + iStatic + ")");
		if (bufferType != GL40.GL_ELEMENT_ARRAY_BUFFER && bufferType != GL46.GL_UNIFORM_BUFFER && bufferType != GL46.GL_DRAW_INDIRECT_BUFFER) {
			GL40.glVertexAttribPointer(index, dataSize, GL40.GL_INT, false, 0, 0);
			PDRUtils.checkGlError("VertexAttribPointer(" + index + ", " + dataSize + ", INT, FALSE, 0, 0)");
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

		GL40.glBufferSubData(bufferType, 0, data);

		return GL40.glGetError() == GL40.GL_NO_ERROR;
	}

}