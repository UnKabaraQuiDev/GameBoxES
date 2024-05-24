package lu.kbra.gamebox.client.es.engine.scene.camera;

import java.io.PrintStream;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.GameEngine;

public class Camera3D extends Camera {

	protected Vector3f position;
	// protected float pitch = 0, yaw = 0, roll = 0;
	protected Quaternionf rotation;
	protected Vector3f up = GameEngine.UP;

	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;

		lookAt(position, new Vector3f(0));
		
		updateMatrix();
	}

	public Camera3D lookAt(Vector3f from, Vector3f to) {
		Vector3f forward = new Vector3f(to).sub(from).normalize();
		Vector3f right = forward.cross(up, new Vector3f()).normalize();

		Vector3f up = right.cross(forward, new Vector3f()).normalize();

		rotation.identity().lookAlong(forward, up);

		position = new Vector3f(from);

		return this;
	}

	public Camera3D roll(float z) {
		rotation.rotateLocalZ(z);
		return this;
	}

	public Camera3D move(float ax, float ay, float moveSpeed) {
		position.add(new Vector3f(ax * moveSpeed, ay * moveSpeed, 0));
		return this;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Camera3D setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	@Deprecated
	public Camera3D loadRotation() {
		return loadRotation(viewMatrix);
	}

	@Deprecated
	public Camera3D loadRotation(Matrix4f matrix) {
		return setRotation(matrix.getUnnormalizedRotation(new Quaternionf()));
	}

	@Deprecated
	public Camera3D loadPosition() {
		return loadPosition(viewMatrix);
	}

	@Deprecated
	public Camera3D loadPosition(Matrix4f matrix) {
		return setPosition(matrix.getTranslation(new Vector3f()));
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Camera3D setRotation(Quaternionf rotation) {
		this.rotation = rotation;
		return this;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}

	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}

	@Override
	public void dump(PrintStream out) {
		out.println("Position: "+position);
		out.println("Rotation: "+rotation);
		out.println("Up: "+up);
		projection.dump(out);
	}

}
