package lu.kbra.gamebox.client.es.engine.graph.composition;

import org.lwjgl.opengl.GL40;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;

public interface FramebufferAttachment extends Cleanupable {

	void bind(int id);
	
	default void bindUniform(int loc, int i) {
		GL40.glUniform1i(loc, i);
		PDRUtils.checkGlError("Uniform1i["+loc+"] = "+i);
	}

}
