package lu.pcy113.pdr.engine;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import lombok.Getter;
import lombok.Setter;

public class Window {
	
	@Getter
	private final long windowHandle;
	
	@Getter @Setter
	private WindowOptions options;
	
	private Vector4f bgColor = new Vector4f(0, 0.5f, 0, 1);;
	
	public Window(WindowOptions opts) {
		this.options = opts;
		
		GLFW.glfwInit();
		windowHandle = GLFW.glfwCreateWindow(options.width, options.height, options.title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		
		GL30.glEnable(GL30.GL_DEPTH_TEST);
	}
	
	public void prepare() {
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		GL30.glClearColor(bgColor.x, bgColor.y, bgColor.z, bgColor.w);
	}
	public void update() {
		
	}
	
}
