package lu.pcy113.pdr.engine.scene.geom;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
	
	private Vector3f position;
	private Quaternionf rotation;
	private Vector3f scale;
	
	private Matrix4f matrix;
	
	public Transform(Vector3f pos, Quaternionf rot, Vector3f scale) {
		this.position = pos;
		this.rotation = rot;
		this.scale = scale;
		
		this.matrix = new Matrix4f();
		this.updateMatrix();
	}
	public Transform() {
		this(new Vector3f(), new Quaternionf(), new Vector3f(1,1,1));
	}
	
	public Transform setPosition(float x, float y, float z) {
		position.set(x, y, z);
		return this;
	}
	public Transform translate(float x, float y, float z) {
		position.add(x, y, z);
		return this;
	}
	public Transform rotateRad(float x, float y, float z) {
		rotation.rotateXYZ(x, y, z);
		return this;
	}
	public Transform rotateDeg(float x, float y, float z) {
		rotation.rotateXYZ((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		return this;
	}
	public Transform rotateAlongAxisAngleDeg(float x, float y, float z, float a) {
		rotation.rotateAxis(a, x, y, z);
		return this;
	}
	public Transform rotateAlongAxisAngleDeg(Vector3f axis, float a) {
		rotation.rotateAxis(a, axis);
		return this;
	}
	public Transform setRotateAlongAxisDeg(float x, float y, float z, float a) {
		rotation.fromAxisAngleDeg(x, y, z, a);
		return this;
	}
	public Transform setRotateAlongAxisDeg(Vector3f axis, float a) {
		rotation.fromAxisAngleDeg(axis, a);
		return this;
	}
	public Transform setScale(float x, float y, float z) {
		scale.set(x, y, z);
		return this;
	}
	
	public Matrix4f updateMatrix() {
		matrix.translationRotateScale(position, rotation, scale);
		return matrix;
	}
	
	public Vector3f getPosition() {return position;}
	public Quaternionf getRotation() {return rotation;}
	public Vector3f getScale() {return scale;}
	public Matrix4f getMatrix() {return matrix;}
	public void setPosition(Vector3f position) {this.position = position;}
	public void setRotation(Quaternionf rotation) {this.rotation = rotation;}
	public void setScale(Vector3f scale) {this.scale = scale;}
	public void setMatrix(Matrix4f matrix) {this.matrix = matrix;}
	
}
