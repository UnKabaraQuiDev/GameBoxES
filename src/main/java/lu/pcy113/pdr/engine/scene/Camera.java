package lu.pcy113.pdr.engine.scene;

import lu.pcy113.pdr.engine.utils.transform.Transform;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class Camera {
	
	protected Transform transform;
	
	protected Projection projection;
	
	public Camera(Transform tr, Projection proj) {
		this.transform = tr;
		this.projection = proj;
	}
	
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	
	public static final Camera camera3D() {
		return new Camera(new Transform3D(), new Projection((float) Math.toRadians(60), 0.01f, 1000f));
	}
	
}
