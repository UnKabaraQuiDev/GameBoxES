package lu.pcy113.pdr.engine.graph.texture;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.consts.DataType;
import lu.pcy113.pdr.engine.utils.consts.TexelFormat;
import lu.pcy113.pdr.engine.utils.consts.TexelInternalFormat;
import lu.pcy113.pdr.engine.utils.consts.TextureType;

public abstract class Texture implements Cleanupable, UniqueID {
	
	protected final String path;
	protected final String name;
	protected int tid = -1;
	protected TextureFilter filter = TextureFiler.LINEAR;
	protected TextureType txtType = TextureType.TXT2D;
	protected TextureWrap wrap = TextureWrap.CLAMP_TO_EDGE;
	protected DataType dataType;
	protected TexelFormat format = TexelFormat.RGB;
	protected TexelInternalFormat internalFormat = TexelInternalFormat.RGB;
	protected boolean generateMipmaps = false;
	protected TextureOperation textureOperation = null;
	
	public Texture(String _name, String _path, TextureOperation txtOp) {
		this.path = _name;
		this.name = _path;
		this.textureOperation = txtOp;
		/*this.filter = _filter;
		this.txtResType = _txtResType;
		this.wrap = _wrap;*/
	}
	
	public abstract boolean setup();
	
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
		GL40.glBindTexture(txtType.getGlId(), tid);
	}
	
	public void unbind(int i) {
		if (i > 31)
			return;
		GL40.glActiveTexture(GL40.GL_TEXTURE0 + i);
		unbind();
	}
	
	public void unbind() {
		GL40.glBindTexture(txtType.getGlId(), 0);
	}
	
	public void applyFilter() {
		GL40.glTexParameteri(txtType.getGlId(), GL40.GL_TEXTURE_MIN_FILTER, filter.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), GL40.GL_TEXTURE_MAG_FILTER, filter.getGlId());
	}
	
	public void applyWrap() {
		GL40.glTexParameteri(txtType.getGlId(), GL40.GL_TEXTURE_WRAP_S, wrap.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), GL40.GL_TEXTURE_WRAP_T, wrap.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), GL40.GL_TEXTURE_WRAP_R, wrap.getGlId());
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
	
	public TextureFilter getFilter() {
		return filter;
	}
	public void setFilter(TextureFilter filter) {
		this.filter = filter;
	}
	
	public TextureType getTextureType() {
		return txtType;
	}
	public void setTextureType(TextureType txtType) {
		this.txtType = txtType;
	}
	
	public TextureWrap getWrap() {
		return wrap;
	}
	public void setWrap(TextureWrap wrap) {
		this.wrap = wrap;
	}
	
	public TexelFormat getTexelFormat() {
		return format;
	}
	public void setTexelFormat(TexelFormat format) {
		this.format = format;
	}
	
	public TexelInternalFormat getInternalFormat() {
		return internalFormat;
	}
	public void setInternalFormat(TexelInternalFormat internalFormat) {
		this.internalFormat = internalFormat;
	}
	
	public static TexelFormat getColorByChannels(int channels) {
		switch (channels) {
		case 1:
			return TexelFormat.RED;
		case 2:
			return TexelFormat.RG;
		case 3:
			return TexelFormat.RGB;
		case 4:
			return TexelFormat.RGBA;
		}
		return null;
	}
	
}
