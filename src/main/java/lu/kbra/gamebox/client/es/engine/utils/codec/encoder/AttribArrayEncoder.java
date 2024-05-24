package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.jbcodec.encoder.EncoderNotFoundException;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;

public class AttribArrayEncoder extends DefaultObjectEncoder<AttribArray> {

	public AttribArrayEncoder() {
		super(AttribArray.class);
	}

	@Override
	public ByteBuffer encode(boolean head, AttribArray obj) {
		if (obj instanceof UIntAttribArray) {
			return ((UIntAttribArrayEncoder) cm.getEncoderByClass(UIntAttribArray.class)).encode(true, (UIntAttribArray) obj);
		}

		throw new EncoderNotFoundException("Encoder not found for class: " + obj.getName());
	}

	@Override
	public boolean confirmClassType(Class<?> clazz) {
		return AttribArray.class.isInstance(clazz);
	}

}
