package lu.kbra.gamebox.client.es.engine.exceptions.opengl;

public class GLInvalidOperationException extends GLRuntimeException {

	public GLInvalidOperationException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
