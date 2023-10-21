package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.GameEngine;

public class Camera3D extends Camera {
	
	protected Vector3f position;
	//protected float pitch = 0, yaw = 0, roll = 0;
	protected Quaternionf rotation;
	
	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;
	}
	
	public void lookAt(Vector3f from, Vector3f to) {
		// Calculate the forward direction (Z-axis) of the camera
		Vector3f forward = new Vector3f(to).sub(from).normalize();
		// Calculate the up direction (Y-axis) of the camera
		Vector3f up = new Vector3f(GameEngine.UP); // Assuming Y is the up direction
		// Calculate the right direction (X-axis) of the camera
		Vector3f right = new Vector3f(up).cross(forward).normalize();
		// Recalculate the up direction to ensure it's orthogonal to the forward and right directions
		up.set(forward).cross(right).normalize();
		// Create a rotation matrix that represents the camera's orientation
		Matrix4f viewMatrix = new Matrix4f().lookAt(from, to, up);
		// Convert the rotation matrix to a quaternion
		rotation.set(viewMatrix.getNormalizedRotation(new Quaternionf()));
		// Update the camera's position
		position.set(from);
		
		updateMatrix();
	}
	
	public void roll(float z) {
		rotation.rotateLocalZ(z);
	}
	
	public void move(float ax, float ay, float moveSpeed) {
		position.add(new Vector3f(ax*moveSpeed, ay*moveSpeed, 0));
	}
	
	public void move(float ax, float ay, float bx, float by, float moveSpeed, float rotationSpeed) {
		// Move the camera based on the local camera axis
		Vector3f movement = new Vector3f(ax * moveSpeed, 0, -ay * moveSpeed);
		movement.rotate(rotation);
		
		// Update the camera's position
		position.add(movement);
		
		// Rotate the camera based on bx and by
		Quaternionf rotationChange = new Quaternionf().rotateYXZ(bx * rotationSpeed, by * rotationSpeed, 0);
		
		// Update the camera's rotation
		rotation.mul(rotationChange);
	}
	
	public Vector3f getPosition() {return position;}
	public Camera3D setPosition(Vector3f position) {this.position = position; return this;}
	public Quaternionf getRotation() {return rotation;}
	public void setRotation(Quaternionf rotation) {this.rotation = rotation;}
	
	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		/*viewMatrix.set(rotation);
		viewMatrix.m30(position.x);
		viewMatrix.m31(position.y);
		viewMatrix.m32(position.z);
		viewMatrix.invert();*/
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x, -position.y, -position.z);
		System.err.println("VEC "+position+"\n"+rotation);
		
		return viewMatrix;
	}
	
}
