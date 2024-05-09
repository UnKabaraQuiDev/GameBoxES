package lu.kbra.gamebox.client.es.engine.utils.transform;

import org.joml.Matrix4f;

public abstract class Transform implements Cloneable {

	protected Matrix4f matrix;

	public abstract Matrix4f updateMatrix();

	public Matrix4f getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix4f matrix) {
		this.matrix = matrix;
	}

	@Override
	public abstract Transform clone();

}
