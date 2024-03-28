package lu.pcy113.pdr.engine.exceptions.opengl;

public class GLInvalidValueException extends GLRuntimeException {

	public GLInvalidValueException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
