package lu.kbra.gamebox.client.es.engine.graph.window;

import lu.kbra.gamebox.client.es.engine.utils.consts.GLType;

@Deprecated
public class GLWindow extends Window {

	public GLWindow(WindowOptions options) {
		super(GLType.GL, options);
	}

	@Override
	protected void init() {
	}

	@Override
	public void takeGLContext() {
	}

	@Override
	public void cleanup() {
	}
	
	public void cleanupGLFW() {
	}

}
