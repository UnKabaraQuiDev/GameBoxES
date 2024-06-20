package lu.kbra.gamebox.client.es.engine.graph.window;

import java.lang.reflect.Field;
import java.nio.IntBuffer;

import org.lwjgl.egl.EGL;
import org.lwjgl.egl.EGL15;
import org.lwjgl.egl.EGLCapabilities;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES30;
import org.lwjgl.opengles.GLESCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.exceptions.egl.EGLNoDisplayException;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.GLType;

public class GLESWindow extends Window {

	private EGLCapabilities eglCapabilities;
	private GLESCapabilities capabilities;

	public GLESWindow(WindowOptions options) {
		super(GLType.GLES, options);
		GlobalLogger.log();
	}

	@Override
	protected void init() {
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		// GLFW.glfwSetErrorCallback(errorCallback);

		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_CREATION_API, GLFW.GLFW_EGL_CONTEXT_API);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_ES_API);

		monitor = getQualifiedMonitor();

		handle = GLFW.glfwCreateWindow(options.windowSize.x, options.windowSize.y, options.title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create GLFW Window");

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer major = stack.mallocInt(1);
			IntBuffer minor = stack.mallocInt(1);

			EGL15.eglInitialize(monitor, major, minor);
			PDRUtils.checkEGLError("glInitialize[" + monitor + ", IB, IB]");

			this.eglCapabilities = EGL.createDisplayCapabilities(monitor, major.get(0), minor.get(0));
		}

		if (this.eglCapabilities == null)
			throw new RuntimeException("Failed to create EGL context");

		try {
			System.out.println("EGL Capabilities:");
			for (Field f : EGLCapabilities.class.getFields()) {
				if (f.getType() == boolean.class) {
					if (f.get(this.eglCapabilities).equals(Boolean.TRUE)) {
						System.out.println("\t" + f.getName());
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		GLFW.glfwMakeContextCurrent(handle);

		if ((this.capabilities = GLES.createCapabilities()) == null)
			throw new RuntimeException("Failed to create OpenGL ES context");

		try {
			System.out.println("OpenGL ES Capabilities:");
			for (Field f : GLESCapabilities.class.getFields()) {
				if (f.getType() == boolean.class) {
					if (f.get(this.capabilities).equals(Boolean.TRUE)) {
						System.out.println("\t" + f.getName());
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (options.windowMultisample > 1) {
			GLES30.glEnable(GL40.GL_MULTISAMPLE);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, options.windowMultisample);
		}

		GLES30.glEnable(GL40.GL_DEPTH_TEST);

		updateOptions();

		createCallbacks();

		GLFW.glfwShowWindow(handle);
	}

	@Override
	public void updateOptions() {
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, options.resizable ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwSwapInterval(options.vsync ? 1 : 0);
		/*GLFWVidMode vidMode = GLFW.glfwGetVideoMode(monitor);
		GLFW.glfwSetWindowMonitor(handle, options.fullscreen ? monitor : MemoryUtil.NULL, 0, 0, !options.fullscreen ? options.windowSize.x : vidMode.width(), !options.fullscreen ? options.windowSize.y : vidMode.height(), options.fps);
		this.width = !options.fullscreen ? options.windowSize.x : vidMode.width();
		this.height = !options.fullscreen ? options.windowSize.y : vidMode.height();*/
	}
	
	@Override
	protected long getQualifiedMonitor() {
		long monitor = EGL15.eglGetDisplay(EGL15.EGL_DEFAULT_DISPLAY);
		PDRUtils.checkEGLError("eglGetDisplay[DEFAULT]");
		if(monitor == EGL15.EGL_NO_DISPLAY) {
			throw new EGLNoDisplayException("eglGetDisplay[DEFAULT]");
		}
		return monitor;
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
			GLFW.glfwSetErrorCallback(null).free();
			handle = -1;
		}
	}

}
