package lu.pcy113.pdr.engine.utils;

public final class MathUtils {

	public static float snap(float x, float interval) {
		return Math.round(x / interval) * interval;
	}

}
