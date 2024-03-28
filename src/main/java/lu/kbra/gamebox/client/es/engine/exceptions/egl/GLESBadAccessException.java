package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadAccessException extends GLESRuntimeException {
	
	public GLESBadAccessException(String str) {
		super(str);
	}

	public GLESBadAccessException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
