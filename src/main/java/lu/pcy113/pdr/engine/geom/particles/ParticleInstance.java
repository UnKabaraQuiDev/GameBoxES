package lu.pcy113.pdr.engine.geom.particles;

import lu.pcy113.pdr.engine.utils.transform.Transform;

public class ParticleInstance {
	
	private final int index;
	private Transform transform;
	private Object[] buffers;
	
	public ParticleInstance(int index, Transform transform, Object... buffers) {
		this.index = index;
		this.transform = transform;
		this.buffers = buffers;
	}
	
	public int getIndex() {return index;}
	public Transform getTransform() {return transform;}
	public Object[] getBuffers() {return buffers;}
	public void setTransform(Transform transform) {this.transform = transform;}
	public void setBuffers(Object[] buffers) {
		if(this.buffers.length != buffers.length)
			return;
		this.buffers = buffers;
	}
	
}
