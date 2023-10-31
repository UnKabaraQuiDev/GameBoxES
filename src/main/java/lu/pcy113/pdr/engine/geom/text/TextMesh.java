package lu.pcy113.pdr.engine.geom.text;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.utils.ArrayUtils;

public class TextMesh extends Mesh {
	
	public static final String NAME = TextMesh.class.getName();
	
	public TextMesh(int size) {
		super(NAME+"_"+size,
				null,
				new Vec3fAttribArray("pos", 0, 1, new Vector3f[size*4], false),
				new UIntAttribArray("ind", -1, 1, ArrayUtils.intCountingUpTriQuads(size)),
				new UIntAttribArray("index", 1, 1, ArrayUtils.intCountingUp(0, size, 1, 4)),
				new Vec2fAttribArray("uv", 2, 1, ArrayUtils.vec2Repeating(new Vector2f[] {
						new Vector2f(0, 1),
						new Vector2f(1, 1),
						new Vector2f(1, 0),
						new Vector2f(0, 0)}, size)));
		System.out.println("inde: "+Arrays.toString(((Vec2fAttribArray) attribs[1]).getData()));
	}

}
