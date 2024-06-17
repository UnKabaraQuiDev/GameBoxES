package lu.kbra.gamebox.client.es.engine.utils;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengles.GLES20;

import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESBadAccessException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESBadAllocException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESBadAttributeException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESBadContextException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESNotInitializedException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.GLESRuntimeException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALInvalidEnumException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALInvalidNameException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALInvalidOperationException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALInvalidValueException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALOutOfMemoryException;
import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALRuntimeException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLContextLost;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLInvalidEnumException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLInvalidFrameBufferOperationException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLInvalidIndexException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLInvalidOperationException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLInvalidValueException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLOutOfMemoryException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLRuntimeException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLStackOverflowException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengl.GLStackUnderflowException;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTask;

public final class PDRUtils {

	public static String getCallerClassName(boolean parent) {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!PDRUtils.class.getName().equals(ste.getClassName())) {
				if (!parent)
					return ste.getClassName() + "#" + ste.getMethodName() + "@" + ste.getLineNumber();
				else {
					ste = stElements[i + 1];
					return ste.getClassName() + "#" + ste.getMethodName() + "@" + ste.getLineNumber();
				}

			}
		}
		return null;
	}

	public static <T> T alcNullError(T obj, String string) {
		PDRUtils.alcError(obj == null, string);
		return obj;
	}

	public static void alcError(boolean b, String string) {
		if (b) {
			throw new ALRuntimeException(string);
		}
	}

	public static boolean checkAlError() {
		return checkAlError("");
	}

	public static boolean checkAlError(String msg) throws ALRuntimeException {
		int status = AL11.alGetError();

		if (status == AL11.AL_NO_ERROR)
			return true;

		String caller = getCallerClassName(false);

		switch (status) {
		/*
		 * case AL11.AL_INVALID_DEVICE: throw new ALInvalidDeviceException(caller,
		 * status, msg);
		 */
		case AL11.AL_INVALID_OPERATION:
			throw new ALInvalidOperationException(caller, status, msg);
		/*
		 * case AL11.AL_INVALID_CONTEXT: throw new ALInvalidContextException(caller,
		 * status, msg);
		 */
		case AL11.AL_INVALID_NAME:
			throw new ALInvalidNameException(caller, status, msg);
		case AL11.AL_INVALID_ENUM:
			throw new ALInvalidEnumException(caller, status, msg);
		case AL11.AL_INVALID_VALUE:
			throw new ALInvalidValueException(caller, status, msg);
		case AL11.AL_OUT_OF_MEMORY:
			throw new ALOutOfMemoryException(caller, status, msg);
		default:
			return true;
		}
	}

	public static boolean checkAlcError(long device) {
		boolean b;
		if (b = ALC11.alcGetError(device) != ALC11.ALC_NO_ERROR)
			throw new RuntimeException("Al error triggered: " + AL11.alGetError());
		return b;
	}

	public static boolean checkGlESError() {
		return checkGlESError("");
	}

	public static boolean checkGlESError(String msg) {
		int status = GLES20.glGetError();

		if (status == GLES20.GL_NO_ERROR)
			return true;

		String caller = getCallerClassName(false);

		switch (status) {
		case GLES20.GL_INVALID_ENUM:
			throw new GLESNotInitializedException(caller, status, msg);
		case GLES20.GL_INVALID_VALUE:
			throw new GLESBadAccessException(caller, status, msg);
		case GLES20.GL_INVALID_OPERATION:
			throw new GLESBadAllocException(caller, status, msg);
		case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
			throw new GLESBadAttributeException(caller, status, msg);
		case GLES20.GL_OUT_OF_MEMORY:
			throw new GLESBadContextException(caller, status, msg);
		// case GL45.GL_TABLE_TOO_LARGE:
		default:
			return true;
		}
	}
	
	public static void throwGLESError(String string) {
		throw new GLESRuntimeException(string);
	}
	
	public static boolean checkGlError() {
		return checkGlError("");
	}

	public static boolean checkGlError(String msg) {
		int status = GL11.glGetError();

		if (status == GL40.GL_NO_ERROR)
			return true;

		String caller = getCallerClassName(false);

		switch (status) {
		case GL40.GL_INVALID_OPERATION:
			throw new GLInvalidOperationException(caller, status, msg);
		case GL40.GL_INVALID_INDEX:
			throw new GLInvalidIndexException(caller, status, msg);
		case GL40.GL_INVALID_ENUM:
			throw new GLInvalidEnumException(caller, status, msg);
		case GL40.GL_INVALID_VALUE:
			throw new GLInvalidValueException(caller, status, msg);
		case GL40.GL_INVALID_FRAMEBUFFER_OPERATION:
			throw new GLInvalidFrameBufferOperationException(caller, status, msg);
		case GL40.GL_STACK_OVERFLOW:
			throw new GLStackOverflowException(caller, status, msg);
		case GL40.GL_STACK_UNDERFLOW:
			throw new GLStackUnderflowException(caller, status, msg);
		case GL40.GL_OUT_OF_MEMORY:
			throw new GLOutOfMemoryException(caller, status, msg);
		case GL45.GL_CONTEXT_LOST:
			throw new GLContextLost(caller, status, msg);
		// case GL45.GL_TABLE_TOO_LARGE:
		default:
			return true;
		}
	}

	public static void throwGLError(String string) {
		throw new GLRuntimeException(string);
	}

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

	public static int[] toPrimitiveInt(Object[] data) {
		return Arrays.stream(data).map((Object i) -> (int) (i == null ? 0 : i)).mapToInt(Integer::intValue).toArray();
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
		byte[] c = new byte[cb.remaining()];
		cb.get(c);
		cb.position(old);
		return c;
	}

	public static double round(double round, int decimales) {
		double places = Math.pow(10, decimales);
		return Math.round(round * places) / places;
	}

	public static float applyMinThreshold(float x, float min) {
		return Math.abs(x) < min ? 0 : x;
	}

	public static Color randomColor(boolean alpha) {
		if (alpha)
			return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		else
			return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
	}

	public static Color clampColor(int red, int green, int blue) {
		return new Color(org.joml.Math.clamp(0, 255, red), org.joml.Math.clamp(0, 255, green), org.joml.Math.clamp(0, 255, blue));
	}

	public static Color clampColor(int red, int green, int blue, int alpha) {
		return new Color(org.joml.Math.clamp(0, 255, red), org.joml.Math.clamp(0, 255, green), org.joml.Math.clamp(0, 255, blue), org.joml.Math.clamp(0, 255, alpha));
	}

	public static String fillString(String str, String place, int length) {
		return (str.length() < length ? repeatString(place, length - str.length()) + str : str);
	}

	public static String repeatString(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++)
			sb.append(str);
		return sb.toString();
	}

	public static Vector3f[] floatArrayToVec3f(float[] arr) {
		return IntStream.range(0, arr.length / 3).mapToObj(i -> new Vector3f(arr[i * 3 + 0], arr[i * 3 + 1], arr[i * 3 + 2])).toArray(Vector3f[]::new);
	}

	public static Vector2f[] floatArrayToVec2f(float[] arr) {
		return IntStream.range(0, arr.length / 2).mapToObj(i -> new Vector2f(arr[i * 2 + 0], arr[i * 2 + 1])).toArray(Vector2f[]::new);
	}

	public static Vector3f[] intArrayToVec3f(int[] arr) {
		return IntStream.range(0, arr.length / 3).mapToObj(i -> new Vector3f(arr[i * 3 + 0], arr[i * 3 + 1], arr[i * 3 + 2])).toArray(Vector3f[]::new);
	}

	public static int[] castInt(Object[] arr) {
		return Arrays.stream(arr).mapToInt(s -> (int) s).toArray();
	}

	public static int[] castInt(Integer[] arr) {
		return Arrays.stream(arr).mapToInt(Integer::valueOf).toArray();
	}

	public static Object[] toObjectArray(int[] data) {
		return Arrays.stream(data).mapToObj(Integer::valueOf).toArray();
	}

	public static ByteBuffer intArrayToByteBuffer(int[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length * Integer.BYTES);
		for (int i = 0; i < data.length; i++) {
			buffer.putInt(data[i]);
		}
		return (ByteBuffer) buffer.flip();
	}

	public static IntStream intToBytes(int value) {
		return IntStream.of((value >> 24) & 0xFF, (value >> 16) & 0xFF, (value >> 8) & 0xFF, value & 0xFF);
	}

	public static int[] byteBufferToIntArray(ByteBuffer bData, int length) {
		int[] data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = bData.getInt();
		}
		return data;
	}

	public static Vector2f getCoordinates(Vector2f in, int[] viewport) {
		return new Vector2f(in.x, viewport[3] - in.y);
	}

	public static Vector3f getCoordinates(Vector3f in, int[] viewport) {
		return new Vector3f(in.x, viewport[3] - in.y, in.z);
	}

	public static Vector2f loadRangeFloat(JSONObject sobj, String key) {
		JSONArray arr = sobj.getJSONArray(key);
		return new Vector2f(arr.getFloat(0), arr.getFloat(1));
	}

	public static int[] randomIntArray(int length, int min, int max) {
		Random rand = new Random();
		int[] arr = new int[length];
		for(int i = 0; i < length; i++) {
			arr[i] = rand.nextInt(max-min)+min;
		}
		return arr;
	}

	public static NextTask[] castNextTask(Object[] array) {
		NextTask[] nt = new NextTask[array.length];
		
		for(int i = 0; i < array.length; i++) {
			nt[i] = (NextTask) array[i];
		}
		
		return nt;
	}

}
