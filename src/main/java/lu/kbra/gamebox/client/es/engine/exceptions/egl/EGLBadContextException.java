package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadContextException extends EGLRuntimeException {
	
	public EGLBadContextException(String str) {
		super(str);
	}

	public EGLBadContextException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
