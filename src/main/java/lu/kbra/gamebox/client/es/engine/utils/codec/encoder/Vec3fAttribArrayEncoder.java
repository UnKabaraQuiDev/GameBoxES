package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import org.joml.Vector3f;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.jbcodec.encoder.Encoder;
import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vec3fAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT
 * divisor ; INT arrayLength ; INT[] data ; INT END
 */
public class Vec3fAttribArrayEncoder extends DefaultObjectEncoder<Vec3fAttribArray> {

	public Vec3fAttribArrayEncoder() {
		super(Vec3fAttribArray.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Vec3fAttribArray obj) {
		String name = obj.getName();
		// int dataCount = obj.getDataCount();
		int dataSize = obj.getDataSize();
		int bufferType = obj.getBufferType();
		boolean _static = obj.isStatic();
		int divisor = obj.getDivisor();
		int index = obj.getIndex();
		// int length = obj.getLength();
		Vector3f[] data = obj.getData();

		// String name, int index, int dataSize, int bufferType, boolean iStatic, int
		// divisor
		// int: index, dataSize, bufferType, divisor, dataLength = 6

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
		
		ByteBuffer bbName = ((Encoder<String>) cm.getEncoderByClass(String.class)).encode(false, name);
		bb.put(bbName);

		bb.putInt(index);
		bb.putInt(dataSize);
		bb.putInt(bufferType);
		bb.put((byte) (_static ? 1 : 0));
		bb.putInt(divisor);
		bb.putInt(data.length);

		Encoder<Vector3f> enc = cm.getEncoderByClass(Vector3f.class);
		
		for(Vector3f v : data) {
			bb.put(enc.encode(false, v));
		}
		
		bb.putShort(Vec3fAttribArrayDecoder.END);
		
		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, Vec3fAttribArray obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) +
				cm.estimateSize(false, obj.getName()) +
				5 * Integer.BYTES + // index, dataSize, bufferType, divisor, dataLength
				1 + // isStatic
				cm.estimateSize(false, new Vector3f()) * obj.getData().length + // data
				2; // end short
	}

}
