package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.ArrayEncoder;
import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.jb.codec.encoder.StringEncoder;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.codec.decoder.UIntAttribArrayDecoder;

/**
 * STRING name ; INT index ; INT dataSize ; INT bufferType ; BOOL _static ; INT divisor ; ARRAY data ; INT END
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
		
		int bufferLength = name.length()+ 4*Integer.BYTES +1 +4*data.length;
		
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
		
		ByteBuffer bbData = ((ArrayEncoder) cm.getEncoderByClass(Object[].class)).encode(false, PDRUtils.toObjectArrya(data));
		bb.put(bbData);
		
		bb.putShort(UIntAttribArrayDecoder.END);
		
		return (ByteBuffer) bb.flip();
	}
	
}
