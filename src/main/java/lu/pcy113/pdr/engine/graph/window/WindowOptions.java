package lu.pcy113.pdr.engine.graph.window;

import org.joml.Vector2i;

import lu.pcy113.pdr.engine.utils.Consts;

public class WindowOptions {

	public int fps = 100;
	public int ups = 30;
	public boolean fullscreen = false;
	public boolean vsync = false;
	public boolean resizable = true;
	public Vector2i windowSize = new Vector2i(800, 600);
	public String title = Consts.TITLE;

}
