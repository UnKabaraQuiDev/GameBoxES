package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadNativePixmapException extends EGLRuntimeException {
	
	public EGLBadNativePixmapException(String str) {
		super(str);
	}

	public EGLBadNativePixmapException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
