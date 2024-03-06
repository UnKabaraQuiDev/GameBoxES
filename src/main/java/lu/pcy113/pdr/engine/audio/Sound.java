package lu.pcy113.pdr.engine.audio;

import java.nio.ByteBuffer;

import org.lwjgl.openal.AL10;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Sound implements UniqueID, Cleanupable {

	private final String name;
	private final int sbo;

	public Sound(String name, ByteBuffer buffer, int format, int sampleRate) {
		this.name = name;

		sbo = createBuffer(
				buffer,
				format,
				sampleRate);
	}

	private int createBuffer(ByteBuffer buffer, int format, int sampleRate) {
		int sbo = AL10.alGenBuffers();
		AL10.alBufferData(
				sbo,
				format,
				buffer,
				sampleRate);
		return sbo;
	}

	@Override
	public void cleanup() {
		AL10.alDeleteBuffers(
				sbo);
	}

	@Override
	public String getId() {
		return null;
	}

}
