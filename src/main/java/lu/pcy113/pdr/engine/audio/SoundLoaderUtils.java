package lu.pcy113.pdr.engine.audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.Triplet;

public final class SoundLoaderUtils {

	public static Triplet<ShortBuffer, Integer, Integer> readVorbis(String file) {
		IntBuffer channels = MemoryUtil.memAllocInt(1);
		IntBuffer sampleRate = MemoryUtil.memAllocInt(1);
		ShortBuffer vorbis = STBVorbis.stb_vorbis_decode_filename(file, channels, sampleRate);

		if (vorbis == null) {
			MemoryUtil.memFree(channels);
			MemoryUtil.memFree(sampleRate);

			throw new RuntimeException("Failed to open Ogg Vorbis file. (" + file + ") ");
		}

		int ch = channels.get();
		int sr = sampleRate.get();

		MemoryUtil.memFree(channels);
		MemoryUtil.memFree(sampleRate);

		return new Triplet<ShortBuffer, Integer, Integer>(vorbis, ch, sr);
	}

}
