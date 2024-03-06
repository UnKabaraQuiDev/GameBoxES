package lu.pcy113.pdr.engine.exceptions;

public class GLInvalidOperationException extends GLRuntimeException {

	public GLInvalidOperationException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
