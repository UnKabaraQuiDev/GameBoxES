package lu.kbra.gamebox.client.es.engine.anim;

import org.joml.Math;

import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolator;

public abstract class CallbackValueInterpolation<T, D> {

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

	public synchronized CallbackValueInterpolation<T, D> set(float x) {
		synchronized (this) {
			this.progress = x;
		}
		return this;
	}

	public synchronized CallbackValueInterpolation<T, D> add(float x) {
		this.progress += x;
		return this;
	}

	public CallbackValueInterpolation<T, D> exec() {
		this.callback(this.object, this.evaluate(this.interpolator.evaluate(this.progress)));
		return this;
	}

	public abstract D evaluate(float progress);

	public abstract void callback(T object, D value);

	public synchronized CallbackValueInterpolation<T, D> clamp() {
		this.progress = Math.clamp(0, 1, progress);
		return this;
	}

	public synchronized CallbackValueInterpolation<T, D> mod() {
		this.progress %= 1;
		return this;
	}

	public void setStart(D start) {
		this.start = start;
	}

	public D getStart() {
		return this.start;
	}

	public D getEnd() {
		return this.end;
	}

	public void setEnd(D end) {
		this.end = end;
	}

	public Interpolator getInterpolator() {
		return this.interpolator;
	}

	public void setInterpolator(Interpolator interpolator) {
		this.interpolator = interpolator;
	}

	public T getObject() {
		return this.object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public float get() {
		return this.progress;
	}

}
