package lu.pcy113.pdr.engine.geom.particles;

import lu.pcy113.pdr.engine.utils.transform.Transform;

public class Particle {
	
	private Transform transform;
	private final int index;
	
	public Particle(Transform transform, int index) {
		this.transform = transform;
		this.index = index;
	}
	
	public Transform getTransform() {
		return transform;
	}
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	public int getIndex() {
		return index;
	}
	
}
