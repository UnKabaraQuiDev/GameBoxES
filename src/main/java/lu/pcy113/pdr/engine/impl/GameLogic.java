package lu.pcy113.pdr.engine.impl;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.renderer.Renderer;
import lu.pcy113.pdr.engine.scene.Scene;

public interface GameLogic extends Cleanupable {
	
	public void init(Window window, Scene scene, Renderer renderer);
	
	public void input(Window window, Scene scene, long dTime);
	
	public void update(Window window, Scene scene, float dTime);
	
}
