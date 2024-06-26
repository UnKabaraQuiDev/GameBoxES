package lu.kbra.gamebox.client.es.engine.geom;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.UIntAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec2fAttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Vec3fAttribArray;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public class CubeMesh extends Mesh {

	private Vector3f size;

	public CubeMesh(String name, Material material2, Vector3f size) {
		super(name, material2,
				new Vec3fAttribArray("pos", 0, 1,
						new Vector3f[] { new Vector3f(-1, -1, -1).mul(size.x, size.y, size.z).div(2), new Vector3f(1, -1, -1).mul(size.x, size.y, size.z).div(2), new Vector3f(1, 1, -1).mul(size.x, size.y, size.z).div(2),
								new Vector3f(-1, 1, -1).mul(size.x, size.y, size.z).div(2), new Vector3f(-1, -1, 1).mul(size.x, size.y, size.z).div(2), new Vector3f(1, -1, 1).mul(size.x, size.y, size.z).div(2),
								new Vector3f(1, 1, 1).mul(size.x, size.y, size.z).div(2), new Vector3f(-1, 1, 1).mul(size.x, size.y, size.z).div(2) }),
				new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 2, 3, 0, 1, 5, 6, 6, 2, 1, 7, 6, 5, 5, 4, 7, 4, 0, 3, 3, 7, 4, 4, 5, 1, 1, 0, 4, 3, 2, 6, 6, 7, 3 }, GL_W.GL_ELEMENT_ARRAY_BUFFER),
				new Vec3fAttribArray("normal", 1, 1,
						new Vector3f[] { new Vector3f(-1, -1, -1).normalize(), new Vector3f(1, -1, -1).normalize(), new Vector3f(1, 1, -1).normalize(), new Vector3f(-1, 1, -1).normalize(), new Vector3f(-1, -1, 1).normalize(),
								new Vector3f(1, -1, 1).normalize(), new Vector3f(1, 1, 1).normalize(), new Vector3f(-1, 1, 1).normalize() }),
				new Vec2fAttribArray("uv", 2, 1, // TODO: 3d uvs ?
						new Vector2f[] { new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1) }));
		
		this.size = size;
	}
	
	public Vector3f getSize() {
		return size;
	}

}
