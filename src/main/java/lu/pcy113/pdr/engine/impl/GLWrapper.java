package lu.pcy113.pdr.engine.impl;

import java.util.HashMap;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.consts.BufferType;
import lu.pcy113.pdr.engine.utils.consts.TextureType;

// TODO: maybe
public class GLWrapper {
	
	private HashMap<Integer, Integer> bound = new HashMap<>();
	
	public boolean bindTexture(TextureType tt, int id) {
		if(this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}
		
		GL40.glBindTexture(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGlError();
	}
	
	public boolean bindBuffer(BufferType tt, int id) {
		if(this.bound.containsKey(tt.getGlId()) && this.bound.get(tt.getGlId()) == id) {
			return true;
		}
		
		GL40.glBindBuffer(tt.getGlId(), id);
		this.bound.put(tt.getGlId(), id);
		return PDRUtils.checkGlError();
	}
	
	// GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0
	public boolean drawElements() {
		//GL40.glDrawElements(tt, count, type, offset);
		return PDRUtils.checkGlError();
	}
	
}
