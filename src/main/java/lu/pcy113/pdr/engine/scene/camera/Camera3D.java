package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.GameEngine;

public class Camera3D extends Camera {

	protected Vector3f position;
	// protected float pitch = 0, yaw = 0, roll = 0;
	protected Quaternionf rotation;

	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;

		viewMatrix.setLookAt(
				position, // Camera position
				new Vector3f(
						0.0f,
						0.0f,
						0.0f), // Look at position
				GameEngine.UP // Up vector
		);
	}

	public Camera3D lookAt(Vector3f from, Vector3f to) {
		viewMatrix.setLookAt(
				from, // Camera position
				to, // Look at position
				GameEngine.UP // Up vector
		);

		return this;
	}

	public Camera3D roll(float z) {
		rotation.rotateLocalZ(
				z);
		return this;
	}

	public Camera3D move(float ax, float ay, float moveSpeed) {
		position.add(
				new Vector3f(
						ax * moveSpeed,
						ay * moveSpeed,
						0));
		return this;
	}

	public Camera3D move(float ax, float ay, float bx, float by, float moveSpeed, float rotationSpeed) {
		// Move the camera based on the local camera axis
		Vector3f movement = new Vector3f(
				ax * moveSpeed,
				0,
				-ay * moveSpeed);
		movement.rotate(
				rotation);

		// Update the camera's position
		position.add(
				movement);

		// Rotate the camera based on bx and by
		Quaternionf rotationChange = new Quaternionf().rotateYXZ(
				bx * rotationSpeed,
				by * rotationSpeed,
				0);

		// Update the camera's rotation
		rotation.mul(
				rotationChange);

		return this;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Camera3D setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Camera3D setRotation(Quaternionf rotation) {
		this.rotation = rotation;
		return this;
	}

	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate(
				rotation);
		viewMatrix.translate(
				-position.x,
				-position.y,
				-position.z);
		return viewMatrix;
	}

}
