package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera3D extends Camera {
	
	protected Vector3f position;
	//protected float pitch = 0, yaw = 0, roll = 0;
	protected Quaternionf rotation;
	
	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;
	}
	
	public Vector3f getPosition() {return position;}
	public Camera3D setPosition(Vector3f position) {this.position = position; return this;}
	public Quaternionf getRotation() {return rotation;}
	public void setRotation(Quaternionf rotation) {this.rotation = rotation;}
	
	public void rotate(float leftRight, float upDown) {
		// Rotate the camera using a quaternion
		rotation.rotateX(upDown).rotateY(leftRight);
	}

	public void moveLocalXZ(float leftRight, float forwardsBackwards) {
		// Calculate the movement vector in the camera's local coordinate system
		position.add(rotation.positiveZ(new Vector3f()).mul(forwardsBackwards));
		position.add(rotation.positiveX(new Vector3f()).mul(leftRight));
	}
	
	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
}
