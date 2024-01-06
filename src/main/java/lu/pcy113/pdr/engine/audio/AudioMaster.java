package lu.pcy113.pdr.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.PDRLoggerUtils;

public class AudioMaster implements Cleanupable {
	
	// TODO https://github.com/LWJGL/lwjgl3/blob/master/modules/samples/src/test/java/org/lwjgl/demo/openal/ALCDemo.java
	
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
		checkAlcError(device);
		
		if (!(deviceCapabilities.ALC_EXT_thread_local_context && EXTThreadLocalContext.alcSetThreadContext(alContext))) {
			if (!ALC11.alcMakeContextCurrent(alContext)) {
				throw new RuntimeException("Coult not set context as thread context.");
			}
		}
		checkAlcError(device);
		
		capabilities = AL.createCapabilities(deviceCapabilities, MemoryUtil::memCallocPointer);
		
		GlobalLogger.info("ALC_FREQUENCY: " + alcGetInteger(device, ALC11.ALC_FREQUENCY));
		GlobalLogger.info("ALC_REFRESH: " + alcGetInteger(device, ALC11.ALC_REFRESH));
		GlobalLogger.info("ALC_SYNC: " + alcGetBool(device, ALC11.ALC_SYNC));
		GlobalLogger.info("ALC_MONO_SOURCES: " + alcGetInteger(device, ALC11.ALC_MONO_SOURCES));
		GlobalLogger.info("ALC_STEREO_SOURCES: " + alcGetInteger(device, ALC11.ALC_STEREO_SOURCES));
	}
	
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
		return alcGetString(ALC11.ALC_DEFAULT_DEVICE_SPECIFIER, "Could not get default device.");
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
	
	public boolean checkAlError() {
		return AL11.alGetError() != AL11.AL_NO_ERROR;
	}
	
	public boolean checkAlcError(long device) {
		return ALC11.alcGetError(device) != AL11.AL_NO_ERROR;
	}
	
	@Override
	public void cleanup() {
		ALC11.alcMakeContextCurrent(MemoryUtil.NULL);
		AL.setCurrentProcess(null);
		AL.setCurrentThread(null);
		MemoryUtil.memFree(capabilities.getAddressBuffer());
		ALC10.alcDestroyContext(alContext);
		ALC10.alcCloseDevice(device);
	}
	
}
