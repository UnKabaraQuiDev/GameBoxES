package lu.kbra.gamebox.client.es.engine.graph.shader.part;

import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public class VertexShaderPart extends AbstractShaderPart {

	public VertexShaderPart(String file) {
		super(file, GL_W.GL_VERTEX_SHADER);
	}

}
