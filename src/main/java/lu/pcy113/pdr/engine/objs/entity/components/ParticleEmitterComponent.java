package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.particles.ParticleEmitter;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.ParticleEmitterModel;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class ParticleEmitterComponent extends Component implements Renderable {
	
	private String particleEmitterId;
	
	public ParticleEmitterComponent(ParticleEmitterModel particleEmitter) {
		this.particleEmitterId = particleEmitter.getId();
	}
	public ParticleEmitterComponent(String particleEmitterId) {
		this.particleEmitterId = particleEmitterId;
	}
	
	public String getParticleEmitterId() {return particleEmitterId;}
	public void setParticleEmitterId(String particleEmitterId) {this.particleEmitterId = particleEmitterId;}
	
	public ParticleEmitter getParticleEmitter(CacheManager cache) {return cache.getParticleEmitter(particleEmitterId);}
	public void setParticleEmitter(ParticleEmitter particleEmitter) {this.particleEmitterId = particleEmitter.getId();}

}
