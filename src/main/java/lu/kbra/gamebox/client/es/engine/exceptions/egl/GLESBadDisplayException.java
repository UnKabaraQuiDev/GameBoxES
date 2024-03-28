package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadDisplayException extends GLESRuntimeException {
	
	public GLESBadDisplayException(String str) {
		super(str);
	}

	public GLESBadDisplayException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
