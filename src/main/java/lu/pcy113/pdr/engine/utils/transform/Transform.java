package lu.pcy113.pdr.engine.utils.transform;

import org.joml.Matrix4f;

public abstract class Transform {

	protected Matrix4f matrix;

	public abstract Matrix4f updateMatrix();

	public Matrix4f getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}

	public abstract Transform clone();

}
