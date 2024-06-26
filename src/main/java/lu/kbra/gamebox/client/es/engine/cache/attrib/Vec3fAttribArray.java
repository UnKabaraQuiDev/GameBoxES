package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public class Vec3fAttribArray extends AttribArray {

	private Vector3f[] data;

	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, int bufferType, boolean _static) {
		super(name, index, dataSize, bufferType, _static);
		this.data = data;
	}

	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, int bufferType, boolean _static, int _divisor) {
		super(name, index, dataSize, bufferType, _static, _divisor);
		this.data = data;
	}

	public Vec3fAttribArray(String name, int index, int dataSize, Vector3f[] data, boolean _static) {
		super(name, index, dataSize, _static);
		this.data = data;
	}

	@Override
	public void init() {
		GL_W.glBufferData(bufferType, toFlatArray(), iStatic ? GL_W.GL_STATIC_DRAW : GL_W.GL_DYNAMIC_DRAW);
		if (bufferType != GL_W.GL_ELEMENT_ARRAY_BUFFER && bufferType != GL_W.GL_UNIFORM_BUFFER)
			GL_W.glVertexAttribPointer(index, dataSize * 3, GL_W.GL_FLOAT, false, 0, 0);
	}

	public boolean update(Vector3f[] nPos) {
		if (!iStatic && nPos.length != data.length)
			throw new IllegalArgumentException("Array's size cannot change");
		data = nPos;

		GL_W.glBufferSubData(GL_W.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GL_W.glGetError() == GL_W.GL_NO_ERROR;
	}

	public float[] toFlatArray() {
		float[] flatArray = new float[data.length * 3];
		for (int i = 0; i < data.length; i++) {
			float[] dat = new float[3];
			Vector3f cdata = data[i];
			if (cdata != null) {
				dat[0] = cdata.x;
				dat[1] = cdata.y;
				dat[2] = cdata.z;
			} else
				Arrays.fill(dat, 0);
			System.arraycopy(dat, 0, flatArray, i * 3, 3);
		}
		return flatArray;
	}

	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize * 3, toFlatArray(), bufferType, iStatic);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public Vector3f[] getData() {
		return data;
	}

	public Vector3f get(int i) {
		return data[i];
	}

}
