package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

public class SingleTexture extends Texture {
	
	/*
	 * GEN
	 */
	public SingleTexture(String name, int w, int h, ByteBuffer buffer) {
		this(name, w, h, buffer, GL40.GL_LINEAR, GL20.GL_TEXTURE_2D);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType) {
		this(name, null, filter, txtResType, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public SingleTexture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType, int wrap) {
		super(name, null, filter, txtResType, wrap);

		this.tid = generateTexture(w, h, buffer);
	}

	private int generateTexture(int w, int h, ByteBuffer buffer) {
		int tid = GL40.glGenTextures();

		GL40.glBindTexture(txtResType, tid);
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		filter();
		wrap();
		if (txtResType == GL40.GL_TEXTURE_1D) {
			GL40.glTexImage1D(txtResType, 0, GL40.GL_RGBA, w, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
		} else if (txtResType == GL40.GL_TEXTURE_2D) {
			GL40.glTexImage2D(txtResType, 0, GL40.GL_RGBA, w, h, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
		}
		GL40.glGenerateMipmap(txtResType);

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
		super(name, path, filter, txtResType, wrap);
		
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];

		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		this.tid = generateTexture(w[0], h[0], buffer);
		STBImage.stbi_image_free(buffer);
	}

}
