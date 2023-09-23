package lu.pcy113.pdr.engine.graph.composition.debug;

import java.util.LinkedList;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.impl.Renderable;

public class PerfHistoryLayerMaterial extends Material {
	
	public static final String NAME = PerfHistoryLayerMaterial.class.getName();
	
	public PerfHistoryLayerMaterial() {
		super(NAME, PerfHistoryLayerShader.NAME);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
		if(parent instanceof PerfHistoryLayer) {
			PerfHistoryLayer rl = (PerfHistoryLayer) parent;
			
			LinkedList<Double> deltaUpdate = rl.getDeltaUpdate();
			LinkedList<Double> deltaRender = rl.getDeltaRender();
			LinkedList<Double> timeUpdate = rl.getTimeUpdate();
			LinkedList<Double> timeRender = rl.getTimeRender();
			
			for(int i = 0; i < deltaUpdate.size(); i++) {
				super.setProperty(PerfHistoryLayerShader.DU+"["+i+"]", deltaUpdate.get(i));
				super.setProperty(PerfHistoryLayerShader.DR+"["+i+"]", deltaRender.get(i));
				super.setProperty(PerfHistoryLayerShader.TU+"["+i+"]", timeUpdate.get(i));
				super.setProperty(PerfHistoryLayerShader.TR+"["+i+"]", timeRender.get(i));
			}
		}
		
		super.bindProperties(cache, parent, shader);
	}
	
}
