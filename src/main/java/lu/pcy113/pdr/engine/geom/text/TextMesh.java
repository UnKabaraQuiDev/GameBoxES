package lu.pcy113.pdr.engine.geom.text;

import java.util.Arrays;

import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.utils.ArrayUtils;

public class TextMesh extends Mesh {
	
	public static final String NAME = TextMesh.class.getName();
	
	public TextMesh(int size) {
		super(NAME+"_"+size,
				null,
				new FloatAttribArray("pos", 0, 3, new float[size*4*3], false),
				new UIntAttribArray("ind", -1, 1, ArrayUtils.intCountingUpTriQuads(size)),
				new UIntAttribArray("index", 1, 1, ArrayUtils.intCountingUp(0, size, 1, 4)),
				new FloatAttribArray("uv", 2, 2, ArrayUtils.floatRepeating(new float[] {
						0, 1,
						1, 1,
						1, 0,
						0, 0}, size)));
		System.out.println("inde: "+Arrays.toString(((FloatAttribArray) attribs[1]).getData()));
	}

}
