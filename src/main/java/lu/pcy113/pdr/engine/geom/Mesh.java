package lu.pcy113.pdr.engine.geom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.utils.Logger;

public class Mesh implements UniqueID, Cleanupable, Renderable {
	
	public static final String NAME = Mesh.class.getName();
	
	protected final String name;
	protected int vao = -1;
	protected HashMap<Integer, Integer> vbo = new HashMap<>();
	protected String material;
	
	protected FloatAttribArray vertices;
	protected UIntAttribArray indices;
	protected AttribArray[] attribs;
	
	protected int vertexCount, indicesCount;
	
	/**
	 * Positions are stored as attribArray 0,
	 * normals as attribArray 1,
	 * uvs as attribArray 2
	 */
	public Mesh(String name, String material, FloatAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
		this.material = material;
		this.attribs = attribs;
		
		this.vertexCount = vertices.getDataCount();
		this.indicesCount = indices.getLength();
		
		this.vao = GL40.glGenVertexArrays();
		bind();
		storeElementArray((UIntAttribArray) indices);
		vertices.setIndex(0);
		storeAttribArray((FloatAttribArray) vertices);
		
		for(AttribArray a : attribs) {
			if(vbo.containsKey(a.getIndex())) {
				Logger.log(Level.WARNING, "Duplicate of index: "+a.getIndex()+" from "+a.getName()+", in Mesh: "+name);
				continue;
			}
			storeAttribArray(a);
		}
		
		unbind();
		
		Logger.log(Level.INFO, "Mesh "+name+": "+vao+" & "+vbo+"; v:"+vertexCount);
	}
	
	public void storeAttribArray(AttribArray data) {
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
		if(vao == -1)
			return;
		
		GL40.glDeleteVertexArrays(vao);
		Arrays.stream(attribs).forEach(AttribArray::cleanup);
		vao = -1;
	}
	
	@Override
	public String getId() {return name;}
	public int getVertexCount() {return vertexCount;}
	public int getVao() {return vao;}
	public HashMap<Integer, Integer> getVbo() {return vbo;}
	public String getName() {return name;}
	public UIntAttribArray getIndices() {return indices;}
	public FloatAttribArray getVertices() {return vertices;}
	public String getMaterial() {return material;}
	public AttribArray[] getAttribs() {return attribs;}
	public int getIndicesCount() {return indicesCount;}
	
}
