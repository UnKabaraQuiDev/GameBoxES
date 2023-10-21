package lu.pcy113.pdr.engine.utils;

import java.util.Arrays;

public final class ArrayUtils {

	public static int[] intCountingUp(int start, int end) {
		int[] in = new int[end - start];
		for(int i = 0; i < in.length; i++)
			in[i] = start + i;
		return in;
	}
	
	public static int[] intCountingUpTriQuads(int quadCount) {
		int[] in = new int[quadCount*6];
		for(int q = 0; q < quadCount; q++) {
			in[q*6+0] = q*4+0;
			in[q*6+1] = q*4+1;
			in[q*6+2] = q*4+2;
			in[q*6+3] = q*4+0;
			in[q*6+4] = q*4+2;
			in[q*6+5] = q*4+3;
		}
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

	/*public static int[] intRepeating(int[] is, int size) {
		int[] in = new int[size*is.length];
		for(int i = 0; i < is.length; i++)
			for(int j = 0; j < size; j++)
				in[i*size+j] = is[i];
		return in;
	}*/

	public static float[] floatRepeating(float[] is, int size) {
		if (size <= 0) {
	        throw new IllegalArgumentException("Size should be greater than 0");
	    }

	    int originalLength = is.length;
	    int repeatedLength = originalLength * size;
	    float[] result = new float[repeatedLength];

	    for (int i = 0; i < size; i++) {
	        System.arraycopy(is, 0, result, i * originalLength, originalLength);
	    }

	    return result;
	}
	
}
