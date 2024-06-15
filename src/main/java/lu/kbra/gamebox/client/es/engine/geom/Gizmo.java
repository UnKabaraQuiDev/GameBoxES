package lu.kbra.gamebox.client.es.engine.geom;

import java.util.HashMap;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec4fAttribArray;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;

public class Gizmo implements UniqueID, Cleanupable, Renderable {

	public static final String NAME = Gizmo.class.getName();

	public static final float LINE_WIDTH = 2.5f;

	protected final String name;
	protected int vao = -1;
	protected HashMap<Integer, Integer> vbo = new HashMap<>();

	protected Vec3fAttribArray vertices;
	protected UIntAttribArray indices;
	protected Vec4fAttribArray color;

	protected int vertexCount, indicesCount;

	/**
	 * Positions are stored as attribArray 0, normals as attribArray 1, uvs as
	 * attribArray 2
	 */
	public Gizmo(String name, Vec3fAttribArray vertices, UIntAttribArray indices, Vec4fAttribArray color) {
		this.name = name;
		this.vertices = vertices;
		this.indices = indices;
		this.color = color;

		/*
		 * if(indices.getDataCount() != vertices.getDataCount()) {
		 * GlobalLogger.log(Level.WARNING,
		 * "Indices ("+indices.getLength()+"/"+indices.getDataSize()+"="+indices.
		 * getDataCount()+") must be equal to vertices ("+vertices.getLength()+"/"+
		 * vertices.getDataSize()+"="+vertices.getDataCount()+")"); }
		 */

		this.vertexCount = vertices.getDataCount();
		this.indicesCount = indices.getLength();

		// System.out.println("gizmo vertices ("+(vertices.getLength()/3)+"*3):
		// "+Arrays.toString(vertices.getData()));
		// System.out.println("gizmo color ("+(color.getLength()/4)+"*4):
		// "+Arrays.toString(color.getData()));
		// System.out.println("gizmo indices
		// ("+indices.getDataCount()+Arrays.toString(indices.getData()));

		this.vao = GL40.glGenVertexArrays();
		bind();
		storeElementArray(indices);
		vertices.setIndex(0);
		storeAttribArray(vertices);
		color.setIndex(1);
		storeAttribArray(color);
		unbind();

		GlobalLogger.log(Level.INFO, "Gizmo " + name + ": " + vao + " & " + vbo);
	}

	protected void storeAttribArray(AttribArray data) {
		this.vbo.put(data.getIndex(), data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();
	}

	private void storeElementArray(UIntAttribArray indices) {
		if (indices == null)
			throw new NullPointerException("UintAttribArray indices is null");
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
		GlobalLogger.log("Cleaning up: "+name+" ("+vao+")");
		
		if (vao == -1)
			return;
		
		GL40.glDeleteVertexArrays(vao);
		vbo = null;
		vertices.cleanup();
		vertices = null;
		indices.cleanup();
		indices = null;
		vao = -1;
	}

	@Override
	public String getId() {
		return name;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getVao() {
		return vao;
	}

	public HashMap<Integer, Integer> getVbo() {
		return vbo;
	}

	public String getName() {
		return name;
	}

	public UIntAttribArray getIndices() {
		return indices;
	}

	public Vec3fAttribArray getVertices() {
		return vertices;
	}

	public Vec4fAttribArray getColor() {
		return color;
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public static Gizmo newRect(String name, Vector2f scale, Vector4f textBoxColor) {
		return new Gizmo(name, new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(0, 0, 0), new Vector3f(scale.x, 0, 0), new Vector3f(scale.x, scale.y, 0), new Vector3f(0, scale.y, 0) }),
				new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 1, 2, 2, 3, 3, 0 }), new Vec4fAttribArray("color", 1, 1, new Vector4f[] { textBoxColor, textBoxColor, textBoxColor, textBoxColor }));
	}

}
