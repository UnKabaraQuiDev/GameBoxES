package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class InstanceEmitterModel implements UniqueID, Renderable {
	
	public static final String NAME = InstanceEmitterModel.class.getName();
	
	private final String name;
	private Transform transform;
	private String emitter;
	
	public InstanceEmitterModel(String name, InstanceEmitter emitter, Transform transform) {
		this.name = name;
		this.transform = transform;
		this.emitter = emitter.getId();
	}
	
	@Override
	public String getId() {
		return name;
	}
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}
	public String getEmitter() {return emitter;}
	public InstanceEmitter getEmitter(CacheManager cache) {return cache.getInstanceEmitter(emitter);}
	public void setEmitter(String emitter) {this.emitter = emitter;}
	
}
