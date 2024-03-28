package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadCurrentSurfaceException extends GLESRuntimeException {
	
	public GLESBadCurrentSurfaceException(String str) {
		super(str);
	}

	public GLESBadCurrentSurfaceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
