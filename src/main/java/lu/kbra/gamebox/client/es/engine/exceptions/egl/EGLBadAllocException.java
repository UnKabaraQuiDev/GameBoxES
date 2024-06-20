package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadAllocException extends EGLRuntimeException {
	
	public EGLBadAllocException(String str) {
		super(str);
	}

	public EGLBadAllocException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
