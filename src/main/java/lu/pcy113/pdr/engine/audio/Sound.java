package lu.pcy113.pdr.engine.audio;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.libc.LibCStdlib;

import lu.pcy113.pclib.Triplet;
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
		sbo = AL11.alGenBuffers();
		PDRUtils.checkAlError("GenBuffers");

		abo = AL11.alGenSources();
		PDRUtils.checkAlError("GenSources");
		
		Triplet<ShortBuffer, Integer, Integer> vorbis_channels_sampleRate = SoundLoaderUtils.readVorbis(file);
		
		// copy to buffer
		AL11.alBufferData(
				abo,
				vorbis_channels_sampleRate.getSecond() == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16,
				vorbis_channels_sampleRate.getFirst(),
				vorbis_channels_sampleRate.getThird());
		PDRUtils.checkAlError("BufferData("+abo+")");

		// free mem
		LibCStdlib.free(vorbis_channels_sampleRate.getFirst());
		//vorbis_channels_sampleRate.getFirst().clear();
		//vorbis_channels_sampleRate = null;
		
		// set up source input
		AL11.alSourcei(sbo, AL11.AL_BUFFER, abo);
		PDRUtils.checkAlError("SourceI["+sbo+"].BUFFER = "+abo);
		
		setLooping(looping);
	}
	
	public void play() {
		if(isPlaying())
			return;
		
		// play source
		AL11.alSourcePlay(sbo);
		PDRUtils.checkAlError("SourcePlay("+sbo+")");
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
	
	public void replay() {
		stop();
		play();
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
