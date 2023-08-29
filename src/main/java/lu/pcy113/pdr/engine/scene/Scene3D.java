package lu.pcy113.pdr.engine.scene;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.graph.renderer.Renderer;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.engine.scene.geom.ModelInstance;
import lu.pcy113.pdr.utils.Logger;

public class Scene3D extends Scene {

	private Map<String, Model> models;
	//private Map<String, ModelInstance> instances;
	private Map<String, ParticleSystem> particles;
	
	public Scene3D(String id, int w, int h, Renderer renderer) {
		super(id, w, h, renderer);
		
		this.models = new HashMap<>();
		this.instances = new HashMap<>();
		//this.particles = new HashMap<>();
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		super.cleanup();
		
		models.values().forEach(Model::cleanup);
		//instances.values().forEach(ModelInstance::cleanup);
		particles.values().forEach(ParticleSystem::cleanup);
	}
	
	public void addModel(Model model) {models.put(model.getId(), model);}
	public Map<String, Model> getModels() {return models;}
	public void setModels(Map<String, Model> models) {this.models = models;}
	
	public void addInstances(ModelInstance inst) {instances.put(inst.getId(), inst);}
	public Map<String, ModelInstance> getInstances() {return instances;}
	public void setInstances(Map<String, ModelInstance> inst) {this.instances = inst;}
	
	/*public void addParticle(ParticleSystem part) {particles.put(part.getId(), part);}
	public Map<String, ParticleSystem> getParticles() {return particles;}
	public void setParticles(Map<String, ParticleSystem> parts) {this.particles = parts;}*/

}
