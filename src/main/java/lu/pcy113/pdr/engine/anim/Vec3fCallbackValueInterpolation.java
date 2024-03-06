package lu.pcy113.pdr.engine.anim;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.utils.interpolation.Interpolator;

public abstract class Vec3fCallbackValueInterpolation<T> extends CallbackValueInterpolation<T, Vector3f> {

	public Vec3fCallbackValueInterpolation(T object, Vector3f start, Vector3f end, Interpolator interpolator) {
		super(object, start, end, interpolator);
	}

	@Override
	public Vector3f evaluate(float progress) {
		return new Vector3f(
				start).lerp(
						end,
						progress);
	}

}
