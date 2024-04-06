package lu.kbra.gamebox.client.es.game.game.utils;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;

public class ControllerInputWatcher {

	private Direction direction = Direction.NONE;
	private boolean waitingForNone = true;

	private float highThreshold = 0.35f;

	public void update(GLFWGamepadState gps) {
		FloatBuffer fb = gps.axes();
		float[] jsaxis = new float[fb.remaining() - 1];
		fb.get(jsaxis).clear();
		int dir = MathUtils.greatestAbsIndex(jsaxis);
		
		float leftX = PDRUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X], highThreshold);
		float leftY = PDRUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y], highThreshold);

		if (waitingForNone && !(leftX == 0 && leftY == 0)) {
			return;
		} else {
			waitingForNone = false;
			// continue;
		}

		switch (dir) {
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_X:
			if (Math.signum(leftX) == -1) {
				direction = Direction.WEST;
			} else if (Math.signum(leftX) == 1) {
				direction = Direction.EST;
			}
			break;
		case GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y:
			if (Math.signum(leftY) == -1) {
				direction = Direction.NORTH;
			} else if (Math.signum(leftY) == 1) {
				direction = Direction.SOUTH;
			}
			break;
		}

		/*
		 * if (!direction.equals(previous)) { waitingForNone = false; }
		 */
	}

	public boolean hasNext() {
		return direction != Direction.NONE;
	}

	public Direction consume() {
		waitingForNone = true;
		Direction dir = direction;
		direction = Direction.NONE;
		return dir;
	}

	public Direction get() {
		return direction;
	}

}
