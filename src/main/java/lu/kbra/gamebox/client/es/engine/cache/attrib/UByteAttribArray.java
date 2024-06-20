package lu.kbra.gamebox.client.es.engine.cache.attrib;

import java.nio.ByteBuffer;

import org.lwjgl.opengles.GLES30;

public class UByteAttribArray extends AttribArray {

	private byte[] data;
	private ByteBuffer bbuffer;

	public UByteAttribArray(String name, int index, int dataSize, byte[] data) {
		super(name, index, dataSize);
		this.data = data;
		this.bbuffer = ByteBuffer.wrap(data);
	}

	public UByteAttribArray(String name, int index, int dataSize, byte[] data, int bufferType) {
		super(name, index, dataSize, bufferType);
		this.data = data;
		this.bbuffer = ByteBuffer.wrap(data);
	}

	public UByteAttribArray(String name, int index, int dataSize, byte[] data, int bufferType, boolean s) {
		super(name, index, dataSize, bufferType, s);
		this.data = data;
		this.bbuffer = ByteBuffer.wrap(data);
	}

	public UByteAttribArray(String name, int index, int dataSize, byte[] data, boolean _static, int divisor) {
		super(name, index, dataSize, _static, divisor);
		this.data = data;
		this.bbuffer = ByteBuffer.wrap(data);
	}

	@Override
	public void init() {
		GLES30.glBufferData(bufferType, bbuffer, iStatic ? GLES30.GL_STATIC_DRAW : GLES30.GL_DYNAMIC_DRAW);
		if (bufferType != GLES30.GL_ELEMENT_ARRAY_BUFFER)
			GLES30.glVertexAttribIPointer(index, dataSize, GLES30.GL_UNSIGNED_BYTE, 0, 0);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	public byte[] getData() {
		return data;
	}

	public Byte get(int i) {
		return data[i];
	}

	public void update(byte[] nPos) {
		if (!iStatic && nPos.length != data.length)
			return;
		data = nPos;

		bbuffer.position(0);
		bbuffer.put(data);

		GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, 0, bbuffer);
	}

}
