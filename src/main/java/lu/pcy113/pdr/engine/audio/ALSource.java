package lu.pcy113.pdr.engine.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.PDRUtils;

public class ALSource implements Cleanupable {

	private ALSourcePool pool;

	private int sourceId = -1;
	private boolean free = true;

	public ALSource(ALSourcePool pool) {
		this.pool = pool;
	}

	public ALSource gen() {
		if (sourceId == -1) {
			sourceId = AL11.alGenSources();
			PDRUtils.checkAlError("GenSources()");
		}
		return this;
	}

	public ALSource play(ALBuffer buffer) {
		if (buffer == null) {
			return this;
		}

		AL11.alSourcei(sourceId, AL11.AL_BUFFER, buffer.getBufferId());
		PDRUtils.checkAlError("SourceI(" + sourceId + ", BUFFER, " + buffer.getBufferId() + ")");
		AL11.alSourcePlay(sourceId);
		PDRUtils.checkAlError("SourcePlay(" + sourceId + ")");
		free = false;
		pool.update(this);
		return this;
	}

	public ALSource play(Sound bz) {
		if (bz.hasBuffer()) {
			return play(bz.getBuffer());
		} else {
			return this;
		}
	}

	public ALSource play() {
		AL11.alSourcePlay(sourceId);
		PDRUtils.checkAlError("SourcePlay(" + sourceId + ")");
		free = false;
		pool.update(this);
		return this;
	}

	public ALSource replay(ALBuffer buffer) {
		stop();
		play(buffer);
		return this;
	}

	public ALSource appendQueue(ALBuffer buffer) {
		AL11.alSourceQueueBuffers(sourceId, buffer.getBufferId());
		PDRUtils.checkAlError("SourceQueueBuffers(" + sourceId + ", " + buffer.getBufferId() + ")");
		return this;
	}

	public ALSource appendQueue(Sound sound) {
		return appendQueue(sound.getBuffer());
	}

	public void stop() {
		AL11.alSourceStop(sourceId);
		PDRUtils.checkAlError("SourceStop(" + sourceId + ")");
		AL11.alSourceRewind(sourceId);
		PDRUtils.checkAlError("SourceRewind(" + sourceId + ")");
		free = true;
		pool.update(this);
	}

	public ALSource pause() {
		AL11.alSourcePause(sourceId);
		PDRUtils.checkAlError("SourcePause(" + sourceId + ")");
		return this;
	}

	public boolean isFree() {
		return free;
	}

	public ALSource setDirection(Vector3f direction) {
		if (!direction.isFinite())
			return this;
		AL11.alSource3f(sourceId, AL11.AL_DIRECTION, direction.x, direction.y, direction.z);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].DIRECTION=" + direction);
		return this;
	}

	public ALSource setPosition(Vector3f position) {
		if (!position.isFinite())
			return this;
		AL11.alSource3f(sourceId, AL11.AL_POSITION, position.x, position.y, position.z);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].POSITION=" + position);
		return this;
	}

	public ALSource setVelocity(Vector3f velocity) {
		if (!velocity.isFinite())
			return this;
		AL11.alSource3f(sourceId, AL11.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].VELOCITY=" + velocity);
		return this;
	}

	public ALSource setPitch(float pitch) {
		AL11.alSourcef(sourceId, AL11.AL_PITCH, pitch);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].PITCH=" + pitch);
		return this;
	}

	public ALSource setVolume(float gain) {
		AL11.alSourcef(sourceId, AL11.AL_GAIN, gain);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].GAIN=" + gain);
		return this;
	}

	public ALSource setLooping(boolean looping) {
		AL11.alSourcei(sourceId, AL11.AL_LOOPING, looping ? AL11.AL_TRUE : AL11.AL_FALSE);
		PDRUtils.checkAlError("Source3f[" + sourceId + "].LOOPING=" + looping);
		return this;
	}

	public ALSource setRolloffFactor(float factor) {
		AL11.alSourcef(sourceId, AL11.AL_ROLLOFF_FACTOR, factor);
		PDRUtils.checkAlError("Sourcef[" + sourceId + "].ROLLOFF_FACTOR=" + factor);
		return this;
	}

	public ALSource setMaxDistance(float distance) {
		AL11.alSourcef(sourceId, AL11.AL_MAX_DISTANCE, distance);
		PDRUtils.checkAlError("Sourcef[" + sourceId + "].MAX_DISTANCE=" + distance);
		return this;
	}

	public ALSource setConeInnerAngle(float angle) {
		AL11.alSourcef(sourceId, AL11.AL_CONE_INNER_ANGLE, angle);
		PDRUtils.checkAlError("Sourcef[" + sourceId + "].CONE_INNER_ANGLE=" + angle);
		return this;
	}

	public ALSource setConeOuterAngle(float angle) {
		AL11.alSourcef(sourceId, AL11.AL_CONE_OUTER_ANGLE, angle);
		PDRUtils.checkAlError("Sourcef[" + sourceId + "].CONE_OUTER_ANGLE=" + angle);
		return this;
	}

	public ALSource setConeOuterGain(float gain) {
		AL11.alSourcef(sourceId, AL11.AL_CONE_OUTER_GAIN, gain);
		PDRUtils.checkAlError("Sourcef[" + sourceId + "].CONE_OUTER_GAIN=" + gain);
		return this;
	}

	public boolean isPlaying() {
		return AL11.alGetSourcei(sourceId, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING;
	}

	public boolean isLooping() {
		return AL11.alGetSourcei(sourceId, AL11.AL_LOOPING) == AL11.AL_TRUE;
	}

	public float getVolume() {
		return AL11.alGetSourcef(sourceId, AL11.AL_GAIN);
	}

	public float getPitch() {
		return AL11.alGetSourcef(sourceId, AL11.AL_PITCH);
	}

	public float getReferenceDistance() {
		return AL11.alGetSourcef(sourceId, AL11.AL_REFERENCE_DISTANCE);
	}

	public Vector3f getPosition() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sourceId, AL11.AL_POSITION, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
	}

	public Vector3f getVelocity() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sourceId, AL11.AL_VELOCITY, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
	}

	public Vector3f getDirection() {
		float[] x = { 0 }, y = { 0 }, z = { 0 };
		AL11.alGetSource3f(sourceId, AL11.AL_DIRECTION, x, y, z);
		return new Vector3f(x[0], y[0], z[0]);
	}

	@Override
	public void cleanup() {
		stop();
		AL11.alDeleteSources(sourceId);
		PDRUtils.checkAlError("DeleteSources(" + sourceId + ")");
		pool.remove(this);
	}

}
