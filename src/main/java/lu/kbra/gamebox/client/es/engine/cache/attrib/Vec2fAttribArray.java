package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector2f;
import org.lwjgl.opengles.GLES30;

public class Vec2fAttribArray extends AttribArray {

	private Vector2f[] data;

	public Vec2fAttribArray(String name, int index, int dataSize, Vector2f[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Vec2fAttribArray(String name, int index, int dataSize, Vector2f[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Vec2fAttribArray(String name, int index, int dataSize, Vector2f[] data, int bufferType, boolean _static) {
		super(name, index, dataSize, bufferType, _static);
		this.data = data;
	}
	
	public Vec2fAttribArray(String name, int index, int dataSize, Vector2f[] data, int bufferType, boolean _static, int divisor) {
		super(name, index, dataSize, bufferType, _static, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, toFlatArray(), iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER)
			GLES30.glVertexAttribPointer(index, dataSize * 2, GLES30.GL_FLOAT, false, 0, 0);
	}

	public boolean update(Vector2f[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return false;
		data = nPos;

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, toFlatArray());
		return GLES30.glGetError() == GLES30.GL_NO_ERROR;
	}

	public float[] toFlatArray() {
		float[] flatArray = new float[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			float[] dat = new float[2];
			Vector2f cdata = data[i];
			if (cdata != null) {
				dat[0] = cdata.x;
				dat[1] = cdata.y;
			} else
				Arrays.fill(dat, 0);
			System.arraycopy(dat, 0, flatArray, i * 2, 2);
		}
		return flatArray;
	}

	public FloatAttribArray toFloatAttribArray() {
		return new FloatAttribArray(name, index, dataSize * 2, toFlatArray(), bufferType, iStatic);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public Vector2f[] getData() {
		return data;
	}

	public Vector2f get(int i) {
		return data[i];
	}

}
