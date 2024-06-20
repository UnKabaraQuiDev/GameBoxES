package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadCurrentSurfaceException extends EGLRuntimeException {
	
	public EGLBadCurrentSurfaceException(String str) {
		super(str);
	}

	public EGLBadCurrentSurfaceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
