package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.jb.codec.encoder.StringEncoder;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;

public class MeshEncoder extends DefaultObjectEncoder<Mesh> {

	public MeshEncoder() {
		super(Mesh.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Mesh obj) {
		// name
		// attrib arrays
		
		String name = obj.getName();
		Material material = obj.getMaterial();
		
		int bufferLength = head ? 2 : 0;
		
		ByteBuffer bbMaterial = null;//((MaterialEncoder) cm.getEncoderByClass(Material.class)).encode(false, material);
		bufferLength += bbMaterial.capacity();
		
		ByteBuffer[] bbArrays = new ByteBuffer[obj.getAttribs().length];
		for(int i = 0; i < obj.getAttribs().length; i++) {
			bbArrays[i] = cm.encode(false, obj.getAttribs()[i]);
			bufferLength += bbArrays[i].capacity();
		}
		
		StringEncoder stringEncoder = (StringEncoder) cm.getEncoderByClass(String.class);
		bufferLength += stringEncoder.estimateSize(false, name);
		
		ByteBuffer bb = ByteBuffer.allocate(bufferLength);
		
		bb.put(stringEncoder.encode(false, name));
		
		bb.put(bbMaterial);
		
		for(ByteBuffer bbArray : bbArrays) {
			bb.put(bbArray);
		}
		
		return (ByteBuffer) bb.flip();
	}
	
}
