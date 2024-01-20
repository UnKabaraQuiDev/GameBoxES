package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

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
		if(isValid()) {
			throw new RuntimeException("Cannot setup already loaded Single Texture");
		}
		
		if(TextureOperation.GENERATE.equals(textureOperation)) {
			generateTexture();
		}else if(TextureOperation.BUFFER_LOAD.equals(textureOperation)) {
			generateBufferTexture();
		}else if(TextureOperation.FILE_BUFFER_LOAD.equals(textureOperation)) {
			generateFileBufferTexture();
		}
		return isValid();
	}
	
	// FILE BUFFER LOAD
	private void generateFileBufferTexture() {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];
		
		if(!Files.exists(Paths.get(path)))
			throw new RuntimeException("File '"+path+"' not found");
		
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		
		int wi = w[0];
		int he = h[0];
		int channels = c[0];
		
		format = getFormatByChannels(channels);
		internalFormat = getInternalFormatByChannels(channels);
		if(format == null || internalFormat == null)
			throw new RuntimeException("Invalid channel count: "+channels+" to format:"+format+" & internalFormat:"+internalFormat);
		
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		
		setSize(wi, he, 0);
		if(he == 1) {
			setTextureType(TextureType.TXT1D);
		}else {
			setTextureType(TextureType.TXT2D);
		}
		setBuffer(buffer);
		
		generateBufferTexture();
		
		STBImage.stbi_image_free(buffer);
	}
	
	// BUFFER LOAD 
	private void generateBufferTexture() {
		gen();
		bind();
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		if (TextureType.TXT1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), width, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2D.equals(txtType)) {
			System.out.println("gen 2d");
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		}
		applyFilter();
		applyWrap();
		
		if(generateMipmaps) {
			GL40.glGenerateMipmap(txtType.getGlId());
		}
		
		unbind();
	}
	
	// GEN
	private void generateTexture() {
		gen();
		
		System.out.println("format "+format+"; internalFormat "+internalFormat);
		
		bind();
		if (TextureType.TXT1D.equals(txtType)) {
			GL40.glTexImage1D(txtType.getGlId(), 0, internalFormat.getGlId(), width, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT2D.equals(txtType)) {
			GL40.glTexImage2D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		} else if (TextureType.TXT3D.equals(txtType)) {
			GL40.glTexImage3D(txtType.getGlId(), 0, internalFormat.getGlId(), width, height, depth, 0, format.getGlId(), dataType.getGlId(), MemoryUtil.NULL);
		}
		applyFilter();
		applyWrap();
		
		if(generateMipmaps) {
			GL40.glGenerateMipmap(txtType.getGlId());
		}
		
		unbind();
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
