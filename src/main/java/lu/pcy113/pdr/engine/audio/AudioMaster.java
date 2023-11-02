package lu.pcy113.pdr.engine.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import lu.pcy113.pdr.engine.impl.Cleanupable;

public class AudioMaster implements Cleanupable {
	
	public AudioMaster() {
		//ALC.create();
		long device = ALC10.alcOpenDevice((ByteBuffer) null);
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		
		long alContext = ALC10.alcCreateContext(device, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(alContext);
        AL.createCapabilities(deviceCapabilities);
	}
	
	@Override
	public void cleanup() {
		ALC.setCapabilities(null);
		ALC.destroy();
	}
	
}
