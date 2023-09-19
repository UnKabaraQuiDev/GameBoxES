package lu.pcy113.pdr.engine.graph.composition;

import java.util.LinkedList;
import java.util.logging.Level;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.utils.Logger;

public class Compositor {
	
	protected Vector4f background = new Vector4f(0.5f, 0.5f, 0.5f, 1);
	
	protected LinkedList<String> layers = new LinkedList<>();
	
	public void render(CacheManager cache, GameEngine engine) {
		int fbo = GL40.glGenFramebuffers();
		GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, fbo);
		
		if(GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER) != GL40.GL_FRAMEBUFFER_COMPLETE) {
			Logger.log(Level.SEVERE, "Framebuffer not complete");
			return;
		}
		
		int text = GL40.glGenTextures();
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, engine.getWindow().getWidth(), engine.getWindow().getHeight(), 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, MemoryUtil.NULL);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);  
		GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, GL40.GL_COLOR_ATTACHMENT0, GL40.GL_PROXY_TEXTURE_2D, text, 0);
		
		GL40.glClearColor(background.x, background.y, background.z, background.w);
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
		
		for(String l : layers) {
			RenderLayer rl = cache.getRenderLayer(l);
			if(rl == null) {
				Logger.log(Level.WARNING, "Render Layer: "+rl.getId()+", not found in Cache");
				break;
			}
			
			// TODO: gen texture
			// render to texture
			// with framebuffer as argument if needed
			
			rl.render(cache, engine);
			
			// combine texture with frame buffer
		}
		
		/*GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, 0);
		
		GL40.glDeleteFramebuffers(fbo);*/
	}

	public void addRenderLayer(int i, String id) {
		layers.add(i, id);
	}

}
