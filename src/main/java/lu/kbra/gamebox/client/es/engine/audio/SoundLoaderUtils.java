package lu.kbra.gamebox.client.es.engine.audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.utils.mem.buffer.MemBuffer;
import lu.kbra.gamebox.client.es.engine.utils.mem.buffer.MemBufferOrigin;
import lu.pcy113.pclib.datastructure.triplet.Triplet;

public final class SoundLoaderUtils {

	public static Triplet<MemBuffer<ShortBuffer>, Integer, Integer> readVorbis(String file) {
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
		
		MemBuffer<ShortBuffer> mb = new MemBuffer<ShortBuffer>(vorbis, MemBufferOrigin.STBV);

		return new Triplet<MemBuffer<ShortBuffer>, Integer, Integer>(mb, ch, sr);
	}

}
