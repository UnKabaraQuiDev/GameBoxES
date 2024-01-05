package lu.pcy113.pdr.engine.graph.composition;

import java.util.LinkedList;
import java.util.logging.Level;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.impl.Cleanupable;

public class Compositor implements Cleanupable {
	
	protected Vector4f background = new Vector4f(0.5f, 0.5f, 0.5f, 1);
	
	protected LinkedList<String> layers = new LinkedList<>();
	protected LinkedList<String> passes = new LinkedList<>();
	
	protected Framebuffer framebuffer;
	
	protected SingleTexture depth, color0;
	
	protected Vector2i resolution = new Vector2i(0, 0);
	
	private boolean genTextures() {
		if (depth != null && depth.getTid() != -1)
			depth.cleanup();
		if (color0 != null && color0.getTid() != -1)
			color0.cleanup();
		
		framebuffer.clearAttachments();
		
		depth = new SingleTexture("depth", resolution.x, resolution.y, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D, GL40.GL_CLAMP_TO_EDGE, GL40.GL_DEPTH_COMPONENT, GL40.GL_FLOAT);
		color0 = new SingleTexture("depth", resolution.x, resolution.y, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D, GL40.GL_CLAMP_TO_EDGE, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE);
		
		if (!framebuffer.attachTexture(GL40.GL_DEPTH_ATTACHMENT, depth))
			return false;
		if (!framebuffer.attachTexture(GL40.GL_COLOR_ATTACHMENT0, color0))
			return false;
		
		return true;
	}
	
	public void render(CacheManager cache, GameEngine engine) {
		if(framebuffer == null) {
			framebuffer = cache.loadFramebuffer("Compositor");
		}
		
		framebuffer.bind();
		
		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();
		
		if (resolution.equals(width, height)) {
			// keep same texture
		} else {
			resolution = new Vector2i(width, height);
			if(!genTextures())
				throw new RuntimeException("Could not attach textures to framebuffer");
		}
		
		/*int text = GL40.glGenTextures();
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, width, height, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, MemoryUtil.NULL);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);
		GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, GL40.GL_COLOR_ATTACHMENT0, GL40.GL_TEXTURE_2D, text, 0);
		
		int depth = GL40.glGenTextures();
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, depth);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_DEPTH_COMPONENT32, width, height, 0, GL40.GL_DEPTH_COMPONENT, GL40.GL_FLOAT, MemoryUtil.NULL);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);
		GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, GL40.GL_DEPTH_ATTACHMENT, GL40.GL_TEXTURE_2D, depth, 0);*/
		
		//GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);
		
		color0.bind();
		
		// System.err.println("w:" + width + " h:" + height);
		
		if (!framebuffer.isComplete()) {
			GlobalLogger.log(Level.SEVERE, "Framebuffer not complete: " + framebuffer.getError() + ", w:" + width + " h:" + height);
			return;
		}
		
		GL40.glClearColor(background.x, background.y, background.z, background.w);
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
		
		for (String l : layers) {
			if (l == null)
				continue;
			
			RenderLayer rl = cache.getRenderLayer(l);
			if (rl == null) {
				GlobalLogger.log(Level.WARNING, "Render Layer: " + l + ", not found in Cache");
				break;
			}
			
			if (!rl.isVisible())
				continue;
			
			rl.render(cache, engine);
			
			// combine texture with frame buffer
		}
		
		GL40.glDepthMask(false);
		for (String l : passes) {
			if (l == null)
				continue;
			
			RenderLayer prl = cache.getRenderLayer(l);
			if (prl == null) {
				GlobalLogger.log(Level.WARNING, "Pass Render Layer: " + prl.getId() + ", not found in Cache");
				break;
			}
			
			if (!(prl instanceof PassRenderLayer))
				continue;
			
			color0.bind(0);
			depth.bind(1);
			
			prl.render(cache, engine);
		}
		GL40.glDepthMask(true);
		
		framebuffer.unbind(GL40.GL_DRAW_FRAMEBUFFER);
		framebuffer.bind(GL40.GL_READ_FRAMEBUFFER);
		
		GL40.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL40.GL_COLOR_BUFFER_BIT, GL40.GL_NEAREST);
	}
	
	public void addRenderLayer(int i, RenderLayer id) {
		if (id instanceof PassRenderLayer) {
			passes.add(i, id.getId());
		} else {
			layers.add(i, id.getId());
		}
	}
	
	@Override
	public void cleanup() {
		if (framebuffer != null)
			framebuffer.cleanup();
		if (depth != null)
			depth.cleanup();
		if (color0 != null)
			color0.cleanup();
	}
	
}
