package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lombok.Getter;
import lombok.Setter;

public class Transform {
	
	@Getter @Setter
	private Vector3f translation;
	@Getter @Setter
	private Quaternionf rotation;
	@Getter @Setter
	private Vector3f scale;
	@Getter @Setter
	private Matrix4f matrix;
	
	public Transform(Vector3f tr, Quaternionf ro, Vector3f sc) {
		this.translation = tr;
		this.rotation = ro;
		this.scale = sc;
		
		this.matrix = new Matrix4f();
	}
	public Transform() {
		this(new Vector3f(), new Quaternionf(), new Vector3f(1f));
	}
	
	public Transform translateAdd(Vector3f v) {
		translation.add(v);
		return this;
	}
	public Transform translateAdd(float x, float y, float z) {
		translation.add(x, y, z);
		return this;
	}
	
	public Transform rotateFromAxisAngleRad(Vector3f v, float angle) {
		rotation.fromAxisAngleRad(v, angle);
		return this;
	}
	public Transform rotateFromAxisAngleRad(float x, float y, float z, float angle) {
		rotation.fromAxisAngleDeg(x, y, z, angle);
		return this;
	}
	public Transform rotate(Quaternionf q) {
		rotation.add(q);
		return this;
	}
	public Transform rotate(Vector3f v) {
		rotation.transform(v);
		return this;
	}
	
	public Transform scaleAdd(Vector3f v) {
		translation.add(v);
		return this;
	}
	public Transform scaleAdd(float x, float y, float z) {
		translation.add(x, y, z);
		return this;
	}
	public Transform scaleMul(Vector3f v) {
		translation.mul(v);
		return this;
	}
	public Transform scaleMul(float x, float y, float z) {
		translation.mul(x, y, z);
		return this;
	}
	
	public Matrix4f updateMatrix() {
		matrix.translationRotateScale(translation, rotation, scale);
		return matrix;
	}
	
}
