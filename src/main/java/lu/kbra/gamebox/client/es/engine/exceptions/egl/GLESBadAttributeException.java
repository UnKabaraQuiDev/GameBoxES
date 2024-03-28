package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadAttributeException extends GLESRuntimeException {
	
	public GLESBadAttributeException(String str) {
		super(str);
	}

	public GLESBadAttributeException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
