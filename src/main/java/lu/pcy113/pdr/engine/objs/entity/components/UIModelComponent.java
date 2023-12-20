package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.UIModel;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class UIModelComponent extends Component implements Renderable {

	private String uiModelId;

	public UIModelComponent(UIModel model) {
		this.uiModelId = model.getId();
	}

	public UIModelComponent(String uiModelId) {
		this.uiModelId = uiModelId;
	}

	public String getUIModelId() {
		return this.uiModelId;
	}

	public void setUIModelId(String uiModelId) {
		this.uiModelId = uiModelId;
	}

	public UIModel getUIModel(CacheManager cache) {
		return cache.getUIModel(this.uiModelId);
	}

	public void setUIModel(UIModel model) {
		this.uiModelId = model.getId();
	}

}
