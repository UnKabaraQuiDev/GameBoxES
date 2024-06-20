package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadMatchException extends EGLRuntimeException {
	
	public EGLBadMatchException(String str) {
		super(str);
	}

	public EGLBadMatchException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
