package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;

public class AttribArrayEncoder extends DefaultObjectEncoder<AttribArray> {
	
	public AttribArrayEncoder() {
		super(AttribArray.class);
	}
	
	@Override
	public ByteBuffer encode(boolean head, AttribArray obj) {
		return null;
	}
	
}
