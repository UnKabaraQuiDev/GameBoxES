package lu.pcy113.pdr.engine.cache.attrib;

import org.lwjgl.opengl.GL40;

public class DrawBuffer extends IntAttribArray {

	public DrawBuffer(int primitiveCount, int instancesCount, int vertexStart, int vertexOffset, int instanceOffset) {
		super("draw", -2, 1, new int[] { primitiveCount, instancesCount, vertexStart, vertexOffset, instanceOffset }, GL40.GL_DRAW_INDIRECT_BUFFER, false);
	}

	public void setPrimitiveCount(int primitiveCount) {
		data[0] = primitiveCount;
		update();
	}

	public void setInstancesCount(int instancesCount) {
		data[1] = instancesCount;
		update();
	}

	public void setVertexStart(int vertexStart) {
		data[2] = vertexStart;
		update();
	}

	public void setVertexOffset(int vertexOffset) {
		data[3] = vertexOffset;
		update();
	}

	public void setInstanceOffset(int instanceOffset) {
		data[4] = instanceOffset;
		update();
	}

	public void set(int primitiveCount, int instancesCount, int vertexStart, int vertexOffset, int instanceOffset) {
		bind();
		update(new int[] { primitiveCount, instancesCount, vertexStart, vertexOffset, instanceOffset });
		unbind();
	}

}
