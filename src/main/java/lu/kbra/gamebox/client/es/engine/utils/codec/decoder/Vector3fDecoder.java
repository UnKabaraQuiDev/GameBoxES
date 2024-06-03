package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

public class Vector3fDecoder extends DefaultObjectDecoder<Vector3f> {

	public Vector3fDecoder() {
		super(Vector3f.class);
	}

	@Override
	public Vector3f decode(boolean head, ByteBuffer bb) {
		verifyHeader(head, bb);

		return new Vector3f(bb.getFloat(), bb.getFloat(), bb.getFloat());
	}

}
