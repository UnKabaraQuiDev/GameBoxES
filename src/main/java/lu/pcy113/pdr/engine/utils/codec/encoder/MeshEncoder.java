package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.jb.codec.encoder.StringEncoder;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
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
		//bufferLength += bbMaterial.capacity();
		
		bufferLength += cm.estimateSize(false, obj.getIndices());
		
		for(AttribArray arr : obj.getAttribs()) {
			System.out.println(arr.getName());
			//bufferLength += cm.estimateSize(true, arr);
		}
		
		System.out.println(bufferLength);
		
		StringEncoder stringEncoder = (StringEncoder) cm.getEncoderByClass(String.class);
		bufferLength += stringEncoder.estimateSize(false, name);
		
		System.out.println(bufferLength);
		
		ByteBuffer bb = ByteBuffer.allocate(bufferLength);
		
		bb.put(stringEncoder.encode(false, name));
		
		//bb.put(bbMaterial);
		
		bb.put(cm.encode(false, obj.getIndices()));
		
		for(AttribArray arr : obj.getAttribs()) {
			//bb.put(cm.encode(true, arr));
		}
		
		return (ByteBuffer) bb.flip();
	}
	
}
