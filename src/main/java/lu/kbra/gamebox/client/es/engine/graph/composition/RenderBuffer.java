package lu.kbra.gamebox.client.es.engine.graph.composition;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;

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
		GL40.glRenderbufferStorage(GL40.GL_RENDERBUFFER, texelFormat.getGlId(), width, height);
		PDRUtils.checkGlError("RenderbufferStorage["+texelFormat+"]=("+width+","+height+")");
	}

	public void bind() {
		bind(GL40.GL_RENDERBUFFER);
	}

	public void unbind() {
		unbind(GL40.GL_RENDERBUFFER);
	}

	public void bind(int target) {
		GL40.glBindRenderbuffer(target, rbid);
		PDRUtils.checkGlError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public void unbind(int target) {
		GL40.glBindRenderbuffer(target, 0);
		PDRUtils.checkGlError("BindRenderbuffer["+target+"] = "+rbid);
	}

	public int gen() {
		return (rbid = GL40.glGenRenderbuffers());
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
		
		GL40.glDeleteRenderbuffers(rbid);
		PDRUtils.checkGlError("DeleteRenderbuffers("+rbid+")");
		rbid = -1;
	}
	
	@Override
	public String getId() {
		return name;
	}

}
