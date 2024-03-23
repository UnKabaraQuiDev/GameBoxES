package lu.pcy113.pdr.engine.cache.attrib;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.joml.Matrix3x2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

public class Mat3x2fAttribArray extends AttribArray {

	private Matrix3x2f[] data;

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data, boolean iStatic) {
		super(name, index, dataSize, iStatic);
		this.data = data;
	}

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data, boolean iStatic, int divisor) {
		super(name, index, dataSize, iStatic, divisor);
		this.data = data;
	}

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}

	public Mat3x2fAttribArray(String name, int index, int dataSize, Matrix3x2f[] data, int bufferType, boolean iStatic, int divisor) {
		super(name, index, dataSize, bufferType, iStatic, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GL40.glBufferData(bufferType, toFlatArray(), iStatic ? GL40.GL_STATIC_DRAW : GL40.GL_DYNAMIC_DRAW);
	}

	public boolean update(Matrix3x2f[] nPos) {
		if (!iStatic && nPos.length != data.length) {
			return false;
		}
		data = nPos;

		GL40.glBufferSubData(GL40.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GL40.glGetError() == GL40.GL_NO_ERROR;
	}

	@Override
	public void enable() {
		for (int i = 0; i < 3; i++) {
			GL40.glEnableVertexAttribArray(index + i);
			GL40.glVertexAttribPointer(index + i, 2, GL40.GL_FLOAT, false, 6 * 3, i * 2 * 4);
			GL40.glVertexAttribDivisor(index + i, divisor);
		}
	}

	public FloatBuffer toFlatFloatBuffer() {
		FloatBuffer fb = BufferUtils.createFloatBuffer(data.length * 3 * 2);
		Arrays.stream(data).forEach(m -> m.get(fb));
		fb.flip();
		return fb;
	}

	public float[] toFlatArray() {
		/*
		 * float[] flatArray = new float[data.length * 3*2]; for(int i = 0; i <
		 * data.length; i++) { float[] dat = new float[3*2]; data[i].get(dat);
		 * System.arraycopy(dat, 0, flatArray, i*3*2, 3*2); } return flatArray;
		 */
		float[] flatArray = new float[data.length * 6];
		for (int i = 0; i < data.length; i++) {
			float[] dat = new float[3 * 2];
			data[i].get(dat);
			System.arraycopy(dat, 0, flatArray, i * 6, 6);
		}
		return flatArray;
	}

	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize * 3 * 2, toFlatArray(), bufferType, iStatic);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public Matrix3x2f[] getData() {
		return data;
	}

	public Matrix3x2f get(int i) {
		return data[i];
	}

}
