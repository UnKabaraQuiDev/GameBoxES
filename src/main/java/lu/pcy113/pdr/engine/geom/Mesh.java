package lu.pcy113.pdr.engine.geom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.MultiAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;

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

	/**
	 * Positions are stored as attribArray 0, normals as attribArray 1, uvs as
	 * attribArray 2
	 */
	public Mesh(String name, Material material, Vec3fAttribArray vertices, UIntAttribArray indices, AttribArray... attribs) {
		this.name = name;
		this.vertices = vertices;
		indices.setBufferType(
				GL40.GL_ELEMENT_ARRAY_BUFFER);
		this.indices = indices;
		this.material = material;
		this.attribs = attribs;

		this.vertexCount = vertices.getDataCount();
		this.indicesCount = indices.getLength();

		this.vao = GL40.glGenVertexArrays();
		bind();
		storeElementArray(
				(UIntAttribArray) indices);
		vertices.setIndex(
				0);
		storeAttribArray(
				(Vec3fAttribArray) vertices);

		for (AttribArray a : attribs) {
			if (vbo.containsKey(
					a.getIndex())) {
				GlobalLogger.log(
						Level.WARNING,
						"Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			storeAttribArray(
					a);
		}

		unbind();

		GlobalLogger.log(
				Level.INFO,
				"Mesh " + name + ": " + vao + " & " + vbo + "; v:" + vertexCount);
	}

	/*
	 * public Mesh(String name2, String material2, Vec3fAttribArray pos,
	 * UIntAttribArray ind, Vec3fAttribArray norm, Vec2fAttribArray uv) {
	 * this(name2, material2, pos, ind, new AttribArray[] {norm, uv}); }
	 */

	private void storeAttribArray(AttribArray data) {
		this.vbo.put(
				data.getIndex(),
				data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();

		if (data instanceof MultiAttribArray) {
			MultiAttribArray ma = (MultiAttribArray) data;
			for (int a = ma.getMinIndex() + 1; a <= ma.getMaxIndex(); a++) {
				vbo.put(
						a,
						data.getVbo());
			}
		}
	}

	public void addAttribArray(AttribArray data) {
		bind();

		this.vbo.put(
				data.getIndex(),
				data.gen());
		data.bind();
		data.init();
		data.enable();
		data.unbind();

		if (data instanceof MultiAttribArray) {
			MultiAttribArray ma = (MultiAttribArray) data;
			for (int a = ma.getMinIndex() + 1; a <= ma.getMaxIndex(); a++) {
				vbo.put(
						a,
						data.getVbo());
			}
		}

		AttribArray[] newAttribs = new AttribArray[this.attribs.length + 1];
		System.arraycopy(
				this.attribs,
				0,
				newAttribs,
				0,
				this.attribs.length);
		newAttribs[this.attribs.length] = data;
		this.attribs = newAttribs;
	}

	private void storeElementArray(UIntAttribArray indices) {
		indices.setBufferType(
				GL40.GL_ELEMENT_ARRAY_BUFFER);
		this.vbo.put(
				indices.getIndex(),
				indices.gen());
		indices.bind();
		indices.init();
	}

	public void bind() {
		GL40.glBindVertexArray(
				vao);
		PDRUtils.checkGlError(
				"BindVertexArray(" + vao + ") (" + name + ")");
	}

	public void unbind() {
		GL40.glBindVertexArray(
				0);
		PDRUtils.checkGlError(
				"BindVertexArray(" + 0 + ") (" + name + ")");
	}

	@Override
	public void cleanup() {
		if (vao == -1)
			return;

		GL40.glDeleteVertexArrays(
				vao);
		Arrays.stream(
				attribs).forEach(
						AttribArray::cleanup);
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

	public Material getMaterial() {
		return material;
	}

	public AttribArray[] getAttribs() {
		return attribs;
	}

	public int getIndicesCount() {
		return indicesCount;
	}

	@Override
	public String toString() {
		return "{" + name + " | VAO: " + vao + " | VBO: " + vbo + " | V: " + vertexCount + "/" + indicesCount + " | Attribs: " + Arrays.toString(
				attribs) + "}";
	}

	public static Mesh newQuad(String name, Material material2, Vector2f size) {
		Mesh mesh = new Mesh(
				name,
				material2,
				new Vec3fAttribArray(
						"pos",
						0,
						1,
						new Vector3f[] { new Vector3f(
								-1f,
								-1f,
								0f).mul(
										size.x,
										size.y,
										0)
								.div(
										2),
								new Vector3f(
										1f,
										-1f,
										0f).mul(
												size.x,
												size.y,
												0)
										.div(
												2),
								new Vector3f(
										1f,
										1f,
										0f).mul(
												size.x,
												size.y,
												0)
										.div(
												2),
								new Vector3f(
										-1f,
										1f,
										0f).mul(
												size.x,
												size.y,
												0)
										.div(
												2), }),
				new UIntAttribArray(
						"ind",
						-1,
						1,
						new int[] { 0, 1, 2, 0, 2, 3 },
						GL40.GL_ELEMENT_ARRAY_BUFFER),
				new Vec3fAttribArray(
						"normal",
						1,
						1,
						new Vector3f[] { new Vector3f(
								0,
								0,
								1),
								new Vector3f(
										0,
										0,
										1),
								new Vector3f(
										0,
										0,
										1),
								new Vector3f(
										0,
										0,
										1) }),
				new Vec2fAttribArray(
						"uv",
						2,
						1,
						new Vector2f[] { new Vector2f(
								0,
								0),
								new Vector2f(
										1,
										0),
								new Vector2f(
										1,
										1),
								new Vector2f(
										0,
										1), }));
		return mesh;
	}

	public static Mesh newCube(String name, Material material2, Vector3f size) {
		Mesh mesh = new Mesh(
				name,
				material2,
				new Vec3fAttribArray(
						"pos",
						0,
						1,
						new Vector3f[] { new Vector3f(
								-1,
								-1,
								-1).mul(
										size.x,
										size.y,
										size.z)
								.div(
										2),
								new Vector3f(
										1,
										-1,
										-1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										1,
										1,
										-1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										-1,
										1,
										-1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										-1,
										-1,
										1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										1,
										-1,
										1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										1,
										1,
										1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2),
								new Vector3f(
										-1,
										1,
										1).mul(
												size.x,
												size.y,
												size.z)
										.div(
												2) }),
				new UIntAttribArray(
						"ind",
						-1,
						1,
						new int[] { 0, 1, 2, 2, 3, 0, 1, 5, 6, 6, 2, 1, 7, 6, 5, 5, 4, 7, 4, 0, 3, 3, 7, 4, 4, 5, 1, 1, 0, 4, 3, 2, 6, 6, 7, 3 },
						GL40.GL_ELEMENT_ARRAY_BUFFER),
				new Vec3fAttribArray(
						"normal",
						1,
						1,
						new Vector3f[] { new Vector3f(
								-1,
								-1,
								-1).normalize(),
								new Vector3f(
										1,
										-1,
										-1).normalize(),
								new Vector3f(
										1,
										1,
										-1).normalize(),
								new Vector3f(
										-1,
										1,
										-1).normalize(),
								new Vector3f(
										-1,
										-1,
										1).normalize(),
								new Vector3f(
										1,
										-1,
										1).normalize(),
								new Vector3f(
										1,
										1,
										1).normalize(),
								new Vector3f(
										-1,
										1,
										1).normalize() }),
				new Vec2fAttribArray(
						"uv",
						2,
						1, // TODO: 3d uvs ?
						new Vector2f[] { new Vector2f(
								0,
								0),
								new Vector2f(
										1,
										0),
								new Vector2f(
										1,
										1),
								new Vector2f(
										0,
										1),
								new Vector2f(
										0,
										0),
								new Vector2f(
										1,
										0),
								new Vector2f(
										1,
										1),
								new Vector2f(
										0,
										1) }));

		return mesh;
	}

}
