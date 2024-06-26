package lu.kbra.gamebox.client.es.engine.graph.composition;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelFormat;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.pcy113.pclib.logger.GlobalLogger;

public class RenderBuffer implements UniqueID, Cleanupable, FramebufferAttachment {

	private String name;
	private int rbid;

	private TexelFormat texelFormat;
	private int width, height;

	public RenderBuffer(String name) {
		this.name = name;
	}

	public void setup() {
		gen();
		bind();
		GL_W.glRenderbufferStorage(GL_W.GL_RENDERBUFFER, texelFormat.getGlId(), width, height);
		PDRUtils.checkGL_WError("RenderbufferStorage["+texelFormat+"]=("+width+","+height+")");
	}

	public void bind() {
		bind(GL_W.GL_RENDERBUFFER);
	}

	public void unbind() {
		unbind(GL_W.GL_RENDERBUFFER);
	}

	public void bind(int target) {
		GL_W.glBindRenderbuffer(target, rbid);
		PDRUtils.checkGL_WError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public void unbind(int target) {
		GL_W.glBindRenderbuffer(target, 0);
		PDRUtils.checkGL_WError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public int gen() {
		return (rbid = GL_W.glGenRenderbuffers());
	}

	public TexelFormat getTexelFormat() {
		return texelFormat;
	}

	public void setTexelFormat(TexelFormat texelFormat) {
		this.texelFormat = texelFormat;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getRBid() {
		return rbid;
	}
	
	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: "+name+"("+rbid+")");
		
		if(rbid == -1)
			return;
		
		GL_W.glDeleteRenderbuffers(rbid);
		PDRUtils.checkGL_WError("DeleteRenderbuffers("+rbid+")");
		rbid = -1;
	}
	
	@Override
	public String getId() {
		return name;
	}

}
