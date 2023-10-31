package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class InstanceEmitterModel implements UniqueID, Renderable {
	
	public static final String NAME = InstanceEmitterModel.class.getName();
	
	private final String name;
	private String emitter;
	
	public InstanceEmitterModel(String name, InstanceEmitter emitter) {
		this.name = name;
		this.emitter = emitter.getId();
	}
	
	@Override
	public String getId() {
		return name;
	}
	public String getEmitter() {return emitter;}
	public InstanceEmitter getEmitter(CacheManager cache) {return cache.getInstanceEmitter(emitter);}
	public void setEmitter(String emitter) {this.emitter = emitter;}
	
}
