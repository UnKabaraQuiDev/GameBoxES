package lu.pcy113.pdr.engine.anim;

import lu.pcy113.pdr.engine.utils.interpol.Interpolator;

public abstract class CallbackValueInterpolation<T, D extends Number> {

	public T object;
	public D start, end;
	public float progress = 0f;
	public Interpolator interpolator;

	public CallbackValueInterpolation(T object, D start, D end, Interpolator interpolator) {
		this.object = object;
		this.start = start;
		this.end = end;
		this.interpolator = interpolator;
	}

	public void set(float x) {
		this.progress = x;
		this.callback(this.object, this.evaluate(this.interpolator.evaluate(this.progress)));
	}

	public void add(float x) {
		this.progress += x;
		this.callback(this.object, this.evaluate(this.interpolator.evaluate(this.progress)));
	}

	public abstract D evaluate(float progress);

	public abstract void callback(T object, D value);

	public void setStart(D start) { this.start = start; }

	public D getStart() { return this.start; }

	public D getEnd() { return this.end; }

	public void setEnd(D end) { this.end = end; }

	public Interpolator getInterpolator() { return this.interpolator; }

	public void setInterpolator(Interpolator interpolator) { this.interpolator = interpolator; }

	public T getObject() { return this.object; }

	public void setObject(T object) { this.object = object; }

	public float getProgress() { return this.progress; }

	public void setProgress(float progress) { this.progress = progress; }

}
