package lu.pcy113.pdr.engine.scene.geom;

import java.util.ArrayList;
import java.util.List;

import lu.pcy113.pdr.engine.impl.Cleanupable;

public class ParticleSystem implements Cleanupable {
	
	private int vao;
	private List<Integer> vboIdList = new ArrayList<>();
	
	public ParticleSystem() {
		
	}
	
	@Override
	public void cleanup() {
		
	}

}
