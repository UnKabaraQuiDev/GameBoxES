package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera3D extends Camera {
	
	protected Vector3f position;
	//protected float pitch = 0, yaw = 0, roll = 0;
	protected Vector3f rotation;
	
	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = new Vector3f();
	}
	
	public Vector3f getPosition() {return position;}
	public Camera3D setPosition(Vector3f position) {this.position = position; return this;}
	public Vector3f getRotation() {return rotation;}
	public void setRotation(Vector3f rotation) {this.rotation = rotation;}
	
	public void rotate(float leftRight, float upDown) {
		// Rotate the camera using a quaternion
		rotation.add(-upDown, -leftRight, 0);
	}

	public void moveLocal(float leftRight, float forwardsBackwards) {
		// Calculate the movement vector in the camera's local coordinate system
		Vector3f movement = new Vector3f(leftRight, 0, forwardsBackwards);
		
		float x = (float) Math.sin(Math.toRadians(rotation.y));
		float z = (float) Math.cos(Math.toRadians(rotation.y));
		
		position.add(z * leftRight, 0, -x * leftRight);
		position.add(x * forwardsBackwards, 0, -z * forwardsBackwards);
	}

	
	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		//viewMatrix.translationRotateScale(position, rotation, 1);
		//viewMatrix.rotate(rotation.)
		//viewMatrix.rotateXYZ((float) Math.toRadians(pitch), (float) Math.toRadians(yaw), (float) Math.toRadians(roll));
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
}
