package lu.pcy113.pdr.engine.geom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.IntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.utils.Logger;

public class Gizmo implements UniqueID, Cleanupable, Renderable {
	
	public static final String NAME = Gizmo.class.getName();

	public static final float LINE_WIDTH = 2.5f;
	
	protected final String name;
	protected int vao = -1;
	protected final HashMap<Integer, Integer> vbo = new HashMap<>();
	
	protected FloatAttribArray vertices;
	protected UIntAttribArray indices;
	protected FloatAttribArray color;
	
	protected int vertexCount, indicesCount;
	
	/**
	 * Positions are stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Gizmo(String name, FloatAttribArray vertices, UIntAttribArray indices, FloatAttribArray color) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
		this.color = color;
		
		/*if(indices.getDataCount() != vertices.getDataCount()) {
			Logger.log(Level.WARNING, "Indices ("+indices.getLength()+"/"+indices.getDataSize()+"="+indices.getDataCount()+") must be equal to vertices ("+vertices.getLength()+"/"+vertices.getDataSize()+"="+vertices.getDataCount()+")");
		}*/
		
		this.vertexCount = vertices.getDataCount();
		this.indicesCount = indices.getLength();
		
		//System.out.println("gizmo vertices ("+(vertices.getLength()/3)+"*3): "+Arrays.toString(vertices.getData()));
		//System.out.println("gizmo color ("+(color.getLength()/4)+"*4): "+Arrays.toString(color.getData()));
		//System.out.println("gizmo indices ("+indices.getDataCount()+Arrays.toString(indices.getData()));
		
		this.vao = GL40.glGenVertexArrays();
		bind();
		storeElementArray(indices);
		vertices.setIndex(0);
		storeAttribArray(vertices);
		color.setIndex(1);
		storeAttribArray(color);
		unbind();
			
		Logger.log(Level.INFO, "Gizmo "+name+": "+vao+" & "+vbo);
	}
	
	protected void storeAttribArray(AttribArray data) {
		this.vbo.put(data.getIndex(), data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();
	}
	private void storeElementArray(UIntAttribArray indices) {
		indices.setBufferType(GL40.GL_ELEMENT_ARRAY_BUFFER);
		this.vbo.put(indices.getIndex(), indices.gen());
		indices.bind();
		indices.init();
	}
	
	public void bind() {
		GL40.glBindVertexArray(vao);
	}
	public void unbind() {
		GL40.glBindVertexArray(0);
	}
	
	@Override
	public void cleanup() {
		if(vao != -1) {
			GL40.glDeleteVertexArrays(vao);
			vertices.cleanup();
			indices.cleanup();
			vao = -1;
		}
	}
	
	@Override
	public String getId() {return name;}
	public int getVertexCount() {return vertexCount;}
	public int getVao() {return vao;}
	public HashMap<Integer, Integer> getVbo() {return vbo;}
	public String getName() {return name;}
	public UIntAttribArray getIndices() {return indices;}
	public FloatAttribArray getVertices() {return vertices;}
	public FloatAttribArray getColor() {return color;}
	public int getIndicesCount() {return indicesCount;}
	
}
