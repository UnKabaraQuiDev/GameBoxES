package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadSurfaceException extends EGLRuntimeException {
	
	public EGLBadSurfaceException(String str) {
		super(str);
	}

	public EGLBadSurfaceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
