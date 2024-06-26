package lu.kbra.gamebox.client.es.engine.impl;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.BufferType;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

// TODO: maybe
public class GLWrapper {

	private HashMap<Integer, Integer> bound = new HashMap<>();

	public boolean bindTexture(TextureType tt, int id) {
		if (this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}

		GL_W.glBindTexture(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGL_WError();
	}

	public boolean bindBuffer(BufferType tt, int id) {
		if (this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}

		GL_W.glBindBuffer(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGL_WError();
	}

	// GL_W.GL_TRIANGLES, mesh.getIndicesCount(), GL_W.GL_UNSIGNED_INT, 0
	public boolean drawElements() {
		// GL_W.glDrawElements(tt, count, type, offset);
		return PDRUtils.checkGL_WError();
	}

}
