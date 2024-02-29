package lu.pcy113.pdr.engine.exceptions;

public class GLStackUnderflowException extends GLRuntimeException {

	public GLStackUnderflowException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
