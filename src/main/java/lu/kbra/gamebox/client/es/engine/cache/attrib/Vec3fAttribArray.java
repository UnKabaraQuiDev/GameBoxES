package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector3f;
import org.lwjgl.opengles.GLES30;

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
		GLES30.glBufferData(bufferType, toFlatArray(), iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER && bufferType != GLES30.GL_UNIFORM_BUFFER)
			GLES30.glVertexAttribPointer(index, dataSize * 3, GLES30.GL_FLOAT, false, 0, 0);
	}

	public boolean update(Vector3f[] nPos) {
		if (!iStatic && nPos.length != data.length)
			throw new IllegalArgumentException("Array's size cannot change");
		data = nPos;

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
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
