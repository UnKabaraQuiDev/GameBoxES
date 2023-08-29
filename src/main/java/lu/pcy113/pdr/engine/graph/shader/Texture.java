package lu.pcy113.pdr.engine.graph.shader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import lu.pcy113.pdr.engine.impl.Cleanupable;

public class Texture implements Cleanupable {
	
	private int id;
	private String path;
	
	public Texture(int w, int h, ByteBuffer bb) {
		this.path = "";
		generateTexture(w, h, bb);
	}
	
	public Texture(String path) {
		this.path = path;
		try(MemoryStack stack = MemoryStack.stackPush()) {
			this.path = path;
			
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			ByteBuffer bb = STBImage.stbi_load("resources/textures/"+path, w, h, channels, 4);
			if(bb == null)
				throw new RuntimeException("Image file "+path+" not loaded: "+STBImage.stbi_failure_reason());
			
			int width = w.get();
			int height = h.get();
			
			generateTexture(width, height, bb);
			
			STBImage.stbi_image_free(bb);
		}
	}
	
	public void bind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
	}
	
	@Override
	public void cleanup() {
		GL30.glDeleteTextures(id);
	}
	
	private void generateTexture(int w, int h, ByteBuffer bb) {
		id = GL30.glGenTextures();
		
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
		GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, w, h, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, bb);
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
	}
	
	public int getId() {return id;}
	public String getPath() {return path;}
	
}
