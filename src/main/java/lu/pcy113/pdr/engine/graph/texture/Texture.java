package lu.pcy113.pdr.engine.graph.texture;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.consts.DataType;
import lu.pcy113.pdr.engine.utils.consts.TexelFormat;
import lu.pcy113.pdr.engine.utils.consts.TexelInternalFormat;
import lu.pcy113.pdr.engine.utils.consts.TextureFilter;
import lu.pcy113.pdr.engine.utils.consts.TextureParameter;
import lu.pcy113.pdr.engine.utils.consts.TextureType;
import lu.pcy113.pdr.engine.utils.consts.TextureWrap;

public abstract class Texture implements Cleanupable, UniqueID {
	
	protected final String path;
	protected final String name;
	protected int tid = -1;
	protected TextureFilter minFilter = TextureFilter.LINEAR, magFilter = TextureFilter.LINEAR;
	protected TextureType txtType = TextureType.TXT2D;
	protected TextureWrap hWrap = TextureWrap.CLAMP_TO_EDGE, vWrap = TextureWrap.CLAMP_TO_EDGE, dWrap = TextureWrap.CLAMP_TO_EDGE;
	protected DataType dataType;
	protected TexelFormat format = TexelFormat.RGB;
	protected TexelInternalFormat internalFormat = TexelInternalFormat.RGB;
	protected boolean generateMipmaps = false;
	protected TextureOperation textureOperation = null;
	
	public Texture(String _name, String _path, TextureOperation txtOp) {
		this.path = _name;
		this.name = _path;
		this.textureOperation = txtOp;
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
		GL40.glTexParameteri(txtType.getGlId(), TextureParameter.MIN_FILTER.getGlId(), minFilter.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), TextureParameter.MAG_FILTER.getGlId(), magFilter.getGlId());
	}
	
	public void applyWrap() {
		GL40.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_HORIZONTAL.getGlId(), hWrap.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_VERTICAL.getGlId(), vWrap.getGlId());
		GL40.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_DEPTH.getGlId(), dWrap.getGlId());
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
	
	public TextureFilter getMinFilter() {
		return minFilter;
	}
	
	public void setMinFilter(TextureFilter minFilter) {
		this.minFilter = minFilter;
	}
	
	public TextureFilter getMagFilter() {
		return magFilter;
	}
	
	public void setMagFilter(TextureFilter magFilter) {
		this.magFilter = magFilter;
	}
	
	public TextureType getTextureType() {
		return txtType;
	}
	
	public void setTextureType(TextureType txtType) {
		this.txtType = txtType;
	}
	
	public TextureWrap gethWrap() {
		return hWrap;
	}
	
	public void sethWrap(TextureWrap hWrap) {
		this.hWrap = hWrap;
	}
	
	public TextureWrap getvWrap() {
		return vWrap;
	}
	
	public void setvWrap(TextureWrap vWrap) {
		this.vWrap = vWrap;
	}
	
	public TextureWrap getdWrap() {
		return dWrap;
	}
	
	public void setdWrap(TextureWrap dWrap) {
		this.dWrap = dWrap;
	}
	
	public void setWrap(TextureWrap hWrap, TextureWrap vWrap, TextureWrap dWrap) {
		this.hWrap = hWrap;
		this.vWrap = vWrap;
		this.dWrap = dWrap;
	}
	public void setWrap(TextureWrap wrap) {
		this.hWrap = wrap;
		this.vWrap = wrap;
		this.dWrap = wrap;
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
