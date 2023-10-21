package lu.pcy113.pdr.engine.geom.text;

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
				new UIntAttribArray("ind", -1, 1, ArrayUtils.intCountingUp(0, size)),
				new UIntAttribArray("index", 1, 1, ArrayUtils.intCountingUp(0, size, 1, 4)));
	}

}