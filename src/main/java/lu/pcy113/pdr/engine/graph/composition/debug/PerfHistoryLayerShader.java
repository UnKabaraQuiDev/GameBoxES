package lu.pcy113.pdr.engine.graph.composition.debug;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class PerfHistoryLayerShader extends Shader {

	public static final String NAME = PerfHistoryLayerShader.class.getName();

	public static final String DR = "dfps";
	public static final String DU = "dups";
	public static final String TR = "tfps";
	public static final String TU = "tups";

	public static final int MAX = 60;

	public PerfHistoryLayerShader() {
		super(NAME, new ShaderPart("./resources/shaders/composite/plain.vert"),
				new ShaderPart("./resources/shaders/composite/debug/debug.frag"));
	}

	@Override
	public void createUniforms() {
		// vert
		// None
		// frag
		getUniform(DU);
		getUniform(DR);
		getUniform(TU);
		getUniform(TR);
	}

}
