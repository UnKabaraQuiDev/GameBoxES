package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.utils.consts.DataType;
import lu.pcy113.pdr.engine.utils.consts.TexelFormat;
import lu.pcy113.pdr.engine.utils.consts.TexelInternalFormat;
import lu.pcy113.pdr.engine.utils.consts.TextureType;

public class SingleTexture extends Texture {
	
	private int width, height, depth;
	private ByteBuffer buffer;
	
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
	public SingleTexture(String name, int width, ByteBuffer buffer) {
		this(name, width, 0, 0, buffer);
		setTextureType(TextureType.TXT1D);
	}
	public SingleTexture(String name, int width, int height, ByteBuffer buffer) {
		this(name, width, height, 0, buffer);
		setTextureType(TextureType.TXT2D);
	}
	/**
	 * BUFFER LOAD
	 */
	public SingleTexture(String name, int width, int height, int depth, ByteBuffer buffer) {
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
	public boolean setup() {
		if(TextureOperation.GENERATE.equals(textureOperation)) {
			this.tid = generateTexture(width, height, depth, format, internalFormat, dataType, generateMipmaps);
		}else if(TextureOperation.BUFFER_LOAD.equals(textureOperation)) {
			this.tid = generateBufferTexture(width, height, depth, format, internalFormat, dataType, generateMipmaps);
		}else if(TextureOperation.FILE_BUFFER_LOAD.equals(textureOperation)) {
			this.tid = generateFileBufferTexture();
		}
		return tid != -1;
	}
	
	// FILE BUFFER LOAD
	private int generateFileBufferTexture() {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];
		
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		
		int wi = w[0];
		int he = h[0];
		int channels = c[0];
		
		format = getColorByChannels(c[0]);
		if(format == null)
			throw new RuntimeException("Invalid channel count: "+c[0]+" to "+format);
		
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		
		setSize(wi, he, 0);
		if(he == 1) {
			setTextureType(TextureType.TXT1D);
		}else {
			setTextureType(TextureType.TXT2D);
		}
		setBuffer(buffer);
		
		int tid = generateBufferTexture(width, height, depth, format, internalFormat, dataType, generateMipmaps);
		
		STBImage.stbi_image_free(buffer);
		
		return tid;
	}
	
	// BUFFER LOAD 
	private int generateBufferTexture(int w, int h, int d, TexelFormat format, TexelInternalFormat internalFormat, DataType dataType, boolean genMipmaps) {
		int tid = gen();

		GL40.glBindTexture(txtType.getGlId(), tid);
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		if (TextureType.TXT1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), w, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2D.equals(txtType)) {
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), w, h, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), w, h, d, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		}
		applyFilter();
		applyWrap();
		
		if(genMipmaps) {
			GL40.glGenerateMipmap(txtType.getGlId());
		}
		
		unbind();
		
		return tid;
	}
	
	// GEN
	private int generateTexture(int w, int h, int d, TexelFormat format, TexelInternalFormat internalFormat, DataType dataType, boolean genMipmaps) {
		/*int tid = gen();

		GL40.glBindTexture(txtType.getGlId(), tid);
		if (TextureType.TXT1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), w, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2D.equals(txtType)) {
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), w, h, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), w, h, d, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		}
		filter();
		wrap();
		
		if(genMipmaps) {
			GL40.glGenerateMipmap(txtType.getGlId());
		}
		
		unbind();*/
		setBuffer(null);
		return generateBufferTexture(w, h, d, format, internalFormat, dataType, genMipmaps);
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
	
	/*
	 * GEN FROM BUFFER
	 */
	/*public SingleTexture(String name, int w, int h, ByteBuffer buffer) {
		this(name, w, h, buffer, GL40.GL_LINEAR);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter) {
		this(name, w, h, buffer, filter, GL40.GL_TEXTURE_2D);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType) {
		this(name, w, h, buffer, filter, txtResType, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType, int wrap) {
		this(name, w, h, buffer, filter, txtResType, wrap, GL40.GL_RGB);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType, int wrap, int channels) {
		super(name, null, filter, txtResType, wrap, channels);

		this.tid = generateTexture(w, h, buffer, channels);
	}

	*/
	
	/*
	 * FILE
	 */
	/*public SingleTexture(String path) {
		this(path, path, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D, GL40.GL_CLAMP_TO_EDGE);
	}

	public SingleTexture(String name, String path) {
		this(name, path, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, String path, int filter) {
		this(name, path, filter, GL40.GL_TEXTURE_2D, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, String path, int filter, int txtResType) {
		this(name, path, filter, txtResType, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, String path, int filter, int txtResType, int wrap) {
		super(name, path, filter, txtResType, wrap, 0);
		
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];
		
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		super.channelType = getColorByChannels(c[0]);
		if(super.channelType == -1)
			throw new RuntimeException("Invalid channel count: "+c[0]+" to "+super.channelType);
		
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		
		this.tid = generateTexture(w[0], h[0], buffer, super.channelType);
		STBImage.stbi_image_free(buffer);
	}*/
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	public void destroyBuffer() {
		buffer = null;
	}

}
