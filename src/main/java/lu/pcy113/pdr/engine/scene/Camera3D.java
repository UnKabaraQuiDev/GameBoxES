package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D extends Camera {
	
	protected Vector3f position;
	protected float pitch, yaw;
	
	public Camera3D(Vector3f position, float pitch, float yaw, Projection proj) {
		super(proj);
		this.position = position;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public Vector3f getPosition() {return position;}
	public void setPosition(Vector3f position) {this.position = position;}
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.translateLocal(-position.x, -position.y, -position.z);
		viewMatrix.rotateX((float) Math.toRadians(pitch));
		viewMatrix.rotateY((float) Math.toRadians(yaw));
		return viewMatrix;
	}
	
}
