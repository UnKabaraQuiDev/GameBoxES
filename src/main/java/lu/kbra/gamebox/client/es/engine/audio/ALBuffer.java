package lu.kbra.gamebox.client.es.engine.audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;

public class ALBuffer implements Cleanupable {

	private int bufferId = -1;

	public ALBuffer gen() {
		if (bufferId == -1) {
			bufferId = AL11.alGenBuffers();
			PDRUtils.checkAlError("GenBuffers()");
		}
		return this;
	}

	public int getBufferId() {
		return bufferId;
	}

	public void setData(short[] data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", short[" + data.length + "])");
	}

	public void setData(int[] data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", int[" + data.length + "])");
	}

	public void setData(float[] data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", float[" + data.length + "])");
	}

	public void setData(ShortBuffer data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", short[" + data.capacity() + "])");
	}

	public void setData(IntBuffer data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", int[" + data.capacity() + "])");
	}

	public void setData(FloatBuffer data, int format, int sampleRate) {
		AL11.alBufferData(bufferId, format, data, sampleRate);
		PDRUtils.checkAlError("BufferData(" + bufferId + ", " + format + ", " + sampleRate + ", float[" + data.capacity() + "])");
	}

	@Override
	public void cleanup() {
		AL11.alDeleteBuffers(bufferId);
	}

}
