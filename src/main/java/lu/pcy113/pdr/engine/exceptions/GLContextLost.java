package lu.pcy113.pdr.engine.exceptions;

public class GLContextLost extends GLRuntimeException {
	
	public GLContextLost(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
