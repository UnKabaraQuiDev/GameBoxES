package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class ModelComponent extends Component {
	
	private String modelId;
	
	public ModelComponent(Model model) {
		this.modelId = model.getId();
	}
	public ModelComponent(String modelId) {
		this.modelId = modelId;
	}
	
	public void update(float dUpdate) {}
	public void render(float dRender) {}
	
	public String getModelId() {return modelId;}
	public void setModelId(String modelId) {this.modelId = modelId;}
	
	public Model getModel(CacheManager cache) {return cache.getModel(modelId);}
	public void setModel(Model model) {this.modelId = model.getId();}

}
