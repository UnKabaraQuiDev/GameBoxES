package lu.kbra.gamebox.client.es.engine.geom;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public class QuadMesh extends Mesh {

	private Vector2f size;
	
	public QuadMesh(String name, Material material, Vector2f size) {
		super(name, material,
				new Vec3fAttribArray("pos", 0, 1,
						new Vector3f[] {
								new Vector3f(-1f, -1f, 0f).mul(size.x, size.y, 0).div(2),
								new Vector3f(1f, -1f, 0f).mul(size.x, size.y, 0).div(2),
								new Vector3f(1f, 1f, 0f).mul(size.x, size.y, 0).div(2),
								new Vector3f(-1f, 1f, 0f).mul(size.x, size.y, 0).div(2)
						}),
				new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER),
				new Vec3fAttribArray("normal", 1, 1, new Vector3f[] {
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1)
				}),
				new Vec2fAttribArray("uv", 2, 1, new Vector2f[] {
						new Vector2f(0, 0),
						new Vector2f(1, 0),
						new Vector2f(1, 1),
						new Vector2f(0, 1),
				}));
		
		this.size = size;
	}
	
	@Deprecated
	public QuadMesh(String name, Material material2, Vector2f size, GeoPlane plane) {
		super(name, material2,
				new Vec3fAttribArray("pos", 0, 1,
						new Vector3f[] {
								new Vector3f(-1f, -1f, -1f).mul(plane.project(size)).div(2),
								new Vector3f(1f, -1f, 1f).mul(plane.project(size)).div(2),
								new Vector3f(1f, 1f, 1f).mul(plane.project(size)).div(2),
								new Vector3f(-1f, 1f, -1f).mul(plane.project(size)).div(2)
				}),
				new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER),
				new Vec3fAttribArray("normal", 1, 1, new Vector3f[] {
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 1)
				}),
				new Vec2fAttribArray("uv", 2, 1, new Vector2f[] {
						new Vector2f(0, 0),
						new Vector2f(1, 0),
						new Vector2f(1, 1),
						new Vector2f(0, 1)
				}));
		
		this.size = plane.getBoundingPlane(vertices.getData());
	}

	public Vector2f getSize() {
		return size;
	}

}
