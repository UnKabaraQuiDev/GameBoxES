package lu.pcy113.pdr.engine.utils.transform;

public abstract class Transform<T> {

	protected T matrix;

	public abstract T updateMatrix();

	public T getMatrix() {
		return matrix;
	}

	public void setMatrix(T matrix) {
		this.matrix = matrix;
	}

	public abstract Transform<T> clone();

}
