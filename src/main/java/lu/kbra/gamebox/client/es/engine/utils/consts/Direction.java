package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public enum Direction {

	NORTH(new Vector2f(0, 1), 0), EAST(new Vector2f(1, 0), 1), SOUTH(new Vector2f(0, -1), 2), WEST(new Vector2f(-1, 0), 3), NONE(new Vector2f(0, 0), -1);

	private Vector2f dir;
	private int index;

	Direction(Vector2f v, int index) {
		this.dir = v;
		this.index = index;
	}

	public Vector2f getDirection() {
		return dir;
	}

	public int getIndex() {
		return index;
	}

	public static Direction getByIndex(int i) {
		for (Direction d : values()) {
			if (d.getIndex() == i)
				return d;
		}
		return NONE;
	}

	public static Direction getGLFW(int dir, float leftX, float leftY) {
		switch (dir) {
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_X:
			if (Math.signum(leftX) == -1) {
				return Direction.WEST;
			} else if (Math.signum(leftX) == 1) {
				return Direction.EAST;
			}
			break;
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y:
			if (Math.signum(leftY) == -1) {
				return Direction.NORTH;
			} else if (Math.signum(leftY) == 1) {
				return Direction.SOUTH;
			}
		}

		return NONE;
	}

	public static Direction getGLFWCross(int dir, byte up, byte right, byte down, byte left) {
		return getByIndex(dir);
	}

}
