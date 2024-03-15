package lu.pcy113.pdr.engine.audio;

import java.nio.ShortBuffer;

import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;
import org.lwjgl.system.libc.LibCStdlib;

import lu.pcy113.pclib.Triplet;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.file.FileUtils;

public class Sound implements UniqueID, Cleanupable {

	private final String name;
	private int abo = -1, sbo = -1;

	public Sound(String name, String file) {
		this.name = name;

		switch (FileUtils.getExtension(file)) {
		case "ogg":
			loadVorbis(file);
			break;
		default:
			throw new RuntimeException("Unsupported file type: " + FileUtils.getExtension(file));
		}
	}

	private void loadVorbis(String file) {
		sbo = AL11.alGenBuffers();
		PDRUtils.checkAlError("GenBuffers");

		abo = AL11.alGenSources();
		PDRUtils.checkAlError("GenSources");

		Triplet<ShortBuffer, Integer, Integer> vorbis_channels_sampleRate = SoundLoaderUtils.readVorbis(file);

		// copy to buffer
		AL11.alBufferData(abo, vorbis_channels_sampleRate.getSecond() == 1 ? AL11.AL_FORMAT_MONO16 : AL11.AL_FORMAT_STEREO16, vorbis_channels_sampleRate.getFirst(), vorbis_channels_sampleRate.getThird());
		PDRUtils.checkAlError("BufferData(" + abo + ")");

		// free mem
		LibCStdlib.free(vorbis_channels_sampleRate.getFirst());
		// vorbis_channels_sampleRate.getFirst().clear();
		// vorbis_channels_sampleRate = null;

		// set up source input
		AL11.alSourcei(sbo, AL11.AL_BUFFER, abo);
		PDRUtils.checkAlError("SourceI[" + sbo + "].BUFFER = " + abo);
	}

	public Sound setVolume(float volume) {
		volume = Math.clamp(0, 1, volume);
		AL11.alSourcef(sbo, AL11.AL_GAIN, volume);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].GAIN=" + volume);
		return this;
	}

	public Sound setPosition(Vector3f vec) {
		System.err.println("sound func pos: " + vec+" finite: "+vec.isFinite());
		
		if(!vec.isFinite())
			return this;
		
		AL11.alSource3f(sbo, AL11.AL_POSITION, vec.x, vec.y, vec.z);
		PDRUtils.checkAlError("Source3f[" + sbo + "].POSITION=" + vec);
		return this;
	}

	public Sound setVelocity(Vector3f vec) {
		AL11.alSource3f(sbo, AL11.AL_VELOCITY, vec.x, vec.y, vec.z);
		PDRUtils.checkAlError("Source3f[" + sbo + "].VELOCITY=" + vec);
		return this;
	}

	public Sound setDirection(Vector3f vec) {
		AL11.alSource3f(sbo, AL11.AL_DIRECTION, vec.x, vec.y, vec.z);
		PDRUtils.checkAlError("Source3f[" + sbo + "].DIRECTION=" + vec);
		return this;
	}

	public Sound setPitch(float pitch) {
		AL11.alSourcef(sbo, AL11.AL_PITCH, pitch);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].PITCH=" + pitch);
		return this;
	}

	public Sound setReferenceDistance(float distance) {
		AL11.alSourcef(sbo, AL11.AL_REFERENCE_DISTANCE, distance);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].REFERENCE_DISTANCE=" + distance);
		return this;
	}

	public Sound setLooping(boolean looping) {
		AL11.alSourcei(sbo, AL11.AL_LOOPING, looping ? AL11.AL_TRUE : AL11.AL_FALSE);
		PDRUtils.checkAlError("Sourcei[" + sbo + "].LOOPING=" + looping);
		return this;
	}
	
	public Sound setMaxDistance(float distance) {
		AL11.alSourcef(sbo, AL11.AL_MAX_DISTANCE, distance);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].MAX_DISTANCE=" + distance);
		return this;
	}
	
	public Sound setConeInnerAngle(float angle) {
		AL11.alSourcef(sbo, AL11.AL_CONE_INNER_ANGLE, angle);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].CONE_INNER_ANGLE=" + angle);
		return this;
	}
	
	public Sound setConeOuterAngle(float angle) {
		AL11.alSourcef(sbo, AL11.AL_CONE_OUTER_ANGLE, angle);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].CONE_OUTER_ANGLE=" + angle);
		return this;
	}
	
	public Sound setConeOuterGain(float gain) {
		AL11.alSourcef(sbo, AL11.AL_CONE_OUTER_GAIN, gain);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].CONE_OUTER_GAIN=" + gain);
		return this;
	}
	
	public Sound setRolloffFactor(float factor) {
		AL11.alSourcef(sbo, AL11.AL_ROLLOFF_FACTOR, factor);
		PDRUtils.checkAlError("Sourcef[" + sbo + "].ROLLOFF_FACTOR=" + factor);
		return this;
	}
	
	/*public Sound setSourceType(int type) {
		AL11.alSourcei(sbo, AL11.AL_SOURCE_TYPE, type);
		PDRUtils.checkAlError("Sourcei[" + sbo + "].SOURCE_TYPE=" + type);
		return this;
	}*/
	
	/*public Sound setSourceState(int state) {
		AL11.alSourcei(sbo, AL11.AL_SOURCE_STATE, state);
		PDRUtils.checkAlError("Sourcei[" + sbo + "].SOURCE_STATE=" + state);
		return this;
	}*/

	public void play() {
		if (isPlaying())
			return;

		// play source
		AL11.alSourcePlay(sbo);
		PDRUtils.checkAlError("SourcePlay(" + sbo + ")");
	}

	/**
	 * @return if the sound was playing
	 */
	public boolean stop() {
		boolean wasPlaying = isPlaying();
		if (!wasPlaying)
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
		return AL11.alGetSourcei(sbo, AL11.AL_LOOPING) == AL11.AL_TRUE;
	}

	public float getVolume() {
		return AL11.alGetSourcef(sbo, AL11.AL_GAIN);
	}

	public float getPitch() {
		return AL11.alGetSourcef(sbo, AL11.AL_PITCH);
	}

	public float getReferenceDistance() {
		return AL11.alGetSourcef(sbo, AL11.AL_REFERENCE_DISTANCE);
	}

	public Vector3f getPosition() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sbo, AL11.AL_POSITION, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
	}

	public Vector3f getVelocity() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sbo, AL11.AL_VELOCITY, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
	}

	public Vector3f getDirection() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sbo, AL11.AL_DIRECTION, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
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
