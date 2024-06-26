package lu.kbra.gamebox.client.es.engine.utils.codec.decoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.geom.QuadMesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

public class QuadMeshDecoder extends DefaultObjectDecoder<QuadMesh> {

	public QuadMeshDecoder() {
		super(QuadMesh.class);
	}

	@Override
	public QuadMesh decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		
		String name = cm.getDecoderByClass(String.class).decode(false, bb);
		Material material = cm.getDecoderByClass(Material.class).decode(false, bb);
		Vector2f size = cm.getDecoderByClass(Vector2f.class).decode(false, bb);
		
		return new QuadMesh(name, material, size);
	}

}
