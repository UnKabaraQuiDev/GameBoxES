package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class Camera {
	
	protected Projection projection;
	
	protected Matrix4f viewMatrix;
	
	public Camera(Projection proj) {
		this.projection = proj;
		this.viewMatrix = new Matrix4f();
	}
	
	public abstract Matrix4f updateMatrix();
	
	public Matrix4f getViewMatrix() {return viewMatrix;}
	
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	
	public static final Camera3D camera3D() {
		return new Camera3D(new Vector3f(), new Quaternionf(), new Projection((float) Math.toRadians(60), 0.01f, 1000f));
	}
	
}
