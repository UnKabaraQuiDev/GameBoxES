package lu.kbra.gamebox.client.es.engine.graph.composition;

import org.lwjgl.opengles.GLES30;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;

public interface FramebufferAttachment extends Cleanupable {

	void bind(int id);
	
	default void bindUniform(int loc, int i) {
		GLES30.glUniform1i(loc, i);
		PDRUtils.checkGlESError("Uniform1i["+loc+"] = "+i);
	}

}
