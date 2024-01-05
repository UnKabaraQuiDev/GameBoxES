package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class SingleTexture extends Texture {
	
	/*
	 * GEN 
	 */
	public SingleTexture(String name, int w, int h) {
		this(name, w, h, GL40.GL_LINEAR);
	}
	
	public SingleTexture(String name, int w, int h, int filter) {
		this(name, w, h, filter, GL20.GL_TEXTURE_2D);
	}
	
	public SingleTexture(String name, int w, int h, int filter, int txtResType) {
		this(name, w, h, filter, txtResType, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, int w, int h, int filter, int txtResType, int wrap) {
		this(name, w, h, filter, txtResType, wrap, GL40.GL_RGB);
	}
	
	public SingleTexture(String name, int w, int h, int filter, int txtResType, int wrap, int channels) {
		this(name, w, h, filter, txtResType, wrap, channels, GL40.GL_UNSIGNED_BYTE);
	}
	
	public SingleTexture(String name, int w, int h, int filter, int txtResType, int wrap, int channels, int type) {
		super(name, null, filter, txtResType, wrap, channels);

		this.tid = generateTexture(w, h, channels, type);
	}
	
	private int generateTexture(int w, int h, int channels, int type) {
		int tid = gen();

		GL40.glBindTexture(txtResType, tid);
		if (txtResType == GL40.GL_TEXTURE_1D) {
			GL40.glTexImage1D(txtResType, 0, channels, w, 0, channels, type, MemoryUtil.NULL);
		} else if (txtResType == GL40.GL_TEXTURE_2D) {
			GL40.glTexImage2D(txtResType, 0, channels, w, h, 0, channels, type, MemoryUtil.NULL);
		}
		filter();
		wrap();
		
		GL40.glGenerateMipmap(txtResType);
		
		unbind();

		return tid;
	}
	
	/*
	 * GEN FROM BUFFER
	 */
	public SingleTexture(String name, int w, int h, ByteBuffer buffer) {
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

	private int generateTexture(int w, int h, ByteBuffer buffer, int channels) {
		int tid = gen();
		
		GL40.glBindTexture(txtResType, tid);
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		if (txtResType == GL40.GL_TEXTURE_1D) {
			GL40.glTexImage1D(txtResType, 0, channels, w, 0, channels, GL40.GL_UNSIGNED_BYTE, buffer);
		} else if (txtResType == GL40.GL_TEXTURE_2D) {
			GL40.glTexImage2D(txtResType, 0, channels, w, h, 0, channels, GL40.GL_UNSIGNED_BYTE, buffer);
		}
		filter();
		wrap();
		
		GL40.glGenerateMipmap(txtResType);
		
		unbind();
		
		return tid;
	}
	
	/*
	 * FILE
	 */
	public SingleTexture(String path) {
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
	}

}
