package lu.kbra.gamebox.client.es.engine.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.GameEngine;

public final class MathUtils {

	public static float snap(float x, float interval) {
		return Math.round(x / interval) * interval;
	}

	public static boolean compare(float a, float b, float epsilon) {
		return Math.abs(a - b) <= epsilon;
	}

	public static Vector3f vec3fromQuatf(Quaternionf rotation) {
		return rotation.transform(GameEngine.Z_POS, new Vector3f());
	}

	public static int greatestAbsIndex(float... arr) {
		int index = -1;
		float max = Float.MIN_VALUE;
		for(int i = 0; i < arr.length; i++) {
			if(Math.abs(arr[i]) > max) {
				index = i;
				max = Math.abs(arr[i]);
			}
		}
		return index;
	}

}
