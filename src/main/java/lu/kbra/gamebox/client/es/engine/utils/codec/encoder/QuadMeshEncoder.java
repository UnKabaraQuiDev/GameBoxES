package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.geom.QuadMesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

public class QuadMeshEncoder extends DefaultObjectEncoder<QuadMesh> {

	public QuadMeshEncoder() {
		super(QuadMesh.class);
	}

	@Override
	public ByteBuffer encode(boolean head, QuadMesh obj) {
		String name = obj.getName();
		Material material = obj.getMaterial();
		Vector2f size = obj.getSize();

		ByteBuffer bb = ByteBuffer.allocate(estimateSize(head, obj));

		if (head) {
			bb.putShort(header);
		}

		bb.put(cm.encode(false, (String) name));
		bb.put(cm.encode(false, (Material) material));
		bb.put(cm.encode(false, (Vector2f) size));

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, QuadMesh obj) {
		int bufferLength = head ? CodecManager.HEAD_SIZE : 0;

		bufferLength += cm.estimateSize(false, (String) obj.getName());

		bufferLength += cm.estimateSize(false, (Material) obj.getMaterial());

		bufferLength += cm.estimateSize(false, (Vector2f) obj.getSize());

		return bufferLength;
	}

}
