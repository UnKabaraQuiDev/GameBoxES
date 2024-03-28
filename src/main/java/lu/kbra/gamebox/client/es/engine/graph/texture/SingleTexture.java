package lu.kbra.gamebox.client.es.engine.graph.texture;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelInternalFormat;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImage;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImageOrigin;

public class SingleTexture extends Texture {

	private int width, height, depth;
	private MemImage buffer;

	// GEN
	public SingleTexture(String name, int width) {
		this(name, width, 0, 0);
		setTextureType(TextureType.TXT1D);
	}

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
	public SingleTexture(String name, int width, MemImage buffer) {
		this(name, width, 0, 0, buffer);
		setTextureType(TextureType.TXT1D);
	}

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
		if (((TexelInternalFormat.isDepth(super.internalFormat) || TexelInternalFormat.isStencil(super.internalFormat)) && super.sampleCount > MAX_DEPTH_TEXTURE_SAMPLES)
				|| (TexelInternalFormat.isColor(super.internalFormat) && super.sampleCount > MAX_COLOR_TEXTURE_SAMPLES) || (TexelInternalFormat.isInteger(super.internalFormat)) && super.sampleCount > MAX_INTEGER_SAMPLES) {
			PDRUtils.throwGLError(super.internalFormat + " does not support " + super.sampleCount + " samples, max are Depth:" + MAX_DEPTH_TEXTURE_SAMPLES + ", Color:" + MAX_COLOR_TEXTURE_SAMPLES + ", Integer:" + MAX_INTEGER_SAMPLES);
		}

		// Invalid value (glTexImage2DMultisample)
		if ((width > MAX_TEXTURE_SIZE || width < 0) || (height > MAX_TEXTURE_SIZE || height < 0) || (depth > MAX_TEXTURE_SIZE || depth < 0)) {
			PDRUtils.throwGLError("Invalid texture size: " + width + "x" + height + "x" + depth + ", max is " + MAX_TEXTURE_SIZE);
		}
		if (TextureType.isMultisampled(super.txtType) && super.sampleCount < 1) {
			PDRUtils.throwGLError("Invalid sample count: " + super.sampleCount + " for " + super.txtType + ", min is 1");
		}

//		System.out.println("info: "+super.internalFormat+" does not support "+super.sampleCount+" samples, max are Depth:"+MAX_DEPTH_TEXTURE_SAMPLES+", Color:"+MAX_COLOR_TEXTURE_SAMPLES+", Integer:"+MAX_INTEGER_SAMPLES);
//		System.out.println("info: "+"Invalid texture size: "+width+"x"+height+"x"+depth+", max is "+MAX_TEXTURE_SIZE);
//		System.out.println("info: "+"Invalid sample count: "+super.sampleCount+" for "+super.txtType);
//		System.out.println("info: isms: "+TextureType.isMultisampled(super.txtType));

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
		if (he == 1) {
			setTextureType(TextureType.TXT1D);
		} else {
			setTextureType(TextureType.TXT2D);
		}
		setMemImage(image);

		generateBufferTexture();

		image.free();
	}

	// BUFFER LOAD
	private void generateBufferTexture() {
		gen();
		bind();

		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		PDRUtils.checkGlError("PixelStoreI.UnpackAlignment=1");
		if (TextureType.TXT1D.equals(txtType) || TextureType.ARRAY1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), width, 0, format.getGlId(), dataType.getGlId(), buffer.getBuffer());
		} else if (TextureType.TXT2D.equals(txtType) || TextureType.ARRAY2D.equals(txtType)) {
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), buffer.getBuffer());
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), buffer.getBuffer());
		}
		PDRUtils.checkGlError("TexImage_" + txtType + "_" + (TextureType.isMultisampled(txtType) ? "MS(" + sampleCount + ")" : ""));
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

		if (TextureType.TXT1D.equals(txtType) || TextureType.ARRAY1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), width, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2D.equals(txtType) || TextureType.ARRAY2D.equals(txtType)) {
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2DMS.equals(txtType) || TextureType.ARRAY2DMS.equals(txtType)) {
			// System.out.println("inputs: " + txtType + " " + super.sampleCount + " " +
			// internalFormat + " " + width + " " + height + " " +
			// super.fixedSampleLocation);
			GL40.glTexImage2DMultisample(txtType.getGlId(), super.sampleCount, internalFormat.getGlId(), width, height, super.fixedSampleLocation);
		}
		PDRUtils.checkGlError("TexImage_" + txtType + "_" + (TextureType.isMultisampled(txtType) ? "MS(" + sampleCount + ")" : ""));
		applyFilter();
		applyWrap();

		if (generateMipmaps && !isMultisampled()) {
			GL40.glGenerateMipmap(txtType.getGlId());
			PDRUtils.checkGlError("GenerateMipmap[" + txtType + "]");
		}

		unbind();
	}

	public MemImage getStoredImage() {
		/*
		 * int width = GL40.glGetTexLevelParameteri(GL40.GL_TEXTURE_2D, 0, GL40.
		 * GL_TEXTURE_WIDTH); int height =
		 * GL40.glGetTexLevelParameteri(GL40.GL_TEXTURE_2D, 0, GL40. GL_TEXTURE_HEIGHT);
		 * int internalFormat = GL40.glGetTexLevelParameteri(GL40.GL_TEXTURE_2D, 0,
		 * GL40.GL_TEXTURE_INTERNAL_FORMAT); int channels =
		 * Texture.getChannelsByInternalFormat(internalFormat); int internalType =
		 * GL40.glGetTexLevelParameteriv(GL40.GL_TEXTURE_2D, 0,
		 * GL40.GL_TEXTURE_COMPONENTS);
		 */

		bind();

		int channelCount = getChannelsByFormat(format.getGlId());
		ByteBuffer buffer = MemoryUtil.memAlloc(width * height * channelCount); // BufferUtils.createByteBuffer(width *
																				// height * channelCount);
		// GL40.glBindBuffer(GL40.GL_PIXEL_PACK_BUFFER, 0);
		// GL40.glBindFramebuffer(GL40.GL_READ_FRAMEBUFFER_BINDING, 0);
		GL40.glReadPixels(0, 0, width, height, format.getGlId(), dataType.getGlId(), buffer);
		PDRUtils.checkGlError("glReadPixels(0, 0, " + width + ", " + height + ", " + internalFormat + ", " + dataType + ")");

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

}
