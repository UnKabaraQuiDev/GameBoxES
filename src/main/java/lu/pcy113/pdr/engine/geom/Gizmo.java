package lu.pcy113.pdr.engine.geom;

import java.util.Arrays;
import java.util.HashMap;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.IntAttribArray;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Gizmo implements UniqueID, Cleanupable {
	
	public static final String NAME = Gizmo.class.getName();
	
	protected final String name;
	protected final int vao;
	protected final HashMap<Integer, Integer> vbo = new HashMap<>();
	
	protected FloatAttribArray vertices;
	protected IntAttribArray indices;
	protected FloatAttribArray color;
	
	protected int vertexCount;
	
	/**
	 * Positions are stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Gizmo(String name, FloatAttribArray vertices, IntAttribArray indices, FloatAttribArray color) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
		this.color = color;
		
		/*if(indices.getDataCount() != vertices.getDataCount()) {
			Logger.log(Level.WARNING, "Indices ("+indices.getLength()+"/"+indices.getDataSize()+"="+indices.getDataCount()+") must be equal to vertices ("+vertices.getLength()+"/"+vertices.getDataSize()+"="+vertices.getDataCount()+")");
		}*/
		
		this.vertexCount = indices.getDataCount();
		
		System.out.println("gizmo vertices ("+(vertices.getLength()/3)+"*3): "+Arrays.toString(vertices.getData()));
		System.out.println("gizmo color ("+(color.getLength()/4)+"*4): "+Arrays.toString(color.getData()));
		System.out.println("gizmo indices ("+indices.getDataCount()+Arrays.toString(indices.getData()));
		
		this.vao = GL30.glGenVertexArrays();
		bind();
		storeElementArray(indices);
		storeAttribArray(0, 3, vertices);
		storeAttribArray(1, 4, color);
		unbind();
			
		System.out.println(vbo);
	}
	
	protected void storeAttribArray(int index, int size, IntAttribArray data) {
		int vbo = GL30.glGenBuffers();
		this.vbo.put(index, vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data.getData(), GL30.GL_STATIC_DRAW);
		GL30.glEnableVertexAttribArray(index);
		GL30.glVertexAttribPointer(index, size, GL30.GL_UNSIGNED_INT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	protected void storeAttribArray(int index, int size, FloatAttribArray data) {
		int vbo = GL30.glGenBuffers();
		this.vbo.put(index, vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data.getData(), GL30.GL_STATIC_DRAW);
		GL30.glEnableVertexAttribArray(index);
		GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	private void storeElementArray(IntAttribArray indices) {
		int vbo = GL30.glGenBuffers();
		this.vbo.put(-1, vbo);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices.getData(), GL30.GL_STATIC_DRAW);
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
		vbo.values().forEach(GL30::glDeleteBuffers);
	}
	
	@Override
	public String getId() {return name;}
	public int getVertexCount() {return vertexCount;}
	public int getVao() {return vao;}
	public HashMap<Integer, Integer> getVbo() {return vbo;}
	public String getName() {return name;}
	public IntAttribArray getIndices() {return indices;}
	public FloatAttribArray getVertices() {return vertices;}
	public FloatAttribArray getColor() {return color;}
	
}
