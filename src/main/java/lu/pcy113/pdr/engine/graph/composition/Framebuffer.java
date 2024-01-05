package lu.pcy113.pdr.engine.graph.composition;

import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.Pair;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Framebuffer implements UniqueID, Cleanupable {
	
	private final String name;
	private int fbo = -1;
	
	// texture attachment point : texture
	private HashMap<Integer, Texture> attachments = new HashMap<>();

	public Framebuffer(String name) {
		this.name = name;
		
		fbo = GL40.glGenFramebuffers();
	}
	
	public boolean attachTexture(int attachmenPoint, SingleTexture texture) {
		GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, attachmenPoint, texture.getTid(), 0);
		
		this.attachments.put(attachmenPoint, texture);
		return GL40.glGetError() == GL40.GL_NO_ERROR;
	}
	
	public boolean clearAttachments() {
		for(Entry<Integer, Texture> it : attachments.entrySet()) {
			GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, it.getKey(), 0, 0);
			
			if(GL40.glGetError() != GL40.GL_NO_ERROR)
				return false;
		}
		return true;
	}
	
	public boolean isComplete() {
		return GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER) == GL40.GL_FRAMEBUFFER_COMPLETE;
	}
	public int getError() {
		return GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER);
	}
	
	public void bind() {
		bind(GL40.GL_FRAMEBUFFER);
	}
	public void bind(int i) {
		GL40.glBindFramebuffer(i, fbo);
	}
	
	public void unbind() {
		unbind(GL40.GL_FRAMEBUFFER);
	}
	public void unbind(int i) {
		GL40.glBindFramebuffer(i, 0);
	}
	
	@Override
	public void cleanup() {
		if(fbo != -1)
			GL40.glDeleteFramebuffers(fbo);
	}

	@Override
	public String getId() {
		return name;
	}
	
}
