package lu.kbra.gamebox.client.es.engine.exceptions.opengl;

public class GLRuntimeException extends RuntimeException {

	public GLRuntimeException(String str) {
		super(str);
	}

	public GLRuntimeException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
