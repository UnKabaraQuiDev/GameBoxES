package lu.kbra.gamebox.client.es.engine.impl;

import java.util.HashMap;

import org.lwjgl.opengles.GLES30;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.BufferType;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;

// TODO: maybe
public class GLWrapper {

	private HashMap<Integer, Integer> bound = new HashMap<>();

	public boolean bindTexture(TextureType tt, int id) {
		if (this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}

		GLES30.glBindTexture(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGlESError();
	}

	public boolean bindBuffer(BufferType tt, int id) {
		if (this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}

		GLES30.glBindBuffer(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGlESError();
	}

	// GLES30.GL_TRIANGLES, mesh.getIndicesCount(), GLES30.GL_UNSIGNED_INT, 0
	public boolean drawElements() {
		// GLES30.glDrawElements(tt, count, type, offset);
		return PDRUtils.checkGlESError();
	}

}
