package lu.pcy113.pdr.engine.audio;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.libc.LibCStdlib;

import lu.pcy113.pclib.Triplet;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.file.FileUtils;

public class Sound implements UniqueID, Cleanupable {

	private final String name;

	private ALBuffer buffer;

	public Sound(String name, String file, boolean stereo) {
		this.name = name;

		switch (FileUtils.getExtension(file)) {
		case "ogg":
			loadVorbis(file, stereo);
			break;
		default:
			throw new RuntimeException("Unsupported file type: " + FileUtils.getExtension(file));
		}
	}

	private void loadVorbis(String file, boolean stereo) {
		buffer = new ALBuffer().gen();

		// buffer, channels, sampleRate
		Triplet<ShortBuffer, Integer, Integer> vorbis_channels_sampleRate = SoundLoaderUtils.readVorbis(file);

		boolean stereoBuffer = vorbis_channels_sampleRate.getSecond() == 2;
		boolean monoBuffer = vorbis_channels_sampleRate.getSecond() == 1;
		
		if (!stereo && !monoBuffer) {
			ShortBuffer preBuffer = vorbis_channels_sampleRate.getFirst();
			vorbis_channels_sampleRate.setFirst(bufferToMonoAvg(preBuffer, vorbis_channels_sampleRate.getSecond()));
			LibCStdlib.free(preBuffer);
			stereoBuffer = false;
		}

		// copy to buffer
		buffer.setData(vorbis_channels_sampleRate.getFirst(), stereoBuffer ? AL11.AL_FORMAT_STEREO16 : AL11.AL_FORMAT_MONO16, vorbis_channels_sampleRate.getThird());

		// free mem
		if (!stereo && !monoBuffer) {
			MemoryUtil.memFree(vorbis_channels_sampleRate.getFirst());
		}else {
			LibCStdlib.free(vorbis_channels_sampleRate.getFirst());
		}
		// vorbis_channels_sampleRate.getFirst().clear();
		// vorbis_channels_sampleRate = null;

		// set up source input
		// well actually, no.
	}

	private ShortBuffer bufferToMonoAvg(ShortBuffer preBuffer, int channels) {
		int length = preBuffer.capacity();
		ShortBuffer sb = MemoryUtil.memAllocShort(length / channels); // Adjusting capacity based on channels
		for (int i = 0; i < length / channels; i++) {
			int sum = 0;
			// Calculate the sum of samples from all channels
			for (int j = 0; j < channels; j++) {
				sum += preBuffer.get() / channels; // Dividing by channels to avoid overflow
			}
			sb.put((short) sum); // Put the average sample value into the output buffer
		}
		preBuffer.rewind(); // Reset position of input buffer
		sb.flip();
		return sb;
	}

	public ALBuffer getBuffer() {
		return buffer;
	}

	@Override
	public void cleanup() {
		buffer.cleanup();
		buffer = null;
	}

	@Override
	public String getId() {
		return name;
	}

	public boolean hasBuffer() {
		return buffer != null;
	}

}
