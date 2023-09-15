package lu.pcy113.pdr.engine.geom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.IntAttribArray;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.utils.Logger;

public class Mesh implements UniqueID, Cleanupable {
	
	public static final String NAME = Mesh.class.getName();
	
	protected final String name;
	protected final int vao;
	protected final HashMap<Integer, Integer> vbo = new HashMap<>();
	protected String material;
	
	protected FloatAttribArray vertices;
	protected IntAttribArray indices;
	protected AttribArray[] attribs;
	
	protected int vertexCount;
	
	/**
	 * Positions are stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Mesh(String name, String material, FloatAttribArray vertices, IntAttribArray indices, AttribArray... attribs) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
		this.material = material;
		this.attribs = attribs;
		
		/*if(indices.getDataCount() != vertices.getDataCount()) {
			Logger.log(Level.WARNING, "Indices ("+indices.getLength()+"/"+indices.getDataSize()+"="+indices.getDataCount()+") must be equal to vertices ("+vertices.getLength()+"/"+vertices.getDataSize()+"="+vertices.getDataCount()+")");
		}*/
		
		this.vertexCount = indices.getDataCount();
		
		System.out.println("vertices ("+(vertices.getLength()/3)+"*3): "+Arrays.toString(vertices.getData()));
		//System.out.println("normals ("+(normals.length/3)+"*3): "+Arrays.toString(normals));
		//System.out.println("uvs ("+(uvs.length/2)+"*2): "+Arrays.toString(uvs));
		System.out.println("indices ("+(indices.getDataCount()/3)+"*3): "+Arrays.toString(indices.getData()));
		
		//try(MemoryStack stack = MemoryStack.stackPush()) {
			this.vao = GL30.glGenVertexArrays();
			bind();
			storeElementArray(indices);
			storeAttribArray(0, 3, vertices);
			
			for(AttribArray a : attribs) {
				if(vbo.containsKey(a.getIndex())) {
					Logger.log(Level.WARNING, "Duplicate of index: "+a.getIndex()+" from "+a.getName()+", in Mesh: "+name);
					continue;
				}
				if(a instanceof FloatAttribArray) {
					storeAttribArray(a.getIndex(), a.getDataSize(), (FloatAttribArray) a);
				}else if(a instanceof IntAttribArray) {
					storeAttribArray(a.getIndex(), a.getDataSize(), (IntAttribArray) a);
				}
			}
			
			/*storeAttribArray(1, 3, normals);
			storeAttribArray(2, 2, uvs);*/
			unbind();
		//}
			
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
	public String getMaterial() {return material;}
	public AttribArray[] getAttribs() {return attribs;}
	
}
