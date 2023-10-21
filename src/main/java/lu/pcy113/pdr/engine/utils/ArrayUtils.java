package lu.pcy113.pdr.engine.utils;

import java.util.Arrays;

public final class ArrayUtils {

	public static int[] intCountingUp(int start, int end) {
		int[] in = new int[end - start];
		for(int i = 0; i < in.length; i++)
			in[i] = start + i;
		return in;
	}

	public static int[] intCountingUp(int start, int end, int steps, int count) {
		int[] in = new int[end - start];
		for(int i = 0; i < in.length; i++)
			in[i] = start + steps*(i/count);
		return in;
	}
	
	public static int[] toPrimitiveInt(Integer[] data) {
		return Arrays.stream(data).mapToInt(Integer::intValue).toArray();
	}
	
	public static float[] toPrimitiveFloat(Float[] data) {
		float[] y = new float[data.length];
		for(int i = 0; i < data.length; i++)
			y[i] = data[i].floatValue();
		return y;
	}
	
}
