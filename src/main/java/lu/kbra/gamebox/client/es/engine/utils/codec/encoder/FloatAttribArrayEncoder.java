package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.jbcodec.encoder.StringEncoder;
import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.attrib.FloatAttribArray;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.FloatAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; INT arrayLength ; FLOAT[] data ; INT END
 */
public class FloatAttribArrayEncoder extends DefaultObjectEncoder<FloatAttribArray> {

	public FloatAttribArrayEncoder() {
		super(FloatAttribArray.class);
	}

	@Override
	public ByteBuffer encode(boolean head, FloatAttribArray obj) {
		String name = obj.getName();
		int dataSize = obj.getDataSize();
		int bufferType = obj.getBufferType();
		boolean _static = obj.isStatic();
		int divisor = obj.getDivisor();
		int index = obj.getIndex();
		float[] data = obj.getData();

		int bufferLength = estimateSize(head, obj);
		GlobalLogger.log("alloc size: " + bufferLength);
		GlobalLogger.log("name : " + obj.getName());
		GlobalLogger.log("index; " + obj.getIndex());
		GlobalLogger.log("dataSize: " + obj.getDataSize());
		GlobalLogger.log("bufferType: " + obj.getBufferType());
		GlobalLogger.log("static: " + obj.isStatic());
		GlobalLogger.log("divisor: " + obj.getDivisor());
		GlobalLogger.log("data: " + data.length);

		ByteBuffer bb = ByteBuffer.allocate(bufferLength);

		if (head) {
			bb.putShort(header);
		}

		ByteBuffer bbName = ((StringEncoder) cm.getEncoderByClass(String.class)).encode(false, name);
		bb.put(bbName);
		bbName.clear();
		bbName = null;

		bb.putInt(index);
		bb.putInt(dataSize);
		bb.putInt(bufferType);
		bb.put((byte) (_static ? 1 : 0));
		bb.putInt(divisor);
		bb.putInt(data.length);

		for (float d : data) {
			bb.putFloat(d);
		}

		bb.putShort(FloatAttribArrayDecoder.END);

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, FloatAttribArray obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) + cm.estimateSize(false, obj.getName()) + 5 * Integer.BYTES + // index, dataSize, bufferType, divisor, dataLength
				1 + // isStatic
				Integer.BYTES * obj.getData().length + // data
				2; // end short
	}

}
