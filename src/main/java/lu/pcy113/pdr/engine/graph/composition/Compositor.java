package lu.pcy113.pdr.engine.graph.composition;

import java.util.LinkedList;
import java.util.logging.Level;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;

public class Compositor {

	protected Vector4f background = new Vector4f(0.5f, 0.5f, 0.5f, 1);

	protected LinkedList<String> layers = new LinkedList<>();
	protected LinkedList<String> passes = new LinkedList<>();

	public void render(CacheManager cache, GameEngine engine) {
		int fbo = GL40.glGenFramebuffers();
		GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, fbo);

		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();

		int text = GL40.glGenTextures();
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, width, height, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, MemoryUtil.NULL);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);
		GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, GL40.GL_COLOR_ATTACHMENT0, GL40.GL_TEXTURE_2D, text, 0);
		// GL40.glDrawBuffer(GL40.GL_COLOR_ATTACHMENT0);

		int depth = GL40.glGenTextures();
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, depth);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_DEPTH_COMPONENT32, width, height, 0, GL40.GL_DEPTH_COMPONENT, GL40.GL_FLOAT,
				MemoryUtil.NULL);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, GL40.GL_LINEAR);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, GL40.GL_LINEAR);
		GL40.glFramebufferTexture2D(GL40.GL_FRAMEBUFFER, GL40.GL_DEPTH_ATTACHMENT, GL40.GL_TEXTURE_2D, depth, 0);

		GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);

		System.err.println("w:" + width + " h:" + height);

		if (GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER) != GL40.GL_FRAMEBUFFER_COMPLETE) {
			GlobalLogger.log(Level.SEVERE,
					"Framebuffer not complete: " + GL40.glCheckFramebufferStatus(GL40.GL_FRAMEBUFFER) + ", w:" + width + " h:" + height);
			return;
		}

		GL40.glClearColor(background.x, background.y, background.z, background.w);
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		for (String l : layers) {
			if (l == null) continue;

			RenderLayer rl = cache.getRenderLayer(l);
			if (rl == null) {
				GlobalLogger.log(Level.WARNING, "Render Layer: " + l + ", not found in Cache");
				break;
			}

			if (!rl.isVisible()) continue;

			rl.render(cache, engine);

			// combine texture with frame buffer
		}

		GL40.glDepthMask(false);
		for (String l : passes) {
			if (l == null) continue;

			RenderLayer prl = cache.getRenderLayer(l);
			if (prl == null) {
				GlobalLogger.log(Level.WARNING, "Pass Render Layer: " + prl.getId() + ", not found in Cache");
				break;
			}

			if (!(prl instanceof PassRenderLayer)) continue;

			GL40.glActiveTexture(GL40.GL_TEXTURE0);
			GL40.glBindTexture(GL40.GL_TEXTURE_2D, text);

			GL40.glActiveTexture(GL40.GL_TEXTURE1);
			GL40.glBindTexture(GL40.GL_TEXTURE_1D, depth);

			prl.render(cache, engine);
		}
		GL40.glDepthMask(true);

		GL40.glBindFramebuffer(GL40.GL_FRAMEBUFFER, 0);
		GL40.glBindFramebuffer(GL40.GL_READ_FRAMEBUFFER, fbo);
		// GL40.glBindRenderbuffer(GL40.GL_RENDERBUFFER, 0);

		GL40.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL40.GL_COLOR_BUFFER_BIT, GL40.GL_NEAREST);

		GL40.glDeleteTextures(text);
		GL40.glDeleteFramebuffers(fbo);
	}

	public void addRenderLayer(int i, RenderLayer id) {
		if (id instanceof PassRenderLayer) {
			passes.add(i, id.getId());
		} else {
			layers.add(i, id.getId());
		}
	}

}
