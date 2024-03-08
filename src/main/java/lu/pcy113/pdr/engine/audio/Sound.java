package lu.pcy113.pdr.engine.audio;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbisInfo;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.PDRUtils;

public class Sound implements UniqueID, Cleanupable {

	private final String name;
	private int abo = -1, sbo = -1;
	private boolean looping;

	public Sound(String name, String file, boolean looping) {
		this.name = name;
		this.looping = looping;
		
		switch (FileUtils.getExtension(file)) {
		case "ogg":
			loadVorbis(file, looping);
			break;
		default:
			throw new RuntimeException("Unsupported file type: " + FileUtils.getExtension(file));
		}
	}

	private void loadVorbis(String file, boolean looping) {
		System.out.println("loading: "+file);
		
		System.out.println("Thread: "+Thread.currentThread().getName());
		sbo = AL11.alGenBuffers();
		PDRUtils.checkAlError("GenBuffers");

		abo = AL11.alGenSources();
		PDRUtils.checkAlError("GenSources");
		
		try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = SoundLoaderUtils.readVorbis("./resources/audio/subnautica_bz_stranger_pings.ogg", 32 * 1024, info);

			// copy to buffer
			AL11.alBufferData(abo, info.channels() == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16, pcm, info.sample_rate());
			PDRUtils.checkAlError("BufferData");
		}
		
		// set up source input
		AL11.alSourcei(sbo, AL11.AL_BUFFER, abo);
		PDRUtils.checkAlError("SourceI["+sbo+"].BUFFER = "+abo);
	}
	
	public void play() {
		System.out.println("is playing: "+isPlaying());
		
		if(!isPlaying())
			return;
		
		// play source
		AL11.alSourcePlay(sbo);
		PDRUtils.checkAlError("SourcePlay("+sbo+")");
		
		System.out.println("is playing: "+isPlaying());
	}

	/**
	 * @return if the sound was playing
	 */
	public boolean stop() {
		boolean wasPlaying = isPlaying();
		if(!wasPlaying)
			return false;
		
		AL11.alSourceStop(sbo);
		PDRUtils.checkAlError();
		
		return wasPlaying;
	}
	
	public boolean isPlaying() {
		return AL11.alGetSourcei(sbo, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
	}
	
	public boolean isLooping() {
		return looping;
	}
	public void setLooping(boolean looping) {
		this.looping = looping;
		AL11.alSourcei(sbo, AL11.AL_LOOPING, looping ? AL11.AL_TRUE : AL11.AL_FALSE);
	}

	@Override
	public void cleanup() {
		AL11.alDeleteBuffers(abo);
		PDRUtils.checkAlError();
		AL11.alDeleteSources(sbo);
		PDRUtils.checkAlError();
	}

	@Override
	public String getId() {
		return name;
	}

}
