package lu.kbra.gamebox.client.es.engine.exceptions.opengl;

public class GLStackOverflowException extends GLRuntimeException {

	public GLStackOverflowException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
