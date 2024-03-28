package lu.kbra.gamebox.client.es.engine.graph.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.utils.consts.GLType;

public class GLWindow extends Window {

	private GLCapabilities capabilities;

	public GLWindow(WindowOptions options) {
		super(GLType.GL, options);
		GlobalLogger.log();
	}

	protected void init() {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW");

		monitor = getQualifiedMonitor();
		
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

		createCallbacks();

		GLFW.glfwShowWindow(handle);
	}

	@Override
	public void takeGLContext() {
		GLFW.glfwMakeContextCurrent(handle);
		GL.setCapabilities(this.capabilities);
	}
	
	@Override
	public void cleanup() {
		if (handle != -1) {
			Callbacks.glfwFreeCallbacks(handle);
			GLFW.glfwDestroyWindow(handle);
			GLFW.glfwTerminate();
			handle = -1;
		}
		if (GL.getCapabilities() != null) {
			GL.setCapabilities(null);
		}
	}

}
