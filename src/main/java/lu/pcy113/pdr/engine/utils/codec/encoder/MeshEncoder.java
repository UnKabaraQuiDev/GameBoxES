package lu.pcy113.pdr.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.pdr.engine.geom.Mesh;

public class MeshEncoder extends DefaultObjectEncoder<Mesh> {

	public MeshEncoder() {
		super(Mesh.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Mesh obj) {
		// name
		// attrib arrays
		
		return null;
	}
	
}
