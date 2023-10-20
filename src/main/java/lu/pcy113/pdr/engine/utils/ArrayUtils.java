package lu.pcy113.pdr.engine.utils;

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
	
}
