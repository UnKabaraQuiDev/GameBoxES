package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;

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

		fbo = GL40.glGenFramebuffers();
	}

	public boolean attachTexture(FrameBufferAttachment attach, int offset, SingleTexture texture) {
		TextureType txtType = texture.getTextureType();
		if (txtType == null) {
			throw new IllegalStateException("TextureType is null");
		}

		if (TextureType.TXT1D.equals(txtType)) {
			GL40.glFramebufferTexture1D(GL40.GL_FRAMEBUFFER, attach.getGlId() + offset, txtType.getGlId(), texture.getTid(), 0);
		} else if (TextureType.TXT2D.equals(txtType) || TextureType.TXT2DMS.equals(txtType)) {
			GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, attach.getGlId() + offset, txtType.getGlId(), texture.getTid(), 0);
		} else if (TextureType.TXT3D.equals(txtType)) {
			// TODO
			assert true : "TODO: Not implemented yet";
			// GL40.glFramebufferTexture3D(GL40.GL_FRAMEBUFFER, attach.getGlId()+offset,
			// texture.getTextureType().getGlId(),
			// texture.getTid(), 0);
		}

		this.attachments.put(attach.getGlId() + offset, texture);
		return PDRUtils.checkGlError("FrameBufferTexture[" + attach + "+" + offset + "][" + name + "]=" + texture.getId());
	}

	public boolean attachRenderBuffer(FrameBufferAttachment attach, int offset, RenderBuffer texture) {
		GL40.glFramebufferRenderbuffer(GL40.GL_FRAMEBUFFER, attach.getGlId() + offset, GL40.GL_RENDERBUFFER, texture.getRBid());
		this.attachments.put(attach.getGlId() + offset, texture);
		return PDRUtils.checkGlError("FrameBufferRenderbuffer[" + attach + "+" + offset + "][" + name + "]=" + texture.getId());
	}

	public boolean hasAttachment(FrameBufferAttachment attach, int offset) {
		return attachments.containsKey(attach.getGlId() + offset);
	}

	public boolean clearAttachments() {
		for (Entry<Integer, FramebufferAttachment> it : attachments.entrySet()) {
			GL40.glFramebufferTexture(GL40.GL_FRAMEBUFFER, it.getKey(), 0, 0);
			PDRUtils.checkGlError("FrameBufferTexture[" + it.getKey() + "][" + name + "]=0");
		}
		attachments.clear();
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
		PDRUtils.checkGlError("BindFrameBuffer[" + target + "][" + name + "]=" + fbo);
	}

	public void unbind() {
		unbind(GL40.GL_FRAMEBUFFER);
	}

	public void unbind(int target) {
		GL40.glBindFramebuffer(target, 0);
		PDRUtils.checkGlError("BindFrameBuffer[" + target + "][" + name + "]=" + fbo);
	}

	public FramebufferAttachment getAttachment(FrameBufferAttachment attach, int offset) {
		return attachments.get(attach.getGlId() + offset);
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + name + "(" + fbo + ")");

		if (fbo == -1)
			return;
		GL40.glDeleteFramebuffers(fbo);
		PDRUtils.checkGlError("DeleteFrameBuffer[" + fbo + "]");
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
