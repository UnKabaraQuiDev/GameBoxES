package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

public class Vector3fEncoder extends DefaultObjectEncoder<Vector3f> {

	public Vector3fEncoder() {
		super(Vector3f.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Vector3f obj) {
		ByteBuffer bb = ByteBuffer.allocate(estimateSize(head, obj));

		if (head) {
			bb.putShort(header());
		}

		bb.putFloat(obj.x);
		bb.putFloat(obj.y);
		bb.putFloat(obj.z);

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, Vector3f obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) + Float.BYTES * 3;
	}

}
