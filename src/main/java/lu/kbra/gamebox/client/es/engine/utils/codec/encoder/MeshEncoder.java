package lu.kbra.gamebox.client.es.engine.utils.codec.encoder;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;

public class MeshEncoder extends DefaultObjectEncoder<Mesh> {

	public MeshEncoder() {
		super(Mesh.class);
	}

	@Override
	public ByteBuffer encode(boolean head, Mesh obj) {
		// name
		// attrib arrays
		// uint indices
		// vec3f vertices
		// ...

		String name = obj.getName();
		Material material = obj.getMaterial();
		UIntAttribArray indices = obj.getIndices();
		Vec3fAttribArray vertices = obj.getVertices();

		// generation

		ByteBuffer bb = ByteBuffer.allocate(estimateSize(head, obj));

		if (head) {
			bb.putShort(header);
		}

		bb.put(cm.encode(false, (String) name));

		bb.put(cm.encode(false, indices));
		bb.put(cm.encode(false, vertices));

		for (AttribArray arr : obj.getAttribs()) {
			bb.put(cm.encode(true, arr));
		}

		return (ByteBuffer) bb.flip();
	}

	@Override
	public int estimateSize(boolean head, Mesh obj) {
		int bufferLength = head ? CodecManager.HEAD_SIZE : 0;

		bufferLength += cm.estimateSize(false, obj.getName());

		bufferLength += cm.estimateSize(false, obj.getMaterial());
		bufferLength += cm.estimateSize(false, obj.getIndices());
		bufferLength += cm.estimateSize(false, obj.getVertices());

		for (AttribArray arr : obj.getAttribs()) {
			System.out.println(arr.getName() + " -> " + arr.getClass().getName());
			bufferLength += cm.estimateSize(true, arr);
		}

		return bufferLength;
	}

}
