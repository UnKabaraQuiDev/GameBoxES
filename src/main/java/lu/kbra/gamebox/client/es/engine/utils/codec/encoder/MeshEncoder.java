package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

public class MeshEncoder extends DefaultObjectEncoder<Mesh> {

	public MeshEncoder() {
		super(Mesh.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Mesh obj) {
		String name = obj.getName();
		Material material = obj.getMaterial();
		UIntAttribArray indices = obj.getIndices();
		Vec3fAttribArray vertices = obj.getVertices();

		final int allocSize = estimateSize(head, obj);
		ByteBuffer bb = ByteBuffer.allocateDirect(allocSize);

		if (head) {
			bb.putShort(header);
		}

		bb.put(cm.encode(false, (String) name));
		bb.put(cm.encode(false, (Material) material));

		bb.put(cm.encode(false, (UIntAttribArray) indices));
		bb.put(cm.encode(false, (Vec3fAttribArray) vertices));

		bb.put(cm.encode(false, (int) obj.getAttribs().length));

		for (AttribArray arr : obj.getAttribs()) {
			ByteBuffer bb2 = cm.encode(true, arr);
			bb.put(bb2);
		}

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, Mesh obj) {
		int bufferLength = head ? CodecManager.HEAD_SIZE : 0;

		bufferLength += cm.estimateSize(false, (String) obj.getName());

		bufferLength += cm.estimateSize(false, (Material) obj.getMaterial());
		bufferLength += cm.estimateSize(false, (UIntAttribArray) obj.getIndices());
		bufferLength += cm.estimateSize(false, (Vec3fAttribArray) obj.getVertices());

		bufferLength += cm.estimateSize(false, (int) obj.getAttribs().length);

		for (AttribArray arr : obj.getAttribs()) {
			bufferLength += cm.estimateSize(true, arr);
		}

		return bufferLength;
	}

}
