package lu.pcy113.pdr.engine.graph.window;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.impl.Cleanupable;

public class Window implements Cleanupable {

	private WindowOptions options;

	private long monitor;
	private long handle;
	private GLCapabilities capabilities;

	private GLFWGamepadState gamepadState;

	private Vector4f background = new Vector4f(0, 0.1f, 0, 1);

	private BiConsumer<Integer, Integer> onResize;

	private GLFWKeyCallback keyCallback;
	private GLFWJoystickCallback joystickCallback;
	private GLFWFramebufferSizeCallback frameBufferCallback;
	private GLFWErrorCallback errorCallback;
	private Vector2d scroll = new Vector2d();
	private GLFWScrollCallback scrollCallback;
	private Vector2f cursorPos = new Vector2f();
	private GLFWCursorPosCallback cursorPosCallback;

	private int width, height;

	public Window(WindowOptions options) {
		GlobalLogger.log();

		this.options = options;

		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW");

		monitor = GLFW.glfwGetPrimaryMonitor();
		if (monitor == MemoryUtil.NULL)
			throw new RuntimeException("No primary monitor found");

		handle = GLFW.glfwCreateWindow(options.windowSize.x, options.windowSize.y, options.title, MemoryUtil.NULL, MemoryUtil.NULL);

		takeGLContext();
		if ((this.capabilities = GL.createCapabilities()) == null)
			throw new RuntimeException("Failed to create OpenGL context");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		if (options.windowMultisample > 1) {
			GL40.glEnable(GL40.GL_MULTISAMPLE);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, options.windowMultisample);
		}
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		updateOptions();

		keyCallback = GLFWKeyCallback.create((window, key, scancode, action, mods) -> callback_key(window, key, scancode, action, mods));
		GLFW.glfwSetKeyCallback(handle, keyCallback);

		joystickCallback = GLFWJoystickCallback.create((jid, event) -> callback_joystick(jid, event));
		GLFW.glfwSetJoystickCallback(joystickCallback);

		frameBufferCallback = GLFWFramebufferSizeCallback.create((window, width, height) -> callback_frameBuffer(window, width, height));
		GLFW.glfwSetFramebufferSizeCallback(handle, frameBufferCallback);

		scrollCallback = GLFWScrollCallback.create((window, sx, sy) -> callback_scroll(window, sx, sy));
		GLFW.glfwSetScrollCallback(handle, scrollCallback);

		cursorPosCallback = GLFWCursorPosCallback.create((window, sx, sy) -> callback_cursor_pos(window, sx, sy));
		GLFW.glfwSetCursorPosCallback(handle, cursorPosCallback);

		gamepadState = GLFWGamepadState.create();

