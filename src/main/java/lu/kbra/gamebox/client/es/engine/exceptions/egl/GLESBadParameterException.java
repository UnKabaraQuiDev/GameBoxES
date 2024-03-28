package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadParameterException extends GLESRuntimeException {
	
	public GLESBadParameterException(String str) {
		super(str);
	}

	public GLESBadParameterException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
