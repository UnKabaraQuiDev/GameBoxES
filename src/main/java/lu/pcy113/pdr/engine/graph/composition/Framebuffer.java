package lu.pcy113.pdr.engine.graph.composition;

import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.consts.FrameBufferAttachment;

public class Framebuffer implements UniqueID, Cleanupable {
	
	private final String name;
	private int fbo = -1;
	
	// texture attachment point : texture
	private HashMap<Integer, Texture> attachments = new HashMap<>();

	public Framebuffer(String name) {
		this.name = name;
		
		fbo = GL40.glGenFramebuffers();
	}
	
	public boolean attachTexture(FrameBufferAttachment attach, int offset, SingleTexture texture) {
		GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, attach.getGlId()+offset, texture.getTid(), 0);
		
		this.attachments.put(attach.getGlId()+offset, texture);
		return PDRUtils.checkGlError("FrameBufferTexture["+attach+"+"+offset+"]["+name+"]="+texture.getId());
	}
	
	public boolean clearAttachments() {
		for(Entry<Integer, Texture> it : attachments.entrySet()) {
			GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, it.getKey(), 0, 0);
			
			PDRUtils.checkGlError("FrameBufferTexture["+it.getKey()+"]["+name+"]=0");
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
	public void bind(int target) {
		GL40.glBindFramebuffer(target, fbo);
		PDRUtils.checkGlError("BindFrameBuffer["+target+"]["+name+"]="+fbo);
	}
	
	public void unbind() {
		unbind(GL40.GL_FRAMEBUFFER);
	}
	public void unbind(int target) {
		GL40.glBindFramebuffer(target, 0);
		PDRUtils.checkGlError("BindFrameBuffer["+target+"]["+name+"]="+fbo);
	}
	
	@Override
	public void cleanup() {
		if(fbo != -1) {
			GL40.glDeleteFramebuffers(fbo);
			PDRUtils.checkGlError("DeleteFrameBuffer["+fbo+"]");
		}
	}

	@Override
	public String getId() {
		return name;
	}
	
	public HashMap<Integer, Texture> getAttachments() {
		return attachments;
	}
	
}
