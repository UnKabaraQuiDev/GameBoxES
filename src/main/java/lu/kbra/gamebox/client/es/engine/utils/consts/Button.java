package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public enum Button {

	NORTH(new Vector2f(0, 1), 0, GLFW.GLFW_GAMEPAD_BUTTON_X),
	EAST(new Vector2f(1, 0), 1, GLFW.GLFW_GAMEPAD_BUTTON_A),
	SOUTH(new Vector2f(0, -1), 2, GLFW.GLFW_GAMEPAD_BUTTON_B),
	WEST(new Vector2f(-1, 0), 3, GLFW.GLFW_GAMEPAD_BUTTON_Y),
	NONE(new Vector2f(0, 0), -1, -1);

	private Vector2f dir;
	private int index, glfwIndex;

	Button(Vector2f v, int index, int glfwIndex) {
		this.dir = v;
		this.index = index;
		this.glfwIndex = glfwIndex;
	}

	public Vector2f getDirection() {
		return dir;
	}

	public int getIndex() {
		return index;
	}

	public int getGLFWIndex() {
		return glfwIndex;
	}

	public static Button getByIndex(int i) {
		for (Button d : values()) {
			if (d.getIndex() == i)
				return d;
		}
		return NONE;
	}

	public static Button getByGLFWIndex(int i) {
		for (Button d : values()) {
			if (d.getGLFWIndex() == i)
				return d;
		}
		return NONE;
	}

}
