package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadAttributeException extends EGLRuntimeException {
	
	public EGLBadAttributeException(String str) {
		super(str);
	}

	public EGLBadAttributeException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
