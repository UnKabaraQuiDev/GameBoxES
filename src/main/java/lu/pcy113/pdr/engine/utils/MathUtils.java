package lu.pcy113.pdr.engine.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.GameEngine;

public final class MathUtils {

	public static float snap(float x, float interval) {
		return Math.round(x / interval) * interval;
	}

	public static boolean compare(float a, float b, float epsilon) {
		return Math.abs(a-b) <= epsilon;
	}

	public static Vector3f vec3fromQuatf(Quaternionf rotation) {
		return rotation.transform(GameEngine.Z_POS, new Vector3f());
	}

}
