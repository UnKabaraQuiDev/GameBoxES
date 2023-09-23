package lu.pcy113.pdr.engine.graph.composition;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public abstract class RenderLayer<K, O extends Renderable> implements Renderable, UniqueID, Cleanupable {
	
	protected final String name;
	protected boolean visible = true;
	protected O target;
	
	public RenderLayer(String name, O t) {
		this.target = t;
		this.name = name;
	}
	
	public abstract void render(CacheManager cache, K parent);
	
	public O getTarget() {return target;}
	public void setTarget(O target) {this.target = target;}
	public boolean isVisible() {return visible;}
	public void setVisible(boolean visible) {this.visible = visible;}
	
	
	@Override
	public void cleanup() {}
	@Override
	public String getId() {return name;}
	
}
