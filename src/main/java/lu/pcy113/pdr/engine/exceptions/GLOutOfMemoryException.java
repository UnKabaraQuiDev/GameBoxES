package lu.pcy113.pdr.engine.exceptions;

public class GLOutOfMemoryException extends GLRuntimeException {

	public GLOutOfMemoryException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
