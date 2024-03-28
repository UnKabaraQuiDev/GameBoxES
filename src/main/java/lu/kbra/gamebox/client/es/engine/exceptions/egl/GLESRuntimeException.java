package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESRuntimeException extends RuntimeException {
	
	public GLESRuntimeException(String str) {
		super(str);
	}

	public GLESRuntimeException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}
	
}
