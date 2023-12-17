package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class ModelComponent extends Component implements Renderable {

	private String modelId;

	public ModelComponent(Model model) {
		this.modelId = model.getId();
	}

	public ModelComponent(String modelId) {
		this.modelId = modelId;
	}

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Model getModel(CacheManager cache) {
		return cache.getModel(this.modelId);
	}

	public void setModel(Model model) {
		this.modelId = model.getId();
	}

}
