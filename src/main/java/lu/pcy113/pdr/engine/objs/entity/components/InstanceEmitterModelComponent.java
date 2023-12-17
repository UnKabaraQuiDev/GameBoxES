package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class InstanceEmitterModelComponent extends Component implements Renderable {

	private String instanceEmitterModelId;

	public InstanceEmitterModelComponent(InstanceEmitterModel instanceEmitter) {
		this.instanceEmitterModelId = instanceEmitter.getId();
	}

	public InstanceEmitterModelComponent(String instanceEmitterId) {
		this.instanceEmitterModelId = instanceEmitterId;
	}

	public String getInstanceEmitterModelId() {
		return this.instanceEmitterModelId;
	}

	public void setInstanceEmitterModelId(String instanceEmitterModelId) {
		this.instanceEmitterModelId = instanceEmitterModelId;
	}

	public InstanceEmitterModel getInstanceEmitterModel(CacheManager cache) {
		return cache.getInstanceEmitterModel(this.instanceEmitterModelId);
	}

	public void setInstanceEmitterModel(InstanceEmitterModel instanceEmitter) {
		this.instanceEmitterModelId = instanceEmitter.getId();
	}

}
