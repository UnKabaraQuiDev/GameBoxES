package lu.kbra.gamebox.client.es.engine.graph.window;

import org.joml.Vector2i;

import lu.kbra.gamebox.client.es.engine.utils.consts.Consts;

public class WindowOptions {

	public int fps = 60;
	public int ups = 30;
	public boolean fullscreen = false;
	public boolean vsync = true;
	public boolean resizable = true;
	public Vector2i windowSize = new Vector2i(800, 600);
	public String title = Consts.TITLE;
	public int windowMultisample = 4;

}
