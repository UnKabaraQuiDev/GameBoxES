package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.ParticleEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class ParticleEmitterModelComponent extends Component implements Renderable {
	
	private String particleEmitterModelId;
	
	public ParticleEmitterModelComponent(ParticleEmitterModel particleEmitterModel) {
		this.particleEmitterModelId = particleEmitterModel.getId();
	}
	public ParticleEmitterModelComponent(String particleEmitterModelId) {
		this.particleEmitterModelId = particleEmitterModelId;
	}
	
	public String getParticleEmitterModelId() {return particleEmitterModelId;}
	public void setParticleEmitterModelId(String particleEmitterModelId) {this.particleEmitterModelId = particleEmitterModelId;}
	
	public ParticleEmitterModel getParticleEmitterModel(CacheManager cache) {return cache.getParticleEmitterModel(particleEmitterModelId);}
	public void setParticleEmitterModel(ParticleEmitterModel particleEmitterModel) {this.particleEmitterModelId = particleEmitterModel.getId();}

}
