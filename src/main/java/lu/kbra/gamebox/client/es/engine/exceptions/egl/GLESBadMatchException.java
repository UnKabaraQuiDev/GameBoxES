package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadMatchException extends GLESRuntimeException {
	
	public GLESBadMatchException(String str) {
		super(str);
	}

	public GLESBadMatchException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
