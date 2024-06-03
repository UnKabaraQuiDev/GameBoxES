package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import lu.pcy113.jbcodec.decoder.Decoder;
import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;
import lu.pcy113.jbcodec.decoder.StringDecoder;

import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; INT arrayLength ; Vector3f[] data ; INT END
 */
public class Vec3fAttribArrayDecoder extends DefaultObjectDecoder<Vec3fAttribArray> {

	public static final short END = 32324;

	public Vec3fAttribArrayDecoder() {
		super(Vec3fAttribArray.class);
	}

	@Override
	public Vec3fAttribArray decode(boolean head, ByteBuffer bb) {
		verifyHeader(head, bb);

		String name = ((StringDecoder) cm.getDecoderByClass(String.class)).decode(false, bb);

		int index = bb.getInt();
		int dataSize = bb.getInt();
		int bufferType = bb.getInt();
		boolean _static = bb.get() != 0;
		int divisor = bb.getInt();
		int arrayLength = bb.getInt();
		Vector3f[] data = new Vector3f[arrayLength];

		Decoder<Vector3f> dec = cm.getDecoderByClass(Vector3f.class);

		for (int i = 0; i < arrayLength; i++) {
			data[i] = dec.decode(false, bb);
		}

		if (bb.getShort() != END) {
			throw new RuntimeException("Error happened while decoding Vec3fAttribArray.");
		}

		return new Vec3fAttribArray(name, index, dataSize, data, bufferType, _static, divisor);
	}

}
