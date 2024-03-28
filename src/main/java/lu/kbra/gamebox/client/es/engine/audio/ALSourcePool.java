package lu.kbra.gamebox.client.es.engine.audio;

import java.util.ArrayList;
import java.util.List;

import lu.kbra.gamebox.client.es.engine.exceptions.openal.ALRuntimeException;

public class ALSourcePool {

	private AudioMaster audio;

	private int maxSources;
	private int sourceCount = 0;
	private List<ALSource> availableSources;
	private List<ALSource> usedSources;

	public ALSourcePool(AudioMaster audio, int maxSources) {
		this.audio = audio;
		this.maxSources = maxSources;
		this.availableSources = new ArrayList<ALSource>();
		this.usedSources = new ArrayList<ALSource>();

		if (!audio.checkAccess()) {
			throw new ALRuntimeException("Could not create ALSourcePool in thread: " + Thread.currentThread().getName() + ", should be in Audio thread: " + audio.getThreadName());
		}
	}

	public ALSource getFreeSource() {
		for (ALSource source : availableSources) {
			if (source.isFree()) {
				return source;
			} else {
				this.usedSources.add(source);
			}
		}

		if (sourceCount >= maxSources) {
			return null;
		}

		// If no free source found, create a new one
		ALSource newSource = new ALSource(this);
		newSource.gen();
		availableSources.add(newSource);
		sourceCount++;
		return newSource;
	}

	public int genSources(int count) {
		for (int i = 0; i < count; i++) {
			try {
				ALSource newSource = new ALSource(this);
				newSource.gen();
				availableSources.add(newSource);
				sourceCount++;
			} catch (ALRuntimeException e) {
				e.printStackTrace();
				return i;
			}

			if (sourceCount <= maxSources) {
				continue;
			}
		}
		return count;
	}

	public void update(ALSource source) {
		if (source.isFree()) {
			this.availableSources.add(source);
			this.usedSources.remove(source);
		} else {
			this.availableSources.remove(source);
			this.usedSources.add(source);
		}
	}

	public void remove(ALSource source) {
		this.availableSources.remove(source);
		this.usedSources.remove(source);
		this.sourceCount--;
	}

}
