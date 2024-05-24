package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;

public class MeshDecoder extends DefaultObjectDecoder<Mesh> {

	public MeshDecoder() {
		super(Mesh.class);
	}

	@Override
	public Mesh decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		
		String name = cm.getDecoderByClass(String.class).decode(false, bb);
		Material material = cm.getDecoderByClass(Material.class).decode(false, bb);
		UIntAttribArray indices = cm.getDecoderByClass(UIntAttribArray.class).decode(false, bb);
		Vec3fAttribArray vertices = cm.getDecoderByClass(Vec3fAttribArray.class).decode(false, bb);
		
		return null;
	}

}
