package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLRuntimeException extends RuntimeException {
	
	public EGLRuntimeException(String str) {
		super(str);
	}

	public EGLRuntimeException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}
	
}
