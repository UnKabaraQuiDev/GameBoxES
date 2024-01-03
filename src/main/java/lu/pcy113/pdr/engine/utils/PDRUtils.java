package lu.pcy113.pdr.engine.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public final class PDRUtils {

	public static int[] intCountingUp(int start, int end) {
		int[] in = new int[end - start];
		for (int i = 0; i < in.length; i++)
			in[i] = start + i;
		return in;
	}

	public static int[] intCountingUpTriQuads(int quadCount) {
		int[] in = new int[quadCount * 6];
		for (int q = 0; q < quadCount; q++) {
			in[q * 6 + 0] = q * 4 + 0;
			in[q * 6 + 1] = q * 4 + 1;
			in[q * 6 + 2] = q * 4 + 2;
			in[q * 6 + 3] = q * 4 + 0;
			in[q * 6 + 4] = q * 4 + 2;
			in[q * 6 + 5] = q * 4 + 3;
		}
		return in;
	}

	public static int[] intCountingUp(int start, int end, int steps, int count) {
		int[] in = new int[end - start];
		for (int i = 0; i < in.length; i++)
			in[i] = start + steps * (i / count);
		return in;
	}

	public static int[] toPrimitiveInt(Integer[] data) {
		return Arrays.stream(data).map((Integer i) -> (i == null ? 0 : i)).mapToInt(Integer::intValue).toArray();
	}
	
	public static byte[] toPrimitiveByte(Byte[] data) {
		byte[] y = new byte[data.length];
		for (int i = 0; i < data.length; i++)
			y[i] = Byte.valueOf((byte) data[i]);
		return y;
	}

	public static float[] toPrimitiveFloat(Object[] data) {
		float[] y = new float[data.length];
		for (int i = 0; i < data.length; i++)
			y[i] = Float.valueOf((float) data[i]);
		return y;
	}

	/*
	 * public static int[] intRepeating(int[] is, int size) { int[] in = new
	 * int[size*is.length]; for(int i = 0; i < is.length; i++) for(int j = 0; j <
	 * size; j++) in[i*size+j] = is[i]; return in; }
	 */

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

	public static Vector2f[] vec2Repeating(Vector2f[] is, int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size should be greater than 0");
		}

		int originalLength = is.length;
		int repeatedLength = originalLength * size;
		Vector2f[] result = new Vector2f[repeatedLength];

		for (int i = 0; i < size; i++) {
			System.arraycopy(is, 0, result, i * originalLength, originalLength);
		}

		return result;
	}

	public static Matrix4f[] castArrayMat4f(Object[] transforms) {
		Matrix4f[] t = new Matrix4f[transforms.length];
		for (int i = 0; i < transforms.length; i++)
			t[i] = (Matrix4f) transforms[i];
		return t;
	}

	public static Matrix3x2f[] castArrayMat3x2f(Object[] transforms) {
		Matrix3x2f[] t = new Matrix3x2f[transforms.length];
		for (int i = 0; i < transforms.length; i++)
			t[i] = (Matrix3x2f) transforms[i];
		return t;
	}

	public static byte[] toByteArray(ByteBuffer cb) {
		int old = cb.position();
		System.out.println("pos: " + old + " " + cb.remaining());
		byte[] c = new byte[cb.remaining()];
		cb.get(c);
		System.out.println("cont: " + Arrays.toString(c));
		cb.position(old);
		return c;
	}

}
