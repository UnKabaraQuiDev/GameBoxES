package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadAccessException extends EGLRuntimeException {
	
	public EGLBadAccessException(String str) {
		super(str);
	}

	public EGLBadAccessException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
