package lu.pcy113.pdr.engine.exceptions.opengl;

public class GLContextLost extends GLRuntimeException {

	public GLContextLost(String caller, int status, String msg) {
		super(caller, status, msg);
	}

}
