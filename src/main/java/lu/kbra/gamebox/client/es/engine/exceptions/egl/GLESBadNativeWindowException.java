package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadNativeWindowException extends GLESRuntimeException {
	
	public GLESBadNativeWindowException(String str) {
		super(str);
	}

	public GLESBadNativeWindowException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
