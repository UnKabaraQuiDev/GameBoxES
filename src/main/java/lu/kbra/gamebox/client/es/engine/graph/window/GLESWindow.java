package lu.kbra.gamebox.client.es.engine.graph.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.GLESCapabilities;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.utils.consts.GLType;

public class GLESWindow extends Window {

	private GLESCapabilities capabilities;

	public GLESWindow(WindowOptions options) {
		super(GLType.GLES, options);
		GlobalLogger.log();
	}

	@Override
	protected void init() {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		errorCallback.set();

		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW");

		monitor = getQualifiedMonitor();

		handle = GLFW.glfwCreateWindow(options.windowSize.x, options.windowSize.y, options.title, MemoryUtil.NULL, MemoryUtil.NULL);

		GLFW.glfwMakeContextCurrent(handle);

		if ((this.capabilities = GLES.createCapabilities()) == null)
			throw new RuntimeException("Failed to create OpenGLES context");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_ES_API);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		if (options.windowMultisample > 1) {
			GLES20.glEnable(GL40.GL_MULTISAMPLE);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, options.windowMultisample);
		}
		GLES20.glEnable(GL40.GL_DEPTH_TEST);

		updateOptions();

		createCallbacks();

		GLFW.glfwShowWindow(handle);
	}

	public void takeGLContext() {
		// throw new GLESRuntimeException("Cannot set GLES context");
		GLFW.glfwMakeContextCurrent(handle);
		GLES.setCapabilities(this.capabilities);
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + getClass().getName() + " (" + handle + ")");

		if (GLES.getCapabilities() != null) {
			GLES.setCapabilities(null);
		}
	}

	@Override
	public void cleanupGLFW() {
		GlobalLogger.log("Cleaning up: " + getClass().getName() + " (" + handle + ")");
		
		if (handle != -1) {
			Callbacks.glfwFreeCallbacks(handle);
			GLFW.glfwDestroyWindow(handle);
			GLFW.glfwTerminate();
			handle = -1;
		}
	}

}
