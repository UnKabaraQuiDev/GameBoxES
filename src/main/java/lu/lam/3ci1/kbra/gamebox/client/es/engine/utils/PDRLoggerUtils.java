package lu.pcy113.pdr.engine.utils;

import org.lwjgl.openal.ALCCapabilities;

import lu.pcy113.pclib.GlobalLogger;

public final class PDRLoggerUtils {

	private static int count = 0;

	public static void log(ALCCapabilities deviceCapabilities) {
		if (!GlobalLogger.isInit())
			throw new RuntimeException("GloballogCger is not initiated");

		logC("OpenALC10: " + deviceCapabilities.OpenALC10);
		logC("OpenALC11: " + deviceCapabilities.OpenALC11);
		logC("OpenAL_SOFT_loopback_bformat: " + deviceCapabilities.OpenALC_SOFT_loopback_bformat);
		logC("ALC_ENUMERATE_ALL_EXT: " + deviceCapabilities.ALC_ENUMERATE_ALL_EXT);

		logC("ALC_ENUMERATION_EXT: " + deviceCapabilities.ALC_ENUMERATION_EXT);
		logC("ALC_EXT_CAPTURE: " + deviceCapabilities.ALC_EXT_CAPTURE);
		logC("ALC_EXT_DEDICATED: " + deviceCapabilities.ALC_EXT_DEDICATED);
		logC("ALC_EXT_DEFAULT_FILTER_ORDER: " + deviceCapabilities.ALC_EXT_DEFAULT_FILTER_ORDER);
		logC("ALC_EXT_disconnect: " + deviceCapabilities.ALC_EXT_disconnect);
		logC("ALC_EXT_EFX: " + deviceCapabilities.ALC_EXT_EFX);
		logC("ALC_EXT_thread_local_context: " + deviceCapabilities.ALC_EXT_thread_local_context);
		logC("ALC_LOKI_audio_channel: " + deviceCapabilities.ALC_LOKI_audio_channel);
		logC("ALC_SOFT_device_clock: " + deviceCapabilities.ALC_SOFT_device_clock);
		logC("ALC_SOFT_HRTF: " + deviceCapabilities.ALC_SOFT_HRTF);
		logC("ALC_SOFT_loopback: " + deviceCapabilities.ALC_SOFT_loopback);
		logC("ALC_SOFT_output_limiter: " + deviceCapabilities.ALC_SOFT_output_limiter);
		logC("ALC_SOFT_output_mode: " + deviceCapabilities.ALC_SOFT_output_mode);
		logC("ALC_SOFT_pause_device: " + deviceCapabilities.ALC_SOFT_pause_device);
		logC("ALC_SOFT_reopen_device: " + deviceCapabilities.ALC_SOFT_reopen_device);

		logC("alcOpenDevice: " + deviceCapabilities.alcOpenDevice);
		logC("alcCloseDevice: " + deviceCapabilities.alcCloseDevice);
		logC("alcCreateContext: " + deviceCapabilities.alcCreateContext);
		logC("alcMakeContextCurrent: " + deviceCapabilities.alcMakeContextCurrent);
		logC("alcProcessContext: " + deviceCapabilities.alcProcessContext);
		logC("alcSuspendContext: " + deviceCapabilities.alcSuspendContext);
		logC("alcDestroyContext: " + deviceCapabilities.alcDestroyContext);
		logC("alcGetCurrentContext: " + deviceCapabilities.alcGetCurrentContext);
		logC("alcGetContextsDevice: " + deviceCapabilities.alcGetContextsDevice);
		logC("alcIsExtensionPresent: " + deviceCapabilities.alcIsExtensionPresent);
		logC("alcGetProcAddress: " + deviceCapabilities.alcGetProcAddress);
		logC("alcGetEnumValue: " + deviceCapabilities.alcGetEnumValue);
		logC("alcGetError: " + deviceCapabilities.alcGetError);
		logC("alcGetString: " + deviceCapabilities.alcGetString);
		logC("alcGetIntegerv: " + deviceCapabilities.alcGetIntegerv);

		logC("alcCaptureOpenDevice: " + deviceCapabilities.alcCaptureOpenDevice);
		logC("alcCaptureCloseDevice: " + deviceCapabilities.alcCaptureCloseDevice);
		logC("alcCaptureStart: " + deviceCapabilities.alcCaptureStart);
		logC("alcCaptureStop: " + deviceCapabilities.alcCaptureStop);
		logC("alcCaptureSamples: " + deviceCapabilities.alcCaptureSamples);

		logC("alcSetThreadContext: " + deviceCapabilities.alcSetThreadContext);
		logC("alcGetThreadContext: " + deviceCapabilities.alcGetThreadContext);

		logC("alcGetInteger64vSOFT: " + deviceCapabilities.alcGetInteger64vSOFT);

		logC("alcGetStringiSOFT: " + deviceCapabilities.alcGetStringiSOFT);
		logC("alcResetDeviceSOFT: " + deviceCapabilities.alcResetDeviceSOFT);

		logC("alcLoopbackOpenDeviceSOFT: " + deviceCapabilities.alcLoopbackOpenDeviceSOFT);
		logC("alcIsRenderFormatSupportedSOFT: " + deviceCapabilities.alcIsRenderFormatSupportedSOFT);
		logC("alcRenderSamplesSOFT: " + deviceCapabilities.alcRenderSamplesSOFT);

		logC("alcDevicePauseSOFT: " + deviceCapabilities.alcDevicePauseSOFT);
		logC("alcDeviceResumeSOFT: " + deviceCapabilities.alcDeviceResumeSOFT);

		logC("alcReopenDeviceSOFT: " + deviceCapabilities.alcReopenDeviceSOFT);

		count = 0;
	}

	private static void logC(String string) {
		GlobalLogger.log(count++ + ". " + string);
	}

}
