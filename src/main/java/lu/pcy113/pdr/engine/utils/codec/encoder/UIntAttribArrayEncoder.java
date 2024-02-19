package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.CodecManager;
import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.jb.codec.encoder.StringEncoder;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.codec.decoder.UIntAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; INT arrayLength ; INT[] data ; INT END
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
		
		// String name, int index, int dataSize, int bufferType, boolean iStatic, int divisor
		
		int bufferLength = estimateSize(head, obj);
		
		ByteBuffer bb = ByteBuffer.allocate(bufferLength);
		
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
		
		// IntBuffer bbData = ByteBuffer.allocate(data.length*Integer.BYTES).asIntBuffer();
		// bbData.put(data);
		// ((ArrayEncoder) cm.getEncoderByClass(Object[].class)).encode(false, PDRUtils.toObjectArrya(data));
		bb.put(PDRUtils.intArrayToByteBuffer(data));
		
		bb.putShort(UIntAttribArrayDecoder.END);
		
		return (ByteBuffer) bb.flip();
	}
	
	@Override
	public int estimateSize(boolean head, UIntAttribArray obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) 
				+obj.getName().length()*Character.BYTES
				+4*Integer.BYTES +1
				+Integer.BYTES*obj.getData().length;
	}
	
}
