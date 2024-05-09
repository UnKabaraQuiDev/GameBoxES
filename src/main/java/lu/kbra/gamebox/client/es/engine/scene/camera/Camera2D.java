package lu.kbra.gamebox.client.es.engine.scene.camera;

import java.io.PrintStream;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;

public class Camera2D extends Camera {

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

	public Vector2f getPosition() {
		return position;
	}

	public Camera2D setPosition(Vector2f position) {
		this.position = position;
		return this;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

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
	
	@Override
	public void dump(PrintStream out) {
		out.println("Position: "+position);
		out.println("Rotation: "+rotation);
		projection.dump(out);
	}

}
