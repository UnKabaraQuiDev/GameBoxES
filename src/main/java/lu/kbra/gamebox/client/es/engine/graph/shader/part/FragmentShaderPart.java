package lu.kbra.gamebox.client.es.engine.graph.shader.part;

import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public class FragmentShaderPart extends AbstractShaderPart {

	public FragmentShaderPart(String file) {
		super(file, GL_W.GL_FRAGMENT_SHADER);
	}

}
