package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;
import lu.pcy113.jbcodec.decoder.StringDecoder;

import lu.kbra.gamebox.client.es.engine.cache.attrib.FloatAttribArray;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; INT arrayLength ; FLOAT[] data ; INT END
 */
public class FloatAttribArrayDecoder extends DefaultObjectDecoder<FloatAttribArray> {

	public static final short END = 32322;

	public FloatAttribArrayDecoder() {
		super(FloatAttribArray.class);
	}

	@Override
	public FloatAttribArray decode(boolean head, ByteBuffer bb) {
		verifyHeader(head, bb);

		String name = ((StringDecoder) cm.getDecoderByClass(String.class)).decode(false, bb);

		int index = bb.getInt();
		int dataSize = bb.getInt();
		int bufferType = bb.getInt();
		boolean _static = bb.get() != 0;
		int divisor = bb.getInt();
		int arrayLength = bb.getInt();
		float[] data = new float[arrayLength];

		for (int i = 0; i < arrayLength; i++) {
			data[i] = bb.getFloat();
		}

		if (bb.getShort() != END) {
			throw new RuntimeException("Error happened while decoding FloatAttribArray.");
		}

		return new FloatAttribArray(name, index, dataSize, data, bufferType, _static, divisor);
	}

}
