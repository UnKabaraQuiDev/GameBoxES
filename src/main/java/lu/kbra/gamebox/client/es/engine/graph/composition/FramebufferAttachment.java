package lu.kbra.gamebox.client.es.engine.graph.composition;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public interface FramebufferAttachment extends Cleanupable {

	void bind(int id);
	
	default void bindUniform(int loc, int i) {
		GL_W.glUniform1i(loc, i);
		PDRUtils.checkGL_WError("Uniform1i["+loc+"] = "+i);
	}

}
