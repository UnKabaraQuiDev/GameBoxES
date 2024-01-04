package lu.pcy113.pdr.engine.graph.texture;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public abstract class Texture implements Cleanupable, UniqueID {
	
	protected final String path;
	protected final String name;
	protected int tid = -1;
	protected int filter, txtResType = GL20.GL_TEXTURE_2D, wrap = GL40.GL_CLAMP_TO_EDGE;
	
	public Texture(String _name, String _path, int _filter, int _txtResType, int _wrap) {
		this.path = _name;
		this.name = _path;
		this.filter = _filter;
		this.txtResType = _txtResType;
	}
	
	protected int gen() {
		return (tid = GL40.glGenTextures());
	}
	
	public void bind(int i) {
		if (i > 31)
			return;
		GL40.glActiveTexture(GL40.GL_TEXTURE0 + i);
		bind();
	}
	
	public void bind() {
		GL40.glBindTexture(txtResType, tid);
	}
	
	public void unbind(int i) {
		if (i > 31)
			return;
		GL40.glActiveTexture(GL40.GL_TEXTURE0 + i);
		unbind();
	}
	
	public void unbind() {
		GL40.glBindTexture(txtResType, 0);
	}
	
	public void filter() {
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_MIN_FILTER, filter);
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_MAG_FILTER, filter);
	}
	
	public void wrap() {
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_WRAP_S, wrap);
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_WRAP_T, wrap);
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_WRAP_R, wrap);
	}
	
	@Override
	public void cleanup() {
		if (tid != -1) {
			GL40.glDeleteTextures(tid);
			tid = -1;
		}
	}
	
	@Override
	public String getId() {
		return name;
	}
	
	public int getTid() {
		return tid;
	}
	
	public String getPath() {
		return path;
	}
	
	public int getFilter() {
		return filter;
	}
	
	public int getTxtResType() {
		return txtResType;
	}
	
	public int getWrap() {
		return wrap;
	}
	
	public static int getColorByChannels(int channels) {
		switch (channels) {
		case 1:
			return GL40.GL_R;
		case 2:
			return GL40.GL_RG;
		case 3:
			return GL40.GL_RGB;
		case 4:
			return GL40.GL_RGBA;
		}
		return -1;
	}
	
}
