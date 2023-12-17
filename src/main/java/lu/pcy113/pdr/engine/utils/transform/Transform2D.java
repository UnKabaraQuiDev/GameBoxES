package lu.pcy113.pdr.engine.utils.transform;

import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public class Transform2D extends Transform<Matrix3x2f> {

	protected Vector2f translation;
	protected float rotation;
	protected Vector2f scale;

	public Transform2D() {
		this(new Vector2f(0), 0, new Vector2f(1));
	}

	public Transform2D(Vector2f trans, float rot, Vector2f scale) {
		this.translation = trans;
		this.rotation = rot;
		this.scale = scale;

		super.matrix = new Matrix3x2f();
	}

	public Transform2D translateAdd(Vector2f v) {
		translation.add(v);
		return this;
	}

	public Transform2D translateAdd(float x, float y) {
		translation.add(x, y);
		return this;
	}

	public Transform2D translateMul(Vector2f center, float x, float y) {
		translation.add(center.negate());
		translation.mul(x, y);
		translation.add(center);
		return this;
	}

	/**
	 * degrees
	 */
	public Transform2D rotate(float q) {
		rotation += Math.toRadians(q);
		rotation %= Math.PI * 2;
		return this;
	}

	public Transform2D scaleAdd(Vector2f v) {
		scale.add(v);
		return this;
	}

	public Transform2D scaleAdd(float x, float y) {
		scale.add(x, y);
		return this;
	}

	public Transform2D scaleMul(Vector2f v) {
		scale.mul(v);
		return this;
	}

	public Transform2D scaleMul(float x, float y) {
		scale.mul(x, y);
		return this;
	}

	@Override
	public Matrix3x2f updateMatrix() {
		return matrix.identity().translate(translation).rotate(rotation).scale(scale);
	}

	public Vector2f getTranslation() {
		return translation;
	}

	public float getRotation() {
		return rotation;
	}

	public Vector2f getScale() {
		return scale;
	}

	public Transform2D setTranslation(Vector2f translation) {
		this.translation = translation;
		return this;
	}

	public Transform2D setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}

	public Transform2D setScale(Vector2f scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public Transform clone() {
		return new Transform2D(translation, rotation, scale);
	}

	@Override
	public String toString() {
		return "Transform:2D[t" + translation + ", r" + rotation + ", s" + scale + "]";
	}

}
