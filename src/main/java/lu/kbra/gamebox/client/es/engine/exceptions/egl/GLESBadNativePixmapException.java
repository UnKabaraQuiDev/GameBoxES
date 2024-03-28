package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadNativePixmapException extends GLESRuntimeException {
	
	public GLESBadNativePixmapException(String str) {
		super(str);
	}

	public GLESBadNativePixmapException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