		GLFW.glfwShowWindow(handle);
	}

	public void clearGLContext() {
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
	}

	public void takeGLContext() {
		GLFW.glfwMakeContextCurrent(handle);
		GL.setCapabilities(this.capabilities);
	}

	private void callback_cursor_pos(long window, double sx, double sy) {
		cursorPos.set(sx, sy);
	}

	private void callback_scroll(long window, double sx, double sy) {
		// System.err.println("scroll: " + sx + ", " + sy + " handle" + window);
		if (window != handle)
			return;
		scroll.add(sx, sy);
	}

	private void callback_frameBuffer(long window, int w, int h) {
		if (window != handle)
			return;
		if (!options.fullscreen)
			options.windowSize.set(w, h);
		if (onResize != null)
			onResize.accept(w, h);
		GL40.glViewport(0, 0, w, h);
		this.width = w;
		this.height = h;
	}

	private void callback_joystick(int jid, int event) {
		if (event == GLFW.GLFW_CONNECTED) {
			GlobalLogger.log(Level.INFO,
					"Joystick connected: jid:" + jid + ", name:" + GLFW.glfwGetJoystickName(jid) + ", guid:" + GLFW.glfwGetJoystickGUID(jid) + " -> name:" + GLFW.glfwGetGamepadName(jid) + ", joystick as gamepad:" + GLFW.glfwJoystickIsGamepad(jid));
		} else if (event == GLFW.GLFW_DISCONNECTED) {
			GlobalLogger.log(Level.INFO, "Joystick disconnected: jid:" + jid);
		}
	}

	private void callback_key(long window, int key, int scancode, int action, int mods) {
		if (window != handle)
			return;
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			GLFW.glfwSetWindowShouldClose(window, true);
		} else if (key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) {
			options.fullscreen = !options.fullscreen;
			updateOptions();
		}
	}

	public boolean isJoystickPresent() {
		return GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1);
	}

	public float getJoystickAxis(int jid, int axis) {
		FloatBuffer fb = GLFW.glfwGetJoystickAxes(jid);
		float[] bb = new float[fb.remaining() - 1];
		fb.get(bb);
		return bb[axis];
	}

	public boolean getJoystickButton(int jid, int btn) {
		ByteBuffer fb = GLFW.glfwGetJoystickButtons(jid);
		byte[] bb = new byte[fb.remaining() - 1];
		fb.get(bb);
		return bb[btn] == GLFW.GLFW_PRESS;
	}

	public ByteBuffer getJoystickButtons(int jid) {
		return GLFW.glfwGetJoystickButtons(jid);
	}

	public byte getJoystickHat(int jid, int hat) {
		ByteBuffer fb = GLFW.glfwGetJoystickButtons(jid);
		byte[] bb = new byte[fb.remaining() - 1];
		fb.get(bb);
		return bb[hat];
	}

	public boolean isMouseButtonPressed(int mbid) {
		return GLFW.glfwGetMouseButton(handle, mbid) == GLFW.GLFW_PRESS;
	}

	public Vector2f getMousePos() {
		return cursorPos;
	}

	public boolean getJoystickHat(int jid, int hat, byte state) {
		return getJoystickHat(jid, hat) == state;
	}

	public ByteBuffer getJoystickHats(int jid) {
		return GLFW.glfwGetJoystickHats(jid);
	}

	public GLFWGamepadState getGamepad() {
		return gamepadState;
	}

	public boolean isKeyPressed(int code) {
		return GLFW.glfwGetKey(handle, code) == GLFW.GLFW_PRESS;
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}

	public void clear() {
		GL40.glClear(GL40.GL_COLOR_BUFFER_BIT | GL40.GL_DEPTH_BUFFER_BIT);
		GL40.glClearColor(background.x, background.y, background.z, background.w);
	}

	public void swapBuffers() {
		GLFW.glfwSwapBuffers(handle);
	}

	public void pollEvents() {
		GLFW.glfwPollEvents();
	}

	public boolean updateGamepad(int jid) {
		return GLFW.glfwGetGamepadState(jid, gamepadState);
	}

	public void updateOptions() {
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, options.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwSwapInterval(options.vsync ? 1 : 0);
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
		GLFW.glfwSetWindowMonitor(handle, options.fullscreen ? monitor : MemoryUtil.NULL, 0, 0, !options.fullscreen ? options.windowSize.x : vidMode.width(), !options.fullscreen ? options.windowSize.y : vidMode.height(), options.fps);
		this.width = !options.fullscreen ? options.windowSize.x : vidMode.width();
		this.height = !options.fullscreen ? options.windowSize.y : vidMode.height();
	}

	public Vector4f getBackground() {
		return background;
	}

	public void setBackground(Vector4f background) {
		this.background = background;
	}

	public WindowOptions getOptions() {
		return options;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector2d getScroll() {
		return scroll;
	}

	public void clearScroll() {
		scroll.set(0, 0);
	}

	public void onResize(BiConsumer<Integer, Integer> object) {
		this.onResize = object;
	}

	public void runCallbacks() {
		if (onResize != null) {
			int[] w = new int[1];
			int[] h = new int[1];
			GLFW.glfwGetWindowSize(handle, w, h);
			onResize.accept(w[0], h[0]);
		}
	}

	@Override
	public void cleanup() {
		/*if (keyCallback != null) {
			keyCallback.free();
			keyCallback = null;
		}
		if (joystickCallback != null) {
			joystickCallback.free();
			joystickCallback = null;
		}
		if (frameBufferCallback != null) {
			frameBufferCallback.free();
			frameBufferCallback = null;
		}
		if (errorCallback != null) {
			errorCallback.free();
			errorCallback = null;
		}
		if (scrollCallback != null) {
			scrollCallback.free();
			scrollCallback = null;
		}
		if (cursorPosCallback != null) {
			cursorPosCallback.free();
			cursorPosCallback = null;
		}*/
		if (handle != -1) {
			Callbacks.glfwFreeCallbacks(handle);
			GLFW.glfwDestroyWindow(handle);
			GLFW.glfwTerminate();
			handle = -1;
		}
		if (GL.getCapabilities() != null) {
			GL.setCapabilities(null);
			// MemoryUtil.memFree(capabilities.getAddressBuffer());
		}
	}

}
