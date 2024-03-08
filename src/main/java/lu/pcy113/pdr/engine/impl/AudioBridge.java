package lu.pcy113.pdr.engine.impl;

import java.nio.file.Files;
import java.nio.file.Paths;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.audio.AudioMaster;
import lu.pcy113.pdr.engine.audio.Sound;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.nexttask.NextTask;
import lu.pcy113.pdr.engine.impl.nexttask.NextTaskFunction;

public class AudioBridge {
	
	private GameEngine engine;
	private AudioMaster master;
	
	public AudioBridge(GameEngine engine) {
		this.engine = engine;
		this.master = engine.getAudioMaster();
	}
	
	public void stop(Sound sound) {
		if(sound == null)
			return;
		
		engine.createTask(engine.QUEUE_AUDIO)
		.exec((s) -> {
			sound.stop();
			
			return 1;
		}).push();
	}
	
	public void play(Sound sound) {
		if(sound == null)
			return;
		
		engine.createTask(engine.QUEUE_AUDIO)
		.exec((s) -> {
			sound.play();
			
			return 1;
		}).push();
	}
	
	public void replay(Sound sound) {
		if(sound == null)
			return;
		
		engine.createTask(engine.QUEUE_AUDIO)
		.exec((s) -> {
			sound.replay();
			
			return 1;
		}).push();
	}
	
	public boolean load(String name, String file, boolean looping, NextTaskFunction<Sound, Void> func) {
		return load(name, file, looping, engine.getCache(), func);
	}
	
	public boolean load(String name, String file, boolean looping) {
		return load(name, file, looping, engine.getCache());
	}
	
	public boolean load(String name, String file, boolean looping, CacheManager cache) {
		return load(name, file, looping, cache, null);
	}
	
	public boolean load(String name, String file, boolean looping, CacheManager cache, NextTaskFunction<Sound, Void> func) {
		if(!Files.exists(Paths.get(file)))
			return false;
		
		NextTask<Void, Sound, Void> nt = engine.createTask(engine.QUEUE_AUDIO);
		
		nt.exec((Void s) -> {
			try {
				Sound sound = new Sound(name, file, looping);
				cache.addSound(sound);
				
				return sound;
			}catch(Exception e) {
				return null;
			}
		});
		
		if(func != null) {
			nt.then(func);
		}
		
		nt.push();
		
		return true;
	}

}
