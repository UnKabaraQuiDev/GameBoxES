package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadAllocException extends GLESRuntimeException {
	
	public GLESBadAllocException(String str) {
		super(str);
	}

	public GLESBadAllocException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
