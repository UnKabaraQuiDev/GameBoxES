package lu.pcy113.pdr.engine.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Mesh implements UniqueID, Cleanupable {
	
	public static final String NAME = Mesh.class.getName();
	
	protected final String name;
	protected final int vao;
	protected final List<Integer> vbo;
	protected String material;
	
	protected float[] vertices;
	protected float[] normals;
	protected float[] uvs;
	protected int[] indices;
	
	protected int vertexCount;
	
	/**
	 * Positions are stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Mesh(String name, float[] vertices, float[] normals, float[] uvs, int[] indices, String material) {
		this.name = name;
		this.vertices = vertices;
		this.normals = normals;
		this.uvs = uvs;
		this.indices = indices;
		this.material = material;
		
		this.vertexCount = indices.length;
		this.vbo = new ArrayList<>();
		
		System.out.println("vertices ("+(vertices.length/3)+"*3): "+Arrays.toString(vertices));
		System.out.println("normals ("+(normals.length/3)+"*3): "+Arrays.toString(normals));
		System.out.println("uvs ("+(uvs.length/2)+"*2): "+Arrays.toString(uvs));
		System.out.println("indices ("+(indices.length/3)+"*3): "+Arrays.toString(indices));
		
		//try(MemoryStack stack = MemoryStack.stackPush()) {
			this.vao = GL30.glGenVertexArrays();
			bind();
			storeElementArray(indices);
			storeAttribArray(0, 3, vertices);
			storeAttribArray(1, 3, normals);
			storeAttribArray(2, 2, uvs);
			unbind();
		//}
	}
	
	protected void storeAttribArray(int index, int size, float[] data) {
		int vbo = GL30.glGenBuffers();
		this.vbo.add(vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		GL30.glEnableVertexAttribArray(index);
		GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	private void storeElementArray(int[] indices) {
		int vbo = GL30.glGenBuffers();
		this.vbo.add(vbo);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);
	}
	
	public void bind() {
		GL30.glBindVertexArray(vao);
	}
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
	
	@Override
	public void cleanup() {
		GL30.glDeleteVertexArrays(vao);
		vbo.forEach(GL30::glDeleteBuffers);
	}
	
	@Override
	public String getId() {return name;}
	public int getVertexCount() {return vertexCount;}
	public int getVao() {return vao;}
	public List<Integer> getVbo() {return vbo;}
	public String getName() {return name;}
	public float[] getNormals() {return normals;}
	public float[] getUvs() {return uvs;}
	public int[] getIndices() {return indices;}
	public String getMaterial() {return material;}
	public float[] getVertices() {return vertices;}
	
}
