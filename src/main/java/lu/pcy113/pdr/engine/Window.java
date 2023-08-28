package lu.pcy113.pdr.engine;

import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Window implements Cleanupable {
	
	private final long windowHandle;
	
	private String title;
	private int width, height;
	private Callable<Void> resizeCallback;
	
	public Window(String title, WindowOptions options, Callable<Void> resize) {
		this.resizeCallback = resize;
		this.title = title;
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		
		if(options.compatibleProfile) {
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
		}else {
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		}
		
		if(options.width > 0 && options.height > 0) {
			this.width = options.width;
			this.height = options.height;
		} else {
			GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			this.width = vidMode.width();
			this.height = vidMode.height();
		}
		
		windowHandle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(windowHandle == MemoryUtil.NULL) {
			throw new RuntimeException("Could not create GLFW window.");
		}
		
		GLFW.glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) -> resized(w, h));
		
		GLFW.glfwSetErrorCallback((int errCode, long msgPtr) -> Logger.log(Level.WARNING, "GLFW["+errCode+"]: "+MemoryUtil.memUTF8(msgPtr)));
		
		GLFW.glfwSetKeyCallback(windowHandle, (window, key, scan, action, mods) -> key(key, scan, action, mods));
		
		GLFW.glfwMakeContextCurrent(windowHandle);
		
		if(options.fps > 0) {
			GLFW.glfwSwapInterval(0);
		}else {
			GLFW.glfwSwapInterval(1);
		}
		
		GLFW.glfwShowWindow(windowHandle);
		
		int[] arrWidth = new int[1];
		int[] arrHeight = new int[1];
		GLFW.glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
		this.width = arrWidth[0];
		this.height = arrHeight[0];
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		Callbacks.glfwFreeCallbacks(windowHandle);
		GLFW.glfwDestroyWindow(windowHandle);
		GLFW.glfwTerminate();
		
		GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
		if(callback != null) {
			callback.free();
		}
	}
	
	public void update() {
		Logger.log();
		
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
	public long getWindowHandle() {return windowHandle;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width;}
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height;}
	
	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(windowHandle);
	}
	
	public boolean isKeyPressed(int key) {
		//Logger.log(GLFW.glfwGetKey(windowHandle, key)+" for key "+key);
		return GLFW.glfwGetKey(windowHandle, key) == GLFW.GLFW_PRESS;
	}
	
	public void pollEvents() {
		GLFW.glfwPollEvents();
	}
	
	private void key(int key, int scan, int action, int mods) {
		//Logger.log(GLFW.glfwGetKey(windowHandle, key)+" callback key "+key);
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
			GLFW.glfwSetWindowShouldClose(windowHandle, true);
		}
	}
	private void resized(int w, int h) {
		this.width = w;
		this.height = h;
		try {
			resizeCallback.call();
		}catch(Exception e) {
			Logger.log(Level.WARNING, "Error calling resiz callback");
		}
	}

}
