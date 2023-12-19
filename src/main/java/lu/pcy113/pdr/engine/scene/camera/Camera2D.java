package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class Camera2D
		extends
		Camera {

	protected Vector2f position;
	protected Quaternionf rotation;

	public Camera2D(Vector2f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;
	}

	public void roll(float z) {
		rotation.rotateLocalZ(z);
	}

	public Vector2f getPosition() { return position; }

	public Camera2D setPosition(Vector2f position) {
		this.position = position;
		return this;
	}

	public Quaternionf getRotation() { return rotation; }

	public Camera2D setRotation(Quaternionf rotation) {
		this.rotation = rotation;
		return this;
	}

	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x, -position.y, 0);
		return viewMatrix;
	}

}
