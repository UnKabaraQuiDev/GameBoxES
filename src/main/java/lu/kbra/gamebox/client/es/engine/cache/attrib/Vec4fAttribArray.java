package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

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
		GL_W.glBufferData(bufferType, toFlatArray(), iStatic ? GL_W.GL_STATIC_DRAW : GL_W.GL_DYNAMIC_DRAW);
		if (bufferType != GL_W.GL_ELEMENT_ARRAY_BUFFER)
			GL_W.glVertexAttribPointer(index, dataSize * 4, GL_W.GL_FLOAT, false, 0, 0);
	}

	public boolean update(Vector4f[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GL_W.glBufferSubData(GL_W.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GL_W.glGetError() == GL_W.GL_NO_ERROR;
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
