package lu.kbra.gamebox.client.es.game.game.utils;

import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pclib.Pair;
import lu.pcy113.pclib.Pairs;

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

		direction = Direction.getGLFW(dir, leftX, leftY);
	}

	public Pair<Direction, Float> getSoftDirection(GLFWGamepadState gps) {
		FloatBuffer fb = gps.axes();
		float[] jsaxis = new float[fb.remaining() - 1];
		fb.get(jsaxis).clear();
		int dir = MathUtils.greatestAbsIndex(jsaxis);

		float leftX = GlobalUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X]);
		float leftY = GlobalUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y]);

		Direction ddir = Direction.getGLFW(dir, leftX, leftY);

		leftX = org.joml.Math.clamp(0, 1, MathUtils.map(Math.abs(leftX), 0, highThreshold, 0, 1));
		leftY = org.joml.Math.clamp(0, 1, MathUtils.map(Math.abs(leftY), 0, highThreshold, 0, 1));

		return Pairs.pair(ddir, Math.max(leftX, leftY));
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
