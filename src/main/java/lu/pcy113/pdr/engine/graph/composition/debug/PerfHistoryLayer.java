package lu.pcy113.pdr.engine.graph.composition.debug;

import java.util.LinkedList;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.graph.composition.PassRenderLayer;

public class PerfHistoryLayer extends PassRenderLayer {
	
	public static final String NAME = PerfHistoryLayer.class.getName();
	
	private LinkedList<Double> deltaUpdate = new LinkedList<>();
	private LinkedList<Double> deltaRender = new LinkedList<>();
	private LinkedList<Double> timeUpdate = new LinkedList<>();
	private LinkedList<Double> timeRender = new LinkedList<>();
	
	public PerfHistoryLayer() {
		super(NAME, PerfHistoryLayerMaterial.NAME);
	}
	
	int i = 0;
	public void update(GameEngine e, double deltaUpdate, double deltaRender, double timeUpdate, double timeRender) {
		if(i < 10000) {
			i++;
			return;
		}
		i = 0;
		
		this.deltaRender.add(deltaRender);
		this.deltaUpdate.add(deltaUpdate);
		this.timeRender.add(timeRender);
		this.timeUpdate.add(timeUpdate);
		
		if(this.deltaRender.size() > PerfHistoryLayerShader.MAX)
			this.deltaRender.removeFirst();
		if(this.deltaUpdate.size() > PerfHistoryLayerShader.MAX)
			this.deltaUpdate.removeFirst();
		if(this.timeRender.size() > PerfHistoryLayerShader.MAX)
			this.timeRender.removeFirst();
		if(this.timeUpdate.size() > PerfHistoryLayerShader.MAX)
			this.timeUpdate.removeFirst();
	}
	
	public LinkedList<Double> getDeltaRender() {return deltaRender;}
	public LinkedList<Double> getDeltaUpdate() {return deltaUpdate;}
	public LinkedList<Double> getTimeRender() {return timeRender;}
	public LinkedList<Double> getTimeUpdate() {return timeUpdate;}
	
}
