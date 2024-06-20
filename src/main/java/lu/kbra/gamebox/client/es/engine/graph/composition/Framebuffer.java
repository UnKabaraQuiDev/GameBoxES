package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.FrameBufferAttachment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;

public class Framebuffer implements UniqueID, Cleanupable {

	private final String name;
	private int fbo = -1;

	// texture attachment point : texture
	private HashMap<Integer, FramebufferAttachment> attachments = new HashMap<>();

	public Framebuffer(String name) {
		this.name = name;

		fbo = GLES30.glGenFramebuffers();
	}

	public boolean attachTexture(FrameBufferAttachment attach, int offset, SingleTexture texture) {
		TextureType txtType = texture.getTextureType();
		if (txtType == null) {
			throw new IllegalStateException("TextureType is null");
		}

		if (TextureType.TXT2D.equals(txtType)) {
			GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, attach.getGlId() + offset, txtType.getGlId(), texture.getTid(), 0);
		} else if (TextureType.TXT3D.equals(txtType)) {
			PDRUtils.throwGLESError("Cannot attach TXT3D to Framebuffer");
		}

		this.attachments.put(attach.getGlId() + offset, texture);
		return PDRUtils.checkGlESError("FrameBufferTexture[" + attach + "+" + offset + "][" + name + "]=" + texture.getId());
	}

	public boolean attachRenderBuffer(FrameBufferAttachment attach, int offset, RenderBuffer texture) {
		GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, attach.getGlId() + offset, GLES30.GL_RENDERBUFFER, texture.getRBid());
		this.attachments.put(attach.getGlId() + offset, texture);
		return PDRUtils.checkGlESError("FrameBufferRenderbuffer[" + attach + "+" + offset + "][" + name + "]=" + texture.getId());
	}

	public boolean hasAttachment(FrameBufferAttachment attach, int offset) {
		return attachments.containsKey(attach.getGlId() + offset);
	}

	public boolean clearAttachments() {
		for (Entry<Integer, FramebufferAttachment> it : attachments.entrySet()) {
			PDRUtils.throwGLESError("Cannot clear attachments from Framebuffer");
			PDRUtils.checkGlESError("FrameBufferTexture[" + it.getKey() + "][" + name + "]=0");
		}
		attachments.clear();
		return true;
	}

	public boolean isComplete() {
		return GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) == GLES30.GL_FRAMEBUFFER_COMPLETE;
	}

	public int getError() {
		return GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER);
	}

	public void bind() {
		bind(GLES30.GL_FRAMEBUFFER);
	}

	public void bind(int target) {
		GLES30.glBindFramebuffer(target, fbo);
		PDRUtils.checkGlESError("BindFrameBuffer[" + target + "][" + name + "]=" + fbo);
	}

	public void unbind() {
		unbind(GLES30.GL_FRAMEBUFFER);
	}

	public void unbind(int target) {
		GLES30.glBindFramebuffer(target, 0);
		PDRUtils.checkGlESError("BindFrameBuffer[" + target + "][" + name + "]=" + fbo);
	}

	public FramebufferAttachment getAttachment(FrameBufferAttachment attach, int offset) {
		return attachments.get(attach.getGlId() + offset);
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + name + "(" + fbo + ")");

		if (fbo == -1)
			return;
		GLES30.glDeleteFramebuffers(fbo);
		PDRUtils.checkGlESError("DeleteFrameBuffer[" + fbo + "]");
		fbo = -1;
	}

	@Override
	public String getId() {
		return name;
	}

	public int getFbo() {
		return fbo;
	}

	public HashMap<Integer, FramebufferAttachment> getAttachments() {
		return attachments;
	}

}
