package lu.kbra.gamebox.client.es.engine.geom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.DrawBuffer;
import lu.kbra.gamebox.client.es.engine.cache.attrib.MultiAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;

public class Mesh implements UniqueID, Cleanupable, Renderable {

	public static final String NAME = Mesh.class.getName();

	protected final String name;
	protected int vao = -1;
	protected HashMap<Integer, Integer> vbo = new HashMap<>();
	protected Material material;

	protected Vec3fAttribArray vertices;
	protected UIntAttribArray indices;
	protected AttribArray[] attribs;

	protected int vertexCount, indicesCount;

	protected DrawBuffer drawBuffer;

	/**
	 * Positions are stored as attribArray 0, normals as attribArray 1, uvs as
	 * attribArray 2
	 */
	public Mesh(String name, Material material, Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		this.name = name;
		this.vertices = vertices;
		indices.setBufferType(GL40.GL_ELEMENT_ARRAY_BUFFER);
		this.indices = indices;
		this.material = material;
		this.attribs = attribs;

		this.vertexCount = vertices.getDataCount();
		this.indicesCount = indices.getLength();

		this.vao = GL40.glGenVertexArrays();
		bind();
		storeElementArray((UIntAttribArray) indices);
		vertices.setIndex(0);
		storeAttribArray((Vec3fAttribArray) vertices);

		for (AttribArray a : attribs) {
			if (vbo.containsKey(a.getIndex())) {
				GlobalLogger.log(Level.WARNING, "Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			storeAttribArray(a);
		}

		unbind();

		GlobalLogger.log(Level.INFO, "Mesh " + name + ": " + vao + " & " + vbo + "; v:" + vertexCount);
	}

	public void createDrawBuffer() {
		if(hasDrawBuffer())
			return;
		drawBuffer = new DrawBuffer(indicesCount, 1, 0, 0, 0);
		// drawBuffer = new IntAttribArray("draw", -2, 1, new int[] {indicesCount, 1, 4,
		// 0, 0}, GL46.GL_DRAW_INDIRECT_BUFFER, false);
		drawBuffer.gen();
		drawBuffer.bind();
		drawBuffer.init();
		drawBuffer.unbind();
	}

	/*
	 * public Mesh(String name2, String material2, Vec3fAttribArray pos,
	 * UIntAttribArray ind, Vec3fAttribArray norm, Vec2fAttribArray uv) {
	 * this(name2, material2, pos, ind, new AttribArray[] {norm, uv}); }
	 */

	private void storeAttribArray(AttribArray data) {
		this.vbo.put(data.getIndex(), data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();

		if (data instanceof MultiAttribArray) {
			MultiAttribArray ma = (MultiAttribArray) data;
			for (int a = ma.getMinIndex() + 1; a <= ma.getMaxIndex(); a++) {
				vbo.put(a, data.getBufferIndex());
			}
		}
	}

	public void addAttribArray(AttribArray data) {
		bind();

		this.vbo.put(data.getIndex(), data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();

		if (data instanceof MultiAttribArray) {
			MultiAttribArray ma = (MultiAttribArray) data;
			for (int a = ma.getMinIndex() + 1; a <= ma.getMaxIndex(); a++) {
				vbo.put(a, data.getBufferIndex());
			}
		}

		AttribArray[] newAttribs = new AttribArray[this.attribs.length + 1];
		System.arraycopy(this.attribs, 0, newAttribs, 0, this.attribs.length);
		newAttribs[this.attribs.length] = data;
		this.attribs = newAttribs;
	}

	private void storeElementArray(UIntAttribArray indices) {
		indices.setBufferType(GL40.GL_ELEMENT_ARRAY_BUFFER);
		this.vbo.put(indices.getIndex(), indices.gen());
		indices.bind();
		indices.init();
	}

	public void bind() {
		GL40.glBindVertexArray(vao);
		PDRUtils.checkGlError("BindVertexArray(" + vao + ") (" + name + ")");
	}

	public void unbind() {
		GL40.glBindVertexArray(0);
		PDRUtils.checkGlError("BindVertexArray(" + 0 + ") (" + name + ")");
	}

	@Override
	public void cleanup() {
		if (vao == -1)
			return;

		GL40.glDeleteVertexArrays(vao);
		Arrays.stream(attribs).forEach(AttribArray::cleanup);
		vao = -1;
		if(hasDrawBuffer()) {
			drawBuffer.cleanup();
		}
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

	public Material getMaterial() {
		return material;
	}

	public AttribArray[] getAttribs() {
		return attribs;
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	public DrawBuffer getDrawBuffer() {
		return drawBuffer;
	}

	public boolean hasDrawBuffer() {
		return drawBuffer != null;
	}

	@Override
	public String toString() {
		return "{" + name + " | VAO: " + vao + " | VBO: " + vbo + " | V: " + vertexCount + "/" + indicesCount + " | Attribs: " + Arrays.toString(attribs) + "}";
	}

	public static QuadMesh newQuad(String name, Material material2, Vector2f size) {
		return new QuadMesh(name, material2, size);
	}
	
	public static QuadMesh newQuad(GeoPlane plane, String name, Material material2, Vector2f size) {
		return new QuadMesh(name, material2, size, plane);
	}

	public static CubeMesh newCube(String name, Material material2, Vector3f size) {
		return new CubeMesh(name, material2, size);
	}

}
