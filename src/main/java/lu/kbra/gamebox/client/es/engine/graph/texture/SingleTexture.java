package lu.kbra.gamebox.client.es.engine.graph.texture;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.opengles.GLES30;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImage;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImageOrigin;

public class SingleTexture extends Texture {

	private int width, height, depth;
	private MemImage buffer;

	// GEN
	public SingleTexture(String name, int width, int height) {
		this(name, width, height, 0);
		setTextureType(TextureType.TXT2D);
	}

	/**
	 * GEN
	 */
	public SingleTexture(String name, int width, int height, int depth) {
		super(name, name, TextureOperation.GENERATE);
		setSize(width, height, depth);
		setTextureType(TextureType.TXT3D);
	}

	// BUFFER LOAD
	public SingleTexture(String name, int width, int height, MemImage buffer) {
		this(name, width, height, 0, buffer);
		setTextureType(TextureType.TXT2D);
	}

	/**
	 * BUFFER LOAD
	 */
	public SingleTexture(String name, int width, int height, int depth, MemImage buffer) {
		super(name, name, TextureOperation.BUFFER_LOAD);
		this.buffer = buffer;
		setSize(width, height, depth);
		setTextureType(TextureType.TXT3D);
	}

	/**
	 * FILE BUFFER LOAD
	 */
	public SingleTexture(String name, String path) {
		super(name, path, TextureOperation.FILE_BUFFER_LOAD);
	}

	@Override
	public boolean checkConfigErrors() {
		// Invalid operation (glTexImage2DMultisample)
		/*
		 * if (((TexelInternalFormat.isDepth(super.internalFormat) || TexelInternalFormat.isStencil(super.internalFormat)) && super.sampleCount > MAX_DEPTH_TEXTURE_SAMPLES) || (TexelInternalFormat.isColor(super.internalFormat) &&
		 * super.sampleCount > MAX_COLOR_TEXTURE_SAMPLES) || (TexelInternalFormat.isInteger(super.internalFormat)) && super.sampleCount > MAX_INTEGER_SAMPLES) { PDRUtils.throwGLESError(super.internalFormat + " does not support " +
		 * super.sampleCount + " samples, max are Depth:" + MAX_DEPTH_TEXTURE_SAMPLES + ", Color:" + MAX_COLOR_TEXTURE_SAMPLES + ", Integer:" + MAX_INTEGER_SAMPLES); }
		 */

		// Invalid value (glTexImage2DMultisample)
		if ((width > MAX_TEXTURE_SIZE || width < 0) || (height > MAX_TEXTURE_SIZE || height < 0) || (depth > MAX_TEXTURE_SIZE || depth < 0)) {
			PDRUtils.throwGLESError("Invalid texture size: " + width + "x" + height + "x" + depth + ", max is " + MAX_TEXTURE_SIZE);
		}

		return true;
	}

	@Override
	public boolean setup() {
		if (isValid()) {
			throw new RuntimeException("Cannot setup already loaded Single Texture");
		}

		checkConfigErrors();

		if (TextureOperation.GENERATE.equals(textureOperation)) {
			generateTexture();
		} else if (TextureOperation.BUFFER_LOAD.equals(textureOperation)) {
			generateBufferTexture();
		} else if (TextureOperation.FILE_BUFFER_LOAD.equals(textureOperation)) {
			generateFileBufferTexture();
		}
		return isValid();
	}

	// FILE BUFFER LOAD
	private void generateFileBufferTexture() {
		if (!Files.exists(Paths.get(path)))
			throw new RuntimeException("File '" + path + "' not found");

		MemImage image = FileUtils.STBILoad(path);

		int wi = image.getWidth();
		int he = image.getHeight();
		int channels = image.getChannels();

		format = getFormatByChannels(channels);
		internalFormat = getInternalFormatByChannels(channels);
		if (format == null || internalFormat == null)
			throw new RuntimeException("Invalid channel count: " + channels + " to format:" + format + " & internalFormat:" + internalFormat);

		if (image.getBuffer() == null)
			throw new RuntimeException("Failed to load texture buffer.");

		setSize(wi, he, 0);
		setTextureType(TextureType.TXT2D);
		setMemImage(image);

		generateBufferTexture();

		image.cleanup();
	}

	// BUFFER LOAD
	private void generateBufferTexture() {
		gen();
		bind();

		GLES30.glPixelStorei(GLES30.GL_UNPACK_ALIGNMENT, 1);
		PDRUtils.checkGlESError("PixelStoreI.UnpackAlignment=1");
		if (TextureType.TXT2D.equals(txtType) || TextureType.ARRAY2D.equals(txtType)) {
			GLES30.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), buffer.getBuffer());
		} else if (TextureType.TXT3D.equals(txtType)) {
			GLES30.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), buffer.getBuffer());
		}
		PDRUtils.checkGlESError("TexImage_" + txtType);
		applyFilter();
		applyWrap();

		if (generateMipmaps) {
			genMipMaps();
		}

		unbind();
	}

	// GEN
	private void generateTexture() {
		gen();
		bind();

		if (TextureType.TXT2D.equals(txtType) || TextureType.ARRAY2D.equals(txtType)) {
			GLES30.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GLES30.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		}
		PDRUtils.checkGlESError("TexImage_" + txtType);
		applyFilter();
		applyWrap();

		if (generateMipmaps) {
			GLES30.glGenerateMipmap(txtType.getGlId());
			PDRUtils.checkGlESError("GenerateMipmap[" + txtType + "]");
		}

		unbind();
	}

	public MemImage getStoredImage() {
		/*
		 * int width = GLES30.glGetTexLevelParameteri(GLES30.GL_TEXTURE_2D, 0, GLES30. GL_TEXTURE_WIDTH); int height = GLES30.glGetTexLevelParameteri(GLES30.GL_TEXTURE_2D, 0, GLES30. GL_TEXTURE_HEIGHT); int internalFormat =
		 * GLES30.glGetTexLevelParameteri(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_TEXTURE_INTERNAL_FORMAT); int channels = Texture.getChannelsByInternalFormat(internalFormat); int internalType =
		 * GLES30.glGetTexLevelParameteriv(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_TEXTURE_COMPONENTS);
		 */

		bind();

		int channelCount = getChannelsByFormat(format.getGlId());
		ByteBuffer buffer = MemoryUtil.memAlloc(width * height * channelCount); // BufferUtils.createByteBuffer(width *
																				// height * channelCount);
		// GLES30.glBindBuffer(GLES30.GL_PIXEL_PACK_BUFFER, 0);
		// GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER_BINDING, 0);
		GLES30.glReadPixels(0, 0, width, height, format.getGlId(), dataType.getGlId(), buffer);
		PDRUtils.checkGlESError("glReadPixels(0, 0, " + width + ", " + height + ", " + internalFormat + ", " + dataType + ")");

		unbind();

		return new MemImage(width, height, channelCount, buffer, MemImageOrigin.OPENGL);
	}

	public void setSize(int width) {
		setSize(width, 0, 0);
	}

	public void setSize(int width, int height) {
		setSize(width, height, 0);
	}

	public void setSize(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public MemImage getMemImage() {
		return buffer;
	}

	public void setMemImage(MemImage buffer) {
		this.buffer = buffer;
	}

	public void destroyBuffer() {
		buffer = null;
	}

	public Vector2i getSize2D() {
		return new Vector2i(width, height);
	}

	public Vector2f getNormalizedSize2D() {
		return new Vector2f(getSize2D()).div(Math.max(width, height));
	}

}
