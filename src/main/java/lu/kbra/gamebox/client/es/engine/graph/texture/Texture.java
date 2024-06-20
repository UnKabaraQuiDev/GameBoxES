package lu.kbra.gamebox.client.es.engine.graph.texture;

import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.graph.composition.FramebufferAttachment;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.DataType;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelFormat;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelInternalFormat;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureParameter;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureWrap;

public abstract class Texture implements Cleanupable, UniqueID, FramebufferAttachment {

	/*
	 * public static final int MAX_DEPTH_TEXTURE_SAMPLES = GLES30.glGetInteger(GLES30.GL_MAX_DEPTH_TEXTURE_SAMPLES); public static final int MAX_COLOR_TEXTURE_SAMPLES = GLES30.glGetInteger(GLES30.GL_MAX_COLOR_TEXTURE_SAMPLES); public static
	 * final int MAX_INTEGER_SAMPLES = GLES30.glGetInteger(GLES30.GL_MAX_INTEGER_SAMPLES);
	 */
	public static final int MAX_TEXTURE_LOD_BIAS = GLES30.glGetInteger(GLES30.GL_MAX_TEXTURE_LOD_BIAS);
	public static final int MAX_TEXTURE_SIZE = GLES30.glGetInteger(GLES30.GL_MAX_TEXTURE_SIZE);

	protected final String path;
	protected final String name;
	protected int tid = -1;
	protected TextureFilter minFilter = TextureFilter.LINEAR, magFilter = TextureFilter.LINEAR;
	protected TextureType txtType = TextureType.TXT2D;
	protected TextureWrap hWrap = TextureWrap.CLAMP_TO_EDGE, vWrap = TextureWrap.CLAMP_TO_EDGE, dWrap = TextureWrap.CLAMP_TO_EDGE;
	protected DataType dataType = DataType.UBYTE;
	protected TexelFormat format = TexelFormat.RGB;
	protected TexelInternalFormat internalFormat = TexelInternalFormat.RGB;
	protected boolean generateMipmaps = true, fixedSampleLocation = false;
	protected int sampleCount = 1;
	protected TextureOperation textureOperation = null;

	public Texture(String _name, String _path, TextureOperation txtOp) {
		this.name = _name;
		this.path = _path;
		this.textureOperation = txtOp;
	}

	public abstract boolean setup();

	public abstract boolean checkConfigErrors();

	protected int gen() {
		this.tid = GLES30.glGenTextures();
		PDRUtils.checkGlESError("GenTextures");
		return tid;
	}

