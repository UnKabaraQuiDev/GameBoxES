package lu.pcy113.pdr.engine.graph.window;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWJoystickCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Window implements Cleanupable {
	
	private WindowOptions options;
	
	private final long monitor;
	private final long handle;
	
	private GLFWGamepadState gamepadState;
	
	private Vector4f background = new Vector4f(0, 0.1f, 0, 1);
	
	private BiConsumer<Integer, Integer> onResize;
	
	private GLFWKeyCallback keyCallback;
	private GLFWJoystickCallback joystickCallback;
	private GLFWFramebufferSizeCallback frameBufferCallback;
	
	private int width, height;
	
	public Window(WindowOptions options) {
		Logger.log();
		
		this.options = options;
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW");
			
		monitor = GLFW.glfwGetPrimaryMonitor();
		if(monitor == MemoryUtil.NULL)
			throw new RuntimeException("No primary monitor found");
		
		handle = GLFW.glfwCreateWindow(options.windowSize.x, options.windowSize.y, options.title, MemoryUtil.NULL, MemoryUtil.NULL);
		
		GLFW.glfwMakeContextCurrent(handle);
		GL.createCapabilities();
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
		
		updateOptions();
		
		keyCallback = GLFWKeyCallback.create((window, key, scancode, action, mods) -> callback_key(window, key, scancode, action, mods));
		GLFW.glfwSetKeyCallback(handle, keyCallback);
		joystickCallback = GLFWJoystickCallback.create((jid, event) -> callback_joystick(jid, event));
		GLFW.glfwSetJoystickCallback(joystickCallback);
		frameBufferCallback = GLFWFramebufferSizeCallback.create((window, width, height) -> callback_frameBuffer(window, width, height));
		GLFW.glfwSetFramebufferSizeCallback(handle, frameBufferCallback);
		
		gamepadState = GLFWGamepadState.create();
		
		GLFW.glfwShowWindow(handle);
	}
	
	private void callback_frameBuffer(long window, int w, int h) {
		if(!options.fullscreen)
			options.windowSize.set(w, h);
		if(onResize != null)
			onResize.accept(w, h);
		GL40.glViewport(0, 0, w, h);
		this.width = w;
		this.height = h;
	}
	private void callback_joystick(int jid, int event) {
		if(event == GLFW.GLFW_CONNECTED) {
			Logger.log(Level.INFO, "Joystick connected: jid:"+jid+", name:"+GLFW.glfwGetJoystickName(jid)+", guid:"+GLFW.glfwGetJoystickGUID(jid)+" -> name:"+GLFW.glfwGetGamepadName(jid)+", joystick as gamepad:"+GLFW.glfwJoystickIsGamepad(jid));
		}else if(event == GLFW.GLFW_DISCONNECTED) {
			Logger.log(Level.INFO, "Joystick disconnected: jid:"+jid);
		}
	}
	private void callback_key(long window, int key, int scancode, int action, int mods) {
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
			GLFW.glfwSetWindowShouldClose(window, true);
		}else if(key == GLFW.GLFW_KEY_F11 && action == GLFW.GLFW_PRESS) {
			options.fullscreen = !options.fullscreen;
			updateOptions();
		}
	}
	
	public boolean isJoystickPresent() {
		return GLFW.glfwJoystickPresent(GLFW.GLFW_JOYSTICK_1);
	}
	public float getJoystickAxis(int jid, int axis) {
		FloatBuffer fb = GLFW.glfwGetJoystickAxes(jid);
		float[] bb = new float[fb.remaining()-1];
		fb.get(bb);
		return bb[axis];
	}
	public boolean getJoystickButton(int jid, int btn) {
		ByteBuffer fb = GLFW.glfwGetJoystickButtons(jid);
		byte[] bb = new byte[fb.remaining()-1];
		fb.get(bb);
		return bb[btn] == GLFW.GLFW_PRESS;
	}
	public ByteBuffer getJoystickButtons(int jid) {
		return GLFW.glfwGetJoystickButtons(jid);
	}
	public byte getJoystickHat(int jid, int hat) {
		ByteBuffer fb = GLFW.glfwGetJoystickButtons(jid);
		byte[] bb = new byte[fb.remaining()-1];
		fb.get(bb);
		return bb[hat];
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
		GLFW.glfwSetWindowMonitor(
				handle,
				options.fullscreen ? monitor : MemoryUtil.NULL,
				0, 0,
				!options.fullscreen ? options.windowSize.x : vidMode.width(), !options.fullscreen ? options.windowSize.y : vidMode.height(),
				options.fps);
		this.width = !options.fullscreen ? options.windowSize.x : vidMode.width();
		this.height = !options.fullscreen ? options.windowSize.y : vidMode.height();
	}
	
	public Vector4f getBackground() {return background;}
	public void setBackground(Vector4f background) {this.background = background;}
	public WindowOptions getOptions() {return options;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public void onResize(BiConsumer<Integer, Integer> object) {this.onResize = object;}

	public void runCallbacks() {
		if(onResize != null) {
			int[] w = new int[1];
			int[] h = new int[1];
			GLFW.glfwGetWindowSize(handle, w, h);
			onResize.accept(w[0], h[0]);
		}
	}
	
	@Override
	public void cleanup() {
		if(keyCallback != null) {
			keyCallback.free();
			keyCallback = null;
		}
		if(joystickCallback != null) {
			joystickCallback.free();
			joystickCallback = null;
		}
		if(frameBufferCallback != null) {
			frameBufferCallback.free();
			frameBufferCallback = null;
		}
	}
	
}
