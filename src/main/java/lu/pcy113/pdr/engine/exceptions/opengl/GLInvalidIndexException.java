package lu.pcy113.pdr.engine.exceptions.opengl;

public class GLInvalidIndexException extends GLRuntimeException {

	public GLInvalidIndexException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}