package lu.pcy113.pdr.engine.exceptions;

public class GLInvalidEnumException extends GLRuntimeException {

	public GLInvalidEnumException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
