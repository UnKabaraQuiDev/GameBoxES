package lu.kbra.gamebox.client.es.engine.utils;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.egl.EGL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.GLES30;

import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadAccessException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadAllocException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadAttributeException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadConfigException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadContextException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadCurrentSurfaceException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadDisplayException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadMatchException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadNativePixmapException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadNativeWindowException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadParameterException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLBadSurfaceException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLNotInitializedException;
import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLRuntimeException;
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
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESInvalidEnumException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESInvalidFramebufferOperationException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESInvalidIndexException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESInvalidOperationException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESInvalidValueException;
import lu.kbra.gamebox.client.es.engine.exceptions.opengles.GLESOutOfMemoryException;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTask;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W_GL40;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W_GLES30;
import lu.pcy113.pclib.PCUtils;

public final class PDRUtils {

	@Deprecated
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
		 * case AL11.AL_INVALID_DEVICE: throw new ALInvalidDeviceException(caller, status, msg);
		 */
		case AL11.AL_INVALID_OPERATION:
			throw new ALInvalidOperationException(caller, status, msg);
		/*
		 * case AL11.AL_INVALID_CONTEXT: throw new ALInvalidContextException(caller, status, msg);
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

	public static boolean checkGL_WError() {
		if (GL_W.WRAPPER instanceof GL_W_GLES30) {
			return checkGlESError();
		} else if (GL_W.WRAPPER instanceof GL_W_GL40) {
			return checkGlError();
		}
		return true;
	}

	public static boolean checkGL_WError(String str) {
		if (GL_W.WRAPPER instanceof GL_W_GLES30) {
			return checkGlESError(str);
		} else if (GL_W.WRAPPER instanceof GL_W_GL40) {
			return checkGlError(str);
		}
		return true;
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
		case GLES30.GL_INVALID_OPERATION:
			throw new GLESInvalidOperationException(caller, status, msg);
		case GLES30.GL_INVALID_INDEX:
			throw new GLESInvalidIndexException(caller, status, msg);
		case GLES30.GL_INVALID_ENUM:
			throw new GLESInvalidEnumException(caller, status, msg);
		case GLES30.GL_INVALID_VALUE:
			throw new GLESInvalidValueException(caller, status, msg);
		case GLES30.GL_INVALID_FRAMEBUFFER_OPERATION:
			throw new GLESInvalidFramebufferOperationException(caller, status, msg);
		case GLES30.GL_OUT_OF_MEMORY:
			throw new GLESOutOfMemoryException(caller, status, msg);
		default:
			return true;

		}

	}

	public static boolean checkEGLError(String msg) {
		int status = EGL10.eglGetError();

		if (status == EGL10.EGL_SUCCESS)
			return true;

		String caller = getCallerClassName(false);

		switch (status) {
		case EGL10.EGL_NOT_INITIALIZED:
			throw new EGLNotInitializedException(caller, status, msg);
		case EGL10.EGL_BAD_ACCESS:
			throw new EGLBadAccessException(caller, status, msg);
		case EGL10.EGL_BAD_ATTRIBUTE:
			throw new EGLBadAttributeException(caller, status, msg);
		case EGL10.EGL_BAD_ALLOC:
			throw new EGLBadAllocException(caller, status, msg);
		case EGL10.EGL_BAD_CONTEXT:
			throw new EGLBadContextException(caller, status, msg);
		case EGL10.EGL_BAD_CONFIG:
			throw new EGLBadConfigException(caller, status, msg);
		case EGL10.EGL_BAD_CURRENT_SURFACE:
			throw new EGLBadCurrentSurfaceException(caller, status, msg);
		case EGL10.EGL_BAD_DISPLAY:
			throw new EGLBadDisplayException(caller, status, msg);
		case EGL10.EGL_BAD_SURFACE:
			throw new EGLBadSurfaceException(caller, status, msg);
		case EGL10.EGL_BAD_MATCH:
			throw new EGLBadMatchException(caller, status, msg);
		case EGL10.EGL_BAD_PARAMETER:
			throw new EGLBadParameterException(caller, status, msg);
		case EGL10.EGL_BAD_NATIVE_PIXMAP:
			throw new EGLBadNativePixmapException(caller, status, msg);
		case EGL10.EGL_BAD_NATIVE_WINDOW:
			throw new EGLBadNativeWindowException(caller, status, msg);
		/*
		 * case EGL10.EGL_NO_CONTEXT: throw new EGLNoContextException(caller, status, msg);
		 */
		default:
			return true;
		}
	}

	public static void throwGLESError(String string) {
		throw new EGLRuntimeException(string);
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

	@Deprecated
	public static double round(double round, int decimales) {
		double places = Math.pow(10, decimales);
		return Math.round(round * places) / places;
	}

	public static float applyMinThreshold(float x, float min) {
		return Math.abs(x) < min ? 0 : x;
	}

	@Deprecated
	public static Color randomColor(boolean alpha) {
		if (alpha)
			return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		else
			return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
	}

	@Deprecated
	public static Color clampColor(int red, int green, int blue) {
		return new Color(org.joml.Math.clamp(0, 255, red), org.joml.Math.clamp(0, 255, green), org.joml.Math.clamp(0, 255, blue));
	}

	@Deprecated
	public static Color clampColor(int red, int green, int blue, int alpha) {
		return new Color(org.joml.Math.clamp(0, 255, red), org.joml.Math.clamp(0, 255, green), org.joml.Math.clamp(0, 255, blue), org.joml.Math.clamp(0, 255, alpha));
	}

	@Deprecated
	public static String fillString(String str, String place, int length) {
		return (str.length() < length ? repeatString(place, length - str.length()) + str : str);
	}

	@Deprecated
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

	@Deprecated
	public static int[] castInt(Object[] arr) {
		return Arrays.stream(arr).mapToInt(s -> (int) s).toArray();
	}

	@Deprecated
	public static int[] castInt(Integer[] arr) {
		return Arrays.stream(arr).mapToInt(Integer::valueOf).toArray();
	}

	@Deprecated
	public static Object[] toObjectArray(int[] data) {
		return Arrays.stream(data).mapToObj(Integer::valueOf).toArray();
	}

	@Deprecated
	public static ByteBuffer intArrayToByteBuffer(int[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length * Integer.BYTES);
		for (int i = 0; i < data.length; i++) {
			buffer.putInt(data[i]);
		}
		return (ByteBuffer) buffer.flip();
	}

	@Deprecated
	public static IntStream intToBytes(int value) {
		return IntStream.of((value >> 24) & 0xFF, (value >> 16) & 0xFF, (value >> 8) & 0xFF, value & 0xFF);
	}

	@Deprecated
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

	@Deprecated
	public static int[] randomIntArray(int length, int min, int max) {
		Random rand = new Random();
		int[] arr = new int[length];
		for (int i = 0; i < length; i++) {
			arr[i] = rand.nextInt(max - min) + min;
		}
		return arr;
	}

	public static NextTask[] castNextTask(Object[] array) {
		NextTask[] nt = new NextTask[array.length];

		for (int i = 0; i < array.length; i++) {
			nt[i] = (NextTask) array[i];
		}

		return nt;
	}

	public static Vector2f clamp(Vector2f vec) {
		return vec.set(PCUtils.clamp(0, 1, vec.x), PCUtils.clamp(0, 1, vec.y));
	}

	public static Vector3f clampPositive(Vector3f vec) {
		return vec.set(PCUtils.clampGreaterOrEquals(0, vec.x), PCUtils.clampGreaterOrEquals(0, vec.y), PCUtils.clampGreaterOrEquals(0, vec.z));
	}

	public static Vector2f normalizeGreater(Vector2f vec) {
		if(vec.lengthSquared() > 1) {
			return vec.normalize();
		}
		return vec;
	}

}