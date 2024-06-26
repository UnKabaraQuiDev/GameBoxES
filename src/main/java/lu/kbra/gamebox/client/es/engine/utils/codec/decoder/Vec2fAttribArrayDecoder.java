package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.jbcodec.decoder.Decoder;
import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;
import lu.pcy113.jbcodec.decoder.StringDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; INT arrayLength ; Vector2f[] data ; INT END
 */
public class Vec2fAttribArrayDecoder extends DefaultObjectDecoder<Vec2fAttribArray> {

	public static final short END = 32325;

	public Vec2fAttribArrayDecoder() {
		super(Vec2fAttribArray.class);
	}

	@Override
	public Vec2fAttribArray decode(boolean head, ByteBuffer bb) {
		verifyHeader(head, bb);

		String name = ((StringDecoder) cm.getDecoderByClass(String.class)).decode(false, bb);

		int index = bb.getInt();
		int dataSize = bb.getInt();
		int bufferType = bb.getInt();
		boolean _static = bb.get() != 0;
		int divisor = bb.getInt();
		int arrayLength = bb.getInt();
		Vector2f[] data = new Vector2f[arrayLength];

		Decoder<Vector2f> dec = cm.getDecoderByClass(Vector2f.class);

		for (int i = 0; i < arrayLength; i++) {
			data[i] = dec.decode(false, bb);
		}

		if (bb.getShort() != END) {
			throw new RuntimeException("Error happened while decoding Vec3fAttribArray.");
		}

		return new Vec2fAttribArray(name, index, dataSize, data, bufferType, _static, divisor);
	}

}
