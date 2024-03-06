package lu.pcy113.pdr.engine.utils.interpolation;

import org.joml.Matrix4f;

public enum Matrix4fValueInterpolators implements Matrix4fValueInterpolator {

	LERP {
		@Override
		public Matrix4f evaluate(Matrix4f one, Matrix4f two, float progress) {
			return new Matrix4f(
					one).lerp(
							two,
							progress);
		}
	}

}
