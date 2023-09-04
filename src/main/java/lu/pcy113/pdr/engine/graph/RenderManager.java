package lu.pcy113.pdr.engine.graph;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.impl.Renderable;

public class RenderManager {
	
	private final Map<Class<?>, Renderer<?>> renderers;
	
	public RenderManager() {
		this.renderers = new HashMap<>();
	}
	
	public void addRenderer(Renderer<?> renderer) {
		renderers.put(renderer.getRenderableClass(), renderer);
	}
	public Renderer<?> getRenderer(Class<? extends Renderable> obj) {
		return renderers.get(obj);
	}
	
}
