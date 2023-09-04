package lu.pcy113.pdr.engine.graph;

import lu.pcy113.pdr.engine.impl.Renderable;

public abstract class Renderer<T> {
	
	protected RenderManager renderManager;
	
	protected final Class<? extends Renderable> renderableClass;
	
	public Renderer(RenderManager rm, Class<? extends Renderable> renderable) {
		this.renderManager = rm;
		this.renderableClass = renderable;
	}
	
	public abstract void prepare();
	
	public abstract void render(T obj);
	
	public Class<? extends Renderable> getRenderableClass() {return renderableClass;}
	public RenderManager getRenderManager() {return renderManager;}
	
}
