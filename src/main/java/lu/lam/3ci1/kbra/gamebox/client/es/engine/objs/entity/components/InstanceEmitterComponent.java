package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class InstanceEmitterComponent extends Component implements Renderable {

	private String instanceEmitterId;

	public InstanceEmitterComponent(InstanceEmitter instanceEmitter) {
		this.instanceEmitterId = instanceEmitter.getId();
	}

	public InstanceEmitterComponent(String instanceEmitterId) {
		this.instanceEmitterId = instanceEmitterId;
	}

	public String getInstanceEmitterId() {
		return this.instanceEmitterId;
	}

	public void setInstanceEmitterId(String instanceEmitterId) {
		this.instanceEmitterId = instanceEmitterId;
	}

	public InstanceEmitter getInstanceEmitter(CacheManager cache) {
		return cache.getInstanceEmitter(this.instanceEmitterId);
	}

	public void setInstanceEmitter(InstanceEmitter instanceEmitter) {
		this.instanceEmitterId = instanceEmitter.getId();
	}

}
