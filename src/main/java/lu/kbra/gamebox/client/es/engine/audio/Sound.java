package lu.kbra.gamebox.client.es.engine.audio;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.mem.buffer.MemBuffer;
import lu.kbra.gamebox.client.es.engine.utils.mem.buffer.MemBufferOrigin;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.logger.GlobalLogger;

public class Sound implements UniqueID, Cleanupable {

	private final String name;

	private ALBuffer buffer;

	public Sound(String name, String file, boolean stereo) {
		this.name = name;

		switch (PCUtils.getFileExtension(file)) {
		case "ogg":
			loadVorbis(file, stereo);
			break;
		default:
			throw new RuntimeException("Unsupported file type: " + PCUtils.getFileExtension(file));
		}
	}

	public Sound(String name, int sampleRate, int channels, MemBuffer<ShortBuffer> mb, boolean stereo) {
		this.name = name;

		boolean stereoBuffer = channels == 2;
		boolean monoBuffer = channels == 1;

		MemBuffer<ShortBuffer> sb = mb;

		if (!stereo && !monoBuffer) {
			System.err.println("converting");
			sb = new MemBuffer<ShortBuffer>(bufferToMonoAvg(mb.getBuffer(), channels), MemBufferOrigin.OPENAL);
			mb.cleanup();
			stereoBuffer = false;
		}

		buffer = new ALBuffer().gen();

		buffer.setData(sb.getBuffer(), stereoBuffer ? AL11.AL_FORMAT_STEREO16 : AL11.AL_FORMAT_MONO16, sampleRate);
	}

	private void loadVorbis(String file, boolean stereo) {
		buffer = new ALBuffer().gen();

		// buffer, channels, sampleRate
		Triplet<MemBuffer<ShortBuffer>, Integer, Integer> vorbis_channels_sampleRate = SoundLoaderUtils.readVorbis(file);

		boolean stereoBuffer = vorbis_channels_sampleRate.getSecond() == 2;
		boolean monoBuffer = vorbis_channels_sampleRate.getSecond() == 1;

		if (!stereo && !monoBuffer) {
			MemBuffer<ShortBuffer> preBuffer = vorbis_channels_sampleRate.getFirst();
			vorbis_channels_sampleRate.setFirst(new MemBuffer<ShortBuffer>(bufferToMonoAvg(preBuffer.getBuffer(), vorbis_channels_sampleRate.getSecond()), MemBufferOrigin.OPENAL));
			preBuffer.cleanup();
			stereoBuffer = false;
		}

		// copy to buffer
		buffer.setData(vorbis_channels_sampleRate.getFirst().getBuffer(), stereoBuffer ? AL11.AL_FORMAT_STEREO16 : AL11.AL_FORMAT_MONO16, vorbis_channels_sampleRate.getThird());

		// free mem
		vorbis_channels_sampleRate.getFirst().cleanup();
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
		GlobalLogger.log("Cleaning up: "+name);
		
		if(buffer == null)
			return;
		
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
