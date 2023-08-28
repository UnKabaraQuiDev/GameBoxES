package lu.pcy113.pdr.engine.scene;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.graph.Model;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Scene implements Cleanupable {
	
	private Map<String, Model> models;
	
	private Projection projection;
	
	public Scene(int w, int h) {
		Logger.log();
		
		models = new HashMap<>();
		projection = new Projection(w, h);
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		models.values().forEach(Model::cleanup);
	}
	
	public void addEntity(Entity entity) {
		String modelId = entity.getModelId();
		Model model = models.get(modelId);
		if(model == null)
			throw new RuntimeException("Could not find model: "+modelId+" in entity: "+entity.getId());
		model.getEntities().add(entity);
	}
	
	public void addModel(Model model) {models.put(model.getId(), model);}
	public Map<String, Model> getModels() {return models;}
	public void setModels(Map<String, Model> models) {this.models = models;}
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	
	public void resize(int width, int height) {
		Logger.log();
		projection.updateProjMatrix(width, height);
	}
	
}
