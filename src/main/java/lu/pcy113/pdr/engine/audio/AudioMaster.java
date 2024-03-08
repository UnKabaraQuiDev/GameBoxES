package lu.pcy113.pdr.engine.audio;

import static lu.pcy113.pdr.engine.utils.bake.openal.IOUtil.ioResourceToByteBuffer;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_STOPPED;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.PDRLoggerUtils;
import lu.pcy113.pdr.engine.utils.PDRUtils;

public class AudioMaster implements Cleanupable {

	// https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/openal/ALCDemo.java

	private boolean useTLC = false;
	private long device, alContext;
	private ALCCapabilities deviceCapabilities;;
	private ALCapabilities capabilities;

	public AudioMaster() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == MemoryUtil.NULL) {
			throw new RuntimeException("Could not open ALC device");
		}

		deviceCapabilities = ALC.createCapabilities(device);
		PDRLoggerUtils.log(deviceCapabilities);

		if (!deviceCapabilities.OpenALC10) {
			throw new RuntimeException("Could not open OpenAL device: OpenALC10 not supported");
		}

		GlobalLogger.info("Audio Output Devices: " + getDevices());
		GlobalLogger.info("Default Audio Output Device: " + getDefaultDevice());
		GlobalLogger.info("Audio Output Device: " + getDevice());

		alContext = ALC10.alcCreateContext(device, (IntBuffer) null);
		PDRUtils.checkAlcError(device);

		useTLC = deviceCapabilities.ALC_EXT_thread_local_context && alcSetThreadContext(alContext);
		if (!useTLC) {
			if (!ALC11.alcMakeContextCurrent(alContext)) {
				throw new RuntimeException("Coult not set context as thread context.");
			}
		}
		PDRUtils.checkAlcError(device);

		capabilities = AL.createCapabilities(deviceCapabilities, MemoryUtil::memCallocPointer);

		GlobalLogger.info("ALC_FREQUENCY: " + alcGetInteger(device, ALC11.ALC_FREQUENCY)+"Hz");
		GlobalLogger.info("ALC_REFRESH: " + alcGetInteger(device, ALC11.ALC_REFRESH)+"Hz");
		GlobalLogger.info("ALC_SYNC: " + alcGetBool(device, ALC11.ALC_SYNC));
		GlobalLogger.info("ALC_MONO_SOURCES: " + alcGetInteger(device, ALC11.ALC_MONO_SOURCES));
		GlobalLogger.info("ALC_STEREO_SOURCES: " + alcGetInteger(device, ALC11.ALC_STEREO_SOURCES));
		
		System.out.println("Thread: "+Thread.currentThread().getName()+" al cap: "+AL.getCapabilities());
		
		//testPlayback();
	}
	
	/*private void testPlayback() {
		// generate buffers and sources
		int buffer = AL10.alGenBuffers();
		PDRUtils.checkAlError();

		int source = AL10.alGenSources();
		PDRUtils.checkAlError();

		try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = readVorbis("./resources/audio/subnautica_bz_stranger_pings.ogg", 32 * 1024, info);

			// copy to buffer
			AL10.alBufferData(buffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
			PDRUtils.checkAlError();
		}

		// set up source input
		AL10.alSourcei(source, AL_BUFFER, buffer);
		PDRUtils.checkAlError();

		// play source
		AL10.alSourcePlay(source);
		PDRUtils.checkAlError();

		// wait
		System.out.println("Waiting for sound to complete...");
		/*while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignored) {
				break;
			}
			if (AL10.alGetSourcei(source, AL_SOURCE_STATE) == AL_STOPPED) {
				break;
			}
			System.out.print(".");
		}

		// stop source 0
		AL10.alSourceStop(source);
		PDRUtils.checkAlError();

		// delete buffers and sources
		AL10.alDeleteSources(source);
		PDRUtils.checkAlError();

		AL10.alDeleteBuffers(buffer);
		PDRUtils.checkAlError();*/
	//}
	
	private boolean alcGetBool(long device, int param) {
		return ALC11.alcGetInteger(device, param) == ALC11.ALC_TRUE;
	}

	private int alcGetInteger(long device, int param) {
		return ALC11.alcGetInteger(device, param);
	}

	public String alcGetString(int param, String str) {
		return alcNullError(ALC11.alcGetString(MemoryUtil.NULL, param), str);
	}

	public String alcGetString(long addr, int param, String str) {
		return alcNullError(ALC11.alcGetString(addr, param), str);
	}

	public String getDefaultDevice() {
		return alcNullError(alcGetString(ALC11.ALC_DEFAULT_DEVICE_SPECIFIER, "Could not get default device."), "Default device is null");
	}

	public String getDevice() {
		return alcGetString(ALC11.ALC_DEVICE_SPECIFIER, "Could not get device.");
	}

	public List<String> getDevices() {
		return alcNullError(ALUtil.getStringList(MemoryUtil.NULL, ALC11.ALC_ALL_DEVICES_SPECIFIER), "Could not get devices.");
	}

	private <T> T alcNullError(T obj, String string) {
		alcError(obj == null, string);
		return obj;
	}

	private void alcError(boolean b, String string) {
		if (b) {
			throw new RuntimeException(string);
		}
	}

	@Override
	public void cleanup() {
		ALC11.alcMakeContextCurrent(MemoryUtil.NULL);
		if (useTLC) {
			AL.setCurrentThread(null);
		} else {
			AL.setCurrentProcess(null);
		}
		MemoryUtil.memFree(capabilities.getAddressBuffer());
		ALC10.alcDestroyContext(alContext);
		ALC10.alcCloseDevice(device);
	}

}
