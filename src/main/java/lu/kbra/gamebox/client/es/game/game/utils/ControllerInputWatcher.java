package lu.kbra.gamebox.client.es.game.game.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import lu.pcy113.pclib.Pair;
import lu.pcy113.pclib.Pairs;

import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;

public class ControllerInputWatcher {

	private static final int TIME_DIRECTION = 500;
	private Direction direction = Direction.NONE;
	private boolean waitingForNoneDirection = true;

	private float highThreshold = 0.8f;

	private Button button = Button.NONE;
	private boolean waitingForNoneButton = true;
	private long lastDirection;

	public void updateButton(GLFWGamepadState gps) {
		ByteBuffer fb = gps.buttons();
		byte[] jsaxis = new byte[fb.remaining() - 1];
		fb.get(jsaxis).clear();

		byte xBtn = jsaxis[GLFW.GLFW_GAMEPAD_BUTTON_X];
		byte aBtn = jsaxis[GLFW.GLFW_GAMEPAD_BUTTON_A];
		byte bBtn = jsaxis[GLFW.GLFW_GAMEPAD_BUTTON_B];
		byte yBtn = jsaxis[GLFW.GLFW_GAMEPAD_BUTTON_Y];

		jsaxis = new byte[] { xBtn, aBtn, bBtn, yBtn };
		int dir = MathUtils.greatestAbsIndex(jsaxis);

		if (waitingForNoneButton && !(xBtn == 0 && aBtn == 0 && bBtn == 0 && yBtn == 0)) {
			return;
		} else {
			waitingForNoneButton = false;
			// continue;
		}

		button = Button.getByIndex(dir);
	}

	public void updateDirection(GLFWGamepadState gps) {
		FloatBuffer fb = gps.axes();
		float[] jsaxis = new float[fb.remaining() - 1];
		fb.get(jsaxis).clear();
		jsaxis = new float[] { jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X], jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y] };
		int dir = MathUtils.greatestAbsIndex(jsaxis);

		float leftX = PDRUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X], highThreshold);
		float leftY = PDRUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y], highThreshold);

		if(System.currentTimeMillis() - lastDirection > TIME_DIRECTION) {
			waitingForNoneDirection = false;
			lastDirection = System.currentTimeMillis();
		}else if (waitingForNoneDirection && !(leftX == 0 && leftY == 0)) {
			return;
		} else {
			waitingForNoneDirection = false;
			// continue;
		}

		direction = Direction.getGLFW(dir, leftX, leftY);
	}

	public Pair<Direction, Float> getSoftDirection(GLFWGamepadState gps) {
		FloatBuffer fb = gps.axes();
		float[] jsaxis = new float[fb.remaining() - 1];
		fb.get(jsaxis).clear();
		jsaxis = new float[] { jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X], jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y] };
		int dir = MathUtils.greatestAbsIndex(jsaxis);

		float leftX = GlobalUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_X]);
		float leftY = GlobalUtils.applyMinThreshold(jsaxis[GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y]);

		Direction ddir = Direction.getGLFW(dir, leftX, leftY);

		leftX = org.joml.Math.clamp(0, 1, MathUtils.map(Math.abs(leftX), 0, highThreshold, 0, 1));
		leftY = org.joml.Math.clamp(0, 1, MathUtils.map(Math.abs(leftY), 0, highThreshold, 0, 1));

		return Pairs.pair(ddir, Math.max(leftX, leftY));
	}

	public boolean hasNextDirection() {
		return direction != Direction.NONE;
	}

	public Direction consumeDirection() {
		waitingForNoneDirection = true;
		Direction dir = direction;
		direction = Direction.NONE;
		return dir;
	}

	public Direction getDirection() {
		return direction;
	}

	public Button getButton() {
		return button;
	}

	public boolean hasNextButton() {
		return button != Button.NONE;
	}

	public Button consumeButton() {
		waitingForNoneButton = true;
		Button dir = button;
		button = Button.NONE;
		return dir;
	}

}
