package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Texture implements Cleanupable, UniqueID {
	
	private final String path;
	private final String name;
	private final int tid;
	
	public Texture(String name, int w, int h, ByteBuffer buffer) {
		this.name = name;
		this.path = null;
		this.tid = generateTexture(w, h, buffer);
	}
	public Texture(String name, String path) {
		this.path = path;
		this.name = name;
		
		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];
		
		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		if(buffer == null)
			throw new RuntimeException("Failed to load texture");
		this.tid = generateTexture(w[0], h[0], buffer);
	}
	
	private int generateTexture(int w, int h, ByteBuffer buffer) {
		int tid = GL30.glGenTextures();
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, tid);
		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, w, h, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
		
		return tid;
	}
	
	public void bind(int i) {
		if(i > 31)
			return;
		GL30.glActiveTexture(GL30.GL_TEXTURE0+i);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, tid);
	}
	
	@Override
	public void cleanup() {
		GL30.glDeleteTextures(tid);
	}

	@Override
	public String getId() {return name;}
	public int getTid() {return tid;}
	
}
