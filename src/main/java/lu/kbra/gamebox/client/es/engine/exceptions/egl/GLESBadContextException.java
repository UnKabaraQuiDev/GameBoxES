package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadContextException extends GLESRuntimeException {
	
	public GLESBadContextException(String str) {
		super(str);
	}

	public GLESBadContextException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
