package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector4f;
import org.lwjgl.opengles.GLES30;

public class Vec4fAttribArray extends AttribArray {

	private Vector4f[] data;

	public Vec4fAttribArray(String name, int index, int dataSize, Vector4f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Vec4fAttribArray(String name, int index, int dataSize, Vector4f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Vec4fAttribArray(String name, int index, int dataSize, Vector4f[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, toFlatArray(), iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER)
			GLES30.glVertexAttribPointer(index, dataSize * 4, GLES30.GL_FLOAT, false, 0, 0);
	}

	public boolean update(Vector4f[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
	}

	public float[] toFlatArray() {
		float[] flatArray = new float[data.length * 4];
		for (int i = 0; i < data.length; i++) {
			float[] dat = new float[4];
			Vector4f cdata = data[i];
			if (cdata != null) {
				dat[0] = cdata.x;
				dat[1] = cdata.y;
				dat[2] = cdata.z;
				dat[3] = cdata.w;
			} else
				Arrays.fill(dat, 0);
			System.arraycopy(dat, 0, flatArray, i * 4, 4);
		}
		return flatArray;
	}

	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize * 4, toFlatArray(), bufferType, iStatic);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public Vector4f[] getData() {
		return data;
	}

	public Vector4f get(int i) {
		return data[i];
	}

}
