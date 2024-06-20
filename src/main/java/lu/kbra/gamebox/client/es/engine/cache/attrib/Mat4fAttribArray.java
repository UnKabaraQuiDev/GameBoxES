package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengles.GLES30;

public class Mat4fAttribArray extends AttribArray implements MultiAttribArray {

	private Matrix4f[] data;

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data, boolean iStatic) {
		super(name, index, dataSize, iStatic);
		this.data = data;
	}

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data, boolean iStatic, int divisor) {
		super(name, index, dataSize, iStatic, divisor);
		this.data = data;
	}

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}

	public Mat4fAttribArray(String name, int index, int dataSize, Matrix4f[] data, int bufferType, boolean iStatic, int divisor) {
		super(name, index, dataSize, bufferType, iStatic, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, toFloatArray(), iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
	}

	public boolean update(Matrix4f[] nPos) {
		if (!iStatic && nPos.length != data.length) {
			return false;
		}
		data = nPos;

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, toFloatArray());
		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
	}

	@Override
	public void enable() {
		for (int i = 0; i < 4; i++) {
			GLES30.glEnableVertexAttribArray(index + i);
			GLES30.glVertexAttribPointer(index + i, 4, GLES30.GL_FLOAT, false, 16 * 4, i * 4 * 4);
			GLES30.glVertexAttribDivisor(index + i, divisor);
		}
	}

	public FloatBuffer toFlatFloatBuffer() {
		FloatBuffer fb = BufferUtils.createFloatBuffer(data.length * 16);
		Arrays.stream(data).forEach(m -> m.get(fb));
		fb.flip();
		return fb;
	}

	public float[] toFloatArray() {
		float[] flatArray = new float[data.length * 16];
		for (int i = 0; i < data.length; i++) {
			float[] dat = new float[16];
			if (data[i] != null)
				data[i].get(dat);
			System.arraycopy(dat, 0, flatArray, i * 16, 16);
		}
		return flatArray;
	}

	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize * 16, toFloatArray(), bufferType, iStatic);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public Matrix4f[] getData() {
		return data;
	}

	public Matrix4f get(int i) {
		return data[i];
	}

	@Override
	public int getMinIndex() {
		return index;
	}

	@Override
	public int getMaxIndex() {
		return index + 3;
	}

	@Override
	public String toString() {
		return getBufferIndex() + "|" + getMaxIndex() + "-" + getMaxIndex() + ") " + getName() + ": " + getLength() + "/" + getDataSize() + "=" + getDataCount();
	}

}
