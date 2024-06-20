package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.util.Arrays;

import org.joml.Vector3i;
import org.lwjgl.opengles.GLES30;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;

public class Vec3iAttribArray extends AttribArray {

	private Vector3i[] data;

	public Vec3iAttribArray(String name, int index, int dataSize, Vector3i[] data) {
		super(name, index, dataSize);
		this.data = data;
	}

	public Vec3iAttribArray(String name, int index, int dataSize, Vector3i[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
	}

	public Vec3iAttribArray(String name, int index, int dataSize, Vector3i[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
	}

	public Vec3iAttribArray(String name, int index, int dataSize, Vector3i[] data, boolean _static, int divisor) {
		super(name, index, dataSize, _static, divisor);
		this.data = data;
	}

	public Vec3iAttribArray(String name, int index, int dataSize, Vector3i[] data, int bufferType, boolean _static, int divisor) {
		super(name, index, dataSize, bufferType, _static, divisor);
		this.data = data;
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, toFlatArray(), iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER)
			GLES30.glVertexAttribPointer(index, dataSize, GLES30.GL_INT, false, 0, 0);
	}

	public boolean update(Vector3i[] nPos) {
		if (!iStatic && nPos.length != data.length)
			throw new IllegalArgumentException("Array's size cannot change");
		data = nPos;

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, toFlatArray());
		return PDRUtils.checkGlESError();
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public int[] toFlatArray() {
		int[] flatArray = new int[data.length * 3];
		for (int i = 0; i < data.length; i++) {
			int[] dat = new int[3];
			Vector3i cdata = data[i];
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

	public IntAttribArray toIntAttribArray() {
		return new IntAttribArray(name, index, dataSize * 3, toFlatArray(), bufferType, iStatic);
	}
	public UIntAttribArray toUIntAttribArray() {
		return new UIntAttribArray(name, index, dataSize * 3, toFlatArray(), bufferType, iStatic);
	}


	public Vector3i[] getData() {
		return data;
	}

	public Vector3i get(int i) {
		return data[i];
	}

}
