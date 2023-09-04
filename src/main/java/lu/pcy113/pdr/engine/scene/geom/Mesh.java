package lu.pcy113.pdr.engine.scene.geom;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

/**
 * Represents a Mesh loaded in memory with these attributes as one-dimentional arrays of floats/ints:
 * - vertices, normals, uvs, indices (faces)
 */
public class Mesh implements Cleanupable, Renderable, UniqueID {
	
	protected final String name;
	
	protected final int vao;
	protected final List<Integer> vbos = new ArrayList<>();
	
	protected float[] vertices;
	protected float[] normals;
	protected float[] uvs;
	protected int[] indices;
	
	protected int numVertices;
	
	/**
	 * Positions should be stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Mesh(String name, float[] vertices, float[] normals, float[] uvs, int[] indices) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.uvs = uvs;
		this.indices = indices;
		
		this.numVertices = vertices.length / 3;
		
		this.vao = GL30.glGenVertexArrays();
		bind();
		storeAttribArray(1, 3, vertices);
		storeAttribArray(2, 3, normals);
		storeAttribArray(3, 2, uvs);
		storeElementArray(indices);
		unbind();
	}
	
	public void bind() {
		GL30.glBindVertexArray(this.vao);
	}
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	protected void storeAttribArray(int index, int size, float[] data) {
		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		GL30.glEnableVertexAttribArray(index);
		GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
	}
	private void storeElementArray(int[] indices2) {
		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices2, GL30.GL_STATIC_DRAW);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	/*private static FloatBuffer toFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}*/
	
	@Override
	public void cleanup() {
		GL30.glDeleteVertexArrays(vao);
		vbos.forEach(GL30::glDeleteBuffers);
	}
	
	@Override
	public String getID() {return name;}
	public float[] getVertices() {return vertices;}
	public float[] getNormals() {return normals;}
	public float[] getUvs() {return uvs;}
	public int[] getIndices() {return indices;}
	public int getNumVertices() {return numVertices;}
	
}