	public void active(int i) {
		if (i > 31)
			return;
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0 + i);
		PDRUtils.checkGlESError("ActiveTexture[" + (GLES30.GL_TEXTURE0 + i) + "]");
	}

	public void bind(int i) {
		active(i);
		bind();
	}

	public void bind() {
		if (tid == -1)
			return;
		GLES30.glBindTexture(txtType.getGlId(), tid);
		PDRUtils.checkGlESError("BindTexture[" + txtType + "]=" + tid);
	}

	public void unbind(int i) {
		active(i);
		unbind();
	}

	public void unbind() {
		GLES30.glBindTexture(txtType.getGlId(), 0);
		PDRUtils.checkGlESError("BindTexture[" + txtType + "]=0");
	}

	public void genMipMaps() {
		GLES30.glGenerateMipmap(txtType.getGlId());
		PDRUtils.checkGlESError("GenerateMipmap[" + txtType + "]");
	}

	public void applyFilter() {
		GLES30.glTexParameteri(txtType.getGlId(), TextureParameter.MIN_FILTER.getGlId(), minFilter.getGlId());
		PDRUtils.checkGlESError("TexParameter[" + txtType + "].MinFilter=" + minFilter);
		GLES30.glTexParameteri(txtType.getGlId(), TextureParameter.MAG_FILTER.getGlId(), magFilter.getGlId());
		PDRUtils.checkGlESError("TexParameter[" + txtType + "].MagFilter=" + magFilter);
	}

	public void applyWrap() {
		GLES30.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_HORIZONTAL.getGlId(), hWrap.getGlId());
		PDRUtils.checkGlESError("TexParameter[" + txtType + "].WrapHorizontal=" + hWrap);
		GLES30.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_VERTICAL.getGlId(), vWrap.getGlId());
		PDRUtils.checkGlESError("TexParameter[" + txtType + "].WrapVertical=" + vWrap);
		GLES30.glTexParameteri(txtType.getGlId(), TextureParameter.WRAP_DEPTH.getGlId(), dWrap.getGlId());
		PDRUtils.checkGlESError("TexParameter[" + txtType + "].WrapDepth=" + dWrap);
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + name + " (" + tid + ")");

		if (tid == -1)
			return;

		GLES30.glDeleteTextures(tid);
		PDRUtils.checkGlESError("DeleteTextures[" + tid + "] (" + name + ")");
		tid = -1;
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

	public void setFilters(TextureFilter min, TextureFilter mag) {
		this.minFilter = min;
		this.magFilter = mag;
	}

	public void setFilters(TextureFilter filter) {
		this.minFilter = filter;
		this.magFilter = filter;
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

	public void setWraps(TextureWrap hWrap, TextureWrap vWrap, TextureWrap dWrap) {
		this.hWrap = hWrap;
		this.vWrap = vWrap;
		this.dWrap = dWrap;
	}

	public void setWraps(TextureWrap wrap) {
		this.hWrap = wrap;
		this.vWrap = wrap;
		this.dWrap = wrap;
	}

	public TexelFormat getFormat() {
		return format;
	}

	public void setFormat(TexelFormat format) {
		this.format = format;
	}

	public TexelInternalFormat getInternalFormat() {
		return internalFormat;
	}

	public void setInternalFormat(TexelInternalFormat internalFormat) {
		this.internalFormat = internalFormat;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public boolean isValid() {
		return tid != -1;
	}

	public boolean isGenerateMipmaps() {
		return generateMipmaps;
	}

	public void setGenerateMipmaps(boolean generateMipmaps) {
		this.generateMipmaps = generateMipmaps;
	}

	public boolean isArray() {
		return TextureType.isArray(txtType);
	}

	public int getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(int sampleCount) {
		this.sampleCount = sampleCount;
	}

	@Override
	public String toString() {
		return "{tid: " + tid + ", name: " + name + ", valid: " + isValid() + ", type: " + txtType + ", format: " + format + ", internalFormat: " + internalFormat + ", dataType: " + dataType + "}";
	}

	public static TexelFormat getFormatByChannels(int channels) {
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

	public static TexelInternalFormat getInternalFormatByChannels(int channels) {
		switch (channels) {
		case 1:
			return TexelInternalFormat.RED;
		case 2:
			return TexelInternalFormat.RG;
		case 3:
			return TexelInternalFormat.RGB;
		case 4:
			return TexelInternalFormat.RGBA;
		}
		return null;
	}

	public static int getChannelsByInternalFormat(int format) {
		if (format == TexelInternalFormat.RED.getGlId()) {
			return 1;
		} else if (format == TexelInternalFormat.RG.getGlId()) {
			return 2;
		} else if (format == TexelInternalFormat.RGB.getGlId()) {
			return 3;
		} else if (format == TexelInternalFormat.RGBA.getGlId()) {
			return 4;
		}
		return -1;
	}

	public static int getChannelsByFormat(int format) {
		if (format == TexelFormat.RED.getGlId()) {
			return 1;
		} else if (format == TexelFormat.RG.getGlId()) {
			return 2;
		} else if (format == TexelFormat.RGB.getGlId()) {
			return 3;
		} else if (format == TexelFormat.RGBA.getGlId()) {
			return 4;
		}
		return -1;
	}

}
