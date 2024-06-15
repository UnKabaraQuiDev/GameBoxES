package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.jbcodec.encoder.Encoder;
import lu.pcy113.jbcodec.encoder.StringEncoder;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vec2fAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT
 * divisor ; INT arrayLength ; INT[] data ; INT END
 */
public class Vec2fAttribArrayEncoder extends DefaultObjectEncoder<Vec2fAttribArray> {

	public Vec2fAttribArrayEncoder() {
		super(Vec2fAttribArray.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Vec2fAttribArray obj) {
		String name = obj.getName();
		// int dataCount = obj.getDataCount();
		int dataSize = obj.getDataSize();
		int bufferType = obj.getBufferType();
		boolean _static = obj.isStatic();
		int divisor = obj.getDivisor();
		int index = obj.getIndex();
		// int length = obj.getLength();
		Vector2f[] data = obj.getData();

		// String name, int index, int dataSize, int bufferType, boolean iStatic, int
		// divisor
		// int: index, dataSize, bufferType, divisor, dataLength = 6

		int bufferLength = estimateSize(head, obj);
		GlobalLogger.info("alloc size: " + bufferLength);
		GlobalLogger.info("name : " + obj.getName());
		GlobalLogger.info("index; " + obj.getIndex());
		GlobalLogger.info("dataSize: " + obj.getDataSize());
		GlobalLogger.info("bufferType: " + obj.getBufferType());
		GlobalLogger.info("static: " + obj.isStatic());
		GlobalLogger.info("divisor: " + obj.getDivisor());
		GlobalLogger.info("data: " + data.length);

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

		Encoder<Vector2f> enc = cm.getEncoderByClass(Vector2f.class);
		
		for(Vector2f v : data) {
			bb.put(enc.encode(false, v));
		}
		
		bb.putShort(Vec2fAttribArrayDecoder.END);

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, Vec2fAttribArray obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) +
				cm.estimateSize(false, obj.getName()) +
				5 * Integer.BYTES + // index, dataSize, bufferType, divisor, dataLength
				1 + // isStatic
				cm.estimateSize(false, new Vector2f()) * obj.getData().length + // data
				2; // end short
	}

}
