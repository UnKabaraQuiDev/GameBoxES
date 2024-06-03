package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

public class Vector2fDecoder extends DefaultObjectDecoder<Vector2f> {

	public Vector2fDecoder() {
		super(Vector2f.class);
	}

	@Override
	public Vector2f decode(boolean head, ByteBuffer bb) {
		verifyHeader(head, bb);

		return new Vector2f(bb.getFloat(), bb.getFloat());
	}

}
