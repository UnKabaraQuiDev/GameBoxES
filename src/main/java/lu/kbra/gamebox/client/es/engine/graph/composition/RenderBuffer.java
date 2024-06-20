package lu.kbra.gamebox.client.es.engine.graph.composition;

import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TexelFormat;

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
		GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, texelFormat.getGlId(), width, height);
		PDRUtils.checkGlESError("RenderbufferStorage["+texelFormat+"]=("+width+","+height+")");
	}

	public void bind() {
		bind(GLES30.GL_RENDERBUFFER);
	}

	public void unbind() {
		unbind(GLES30.GL_RENDERBUFFER);
	}

	public void bind(int target) {
		GLES30.glBindRenderbuffer(target, rbid);
		PDRUtils.checkGlESError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public void unbind(int target) {
		GLES30.glBindRenderbuffer(target, 0);
		PDRUtils.checkGlESError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public int gen() {
		return (rbid = GLES30.glGenRenderbuffers());
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
		
		GLES30.glDeleteRenderbuffers(rbid);
		PDRUtils.checkGlESError("DeleteRenderbuffers("+rbid+")");
		rbid = -1;
	}
	
	@Override
	public String getId() {
		return name;
	}

}
