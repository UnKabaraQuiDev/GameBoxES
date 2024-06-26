package lu.kbra.gamebox.client.es.engine.graph.composition;

import java.util.LinkedList;
import java.util.logging.Level;

import org.joml.Vector2i;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.pcy113.pclib.logger.GlobalLogger;

public class Compositor implements Cleanupable {

	protected Vector4f background = new Vector4f(0.5f, 0.5f, 0.5f, 1);

	protected LinkedList<String> layers = new LinkedList<>();
	protected LinkedList<String> passes = new LinkedList<>();

	protected Vector2i resolution = new Vector2i(0, 0);

	public void render(CacheManager cache, GameEngine engine) {
		int width = engine.getWindow().getWidth();
		int height = engine.getWindow().getHeight();
		
		if (resolution.equals(width, height)) {
			// keep same texture
		} else {
			resolution = new Vector2i(width, height);
			GL_W.glViewport(0, 0, width, height);
		}

		GL_W.glEnable(GL_W.GL_DEPTH_TEST);

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

			rl.render(engine, null);
		}

		GL_W.glDepthMask(false);
		for (String l : passes) {
			if (l == null)
				continue;

			RenderLayer prl = cache.getRenderLayer(l);
			if (prl == null) {
				GlobalLogger.log(Level.WARNING, "Pass Render Layer: " + l + ", not found in Cache");
				break;
			}

			if (!(prl instanceof PassRenderLayer))
				continue;

			// color0.bind(0);
			// depth.bind

			((PassRenderLayer) prl).render(engine, null);
		}
		GL_W.glDepthMask(true);

		GL_W.glClearColor(background.x, background.y, background.z, background.w);
		GL_W.glClear(GL_W.GL_COLOR_BUFFER_BIT | GL_W.GL_DEPTH_BUFFER_BIT);
	}

	public void addRenderLayer(int i, RenderLayer id) {
		if (id instanceof PassRenderLayer)
			return;
		layers.add(i, id.getId());
	}

	public void addPassLayer(int i, PassRenderLayer id) {
		passes.add(i, id.getId());
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: "+getClass().getName());
	}

}
