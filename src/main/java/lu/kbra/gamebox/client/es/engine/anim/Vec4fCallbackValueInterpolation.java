package lu.kbra.gamebox.client.es.engine.anim;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolator;

public abstract class Vec4fCallbackValueInterpolation<T> extends CallbackValueInterpolation<T, Vector4f> {

	public Vec4fCallbackValueInterpolation(T object, Vector4f start, Vector4f end, Interpolator interpolator) {
		super(object, start, end, interpolator);
	}

	@Override
	public Vector4f evaluate(float progress) {
		return new Vector4f(start).lerp(end, progress);
	}

}
