package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadSurfaceException extends GLESRuntimeException {
	
	public GLESBadSurfaceException(String str) {
		super(str);
	}

	public GLESBadSurfaceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
