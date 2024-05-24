package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.jbcodec.encoder.StringEncoder;

import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.UIntAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT
 * divisor ; INT arrayLength ; INT[] data ; INT END
 */
public class UIntAttribArrayEncoder extends DefaultObjectEncoder<UIntAttribArray> {

	public UIntAttribArrayEncoder() {
		super(UIntAttribArray.class);
	}

	@Override
	public ByteBuffer encode(boolean head, UIntAttribArray obj) {
		String name = obj.getName();
		// int dataCount = obj.getDataCount();
		int dataSize = obj.getDataSize();
		int bufferType = obj.getBufferType();
		boolean _static = obj.isStatic();
		int divisor = obj.getDivisor();
		int index = obj.getIndex();
		// int length = obj.getLength();
		int[] data = obj.getData();

		// String name, int index, int dataSize, int bufferType, boolean iStatic, int
		// divisor
		// int: index, dataSize, bufferType, divisor, dataLength = 6

		int bufferLength = estimateSize(head, obj);
		System.out.println("alloc size: " + bufferLength);
		System.out.println("name : " + obj.getName());
		System.out.println("index; " + obj.getIndex());
		System.out.println("dataSize: " + obj.getDataSize());
		System.out.println("bufferType: " + obj.getBufferType());
		System.out.println("static: " + obj.isStatic());
		System.out.println("divisor: " + obj.getDivisor());
		System.out.println("data: " + data.length);

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

		// IntBuffer bbData =
		// ByteBuffer.allocate(data.length*Integer.BYTES).asIntBuffer();
		// bbData.put(data);
		// ((ArrayEncoder) cm.getEncoderByClass(Object[].class)).encode(false,
		// PDRUtils.toObjectArrya(data));
		ByteBuffer byteArray = PDRUtils.intArrayToByteBuffer(data);
		bb.put(byteArray);

		bb.putShort(UIntAttribArrayDecoder.END);

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, UIntAttribArray obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) +
				cm.estimateSize(false, obj.getName()) +
				5 * Integer.BYTES + // index, dataSize, bufferType, divisor, dataLength
				1 + // isStatic
				Integer.BYTES * obj.getData().length + // data
				2; // end short
	}

}
