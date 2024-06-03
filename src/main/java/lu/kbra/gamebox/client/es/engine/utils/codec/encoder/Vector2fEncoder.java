package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

public class Vector2fEncoder extends DefaultObjectEncoder<Vector2f> {

	public Vector2fEncoder() {
		super(Vector2f.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Vector2f obj) {
		ByteBuffer bb = ByteBuffer.allocate(estimateSize(head, obj));

		if (head) {
			bb.putShort(header());
		}

		bb.putFloat(obj.x);
		bb.putFloat(obj.y);

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, Vector2f obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) + Float.BYTES * 2;
	}

}
