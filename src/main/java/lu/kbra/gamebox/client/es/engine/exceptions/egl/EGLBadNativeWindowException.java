package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadNativeWindowException extends EGLRuntimeException {
	
	public EGLBadNativeWindowException(String str) {
		super(str);
	}

	public EGLBadNativeWindowException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
