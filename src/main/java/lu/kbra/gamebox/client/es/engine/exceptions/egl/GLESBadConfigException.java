package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESBadConfigException extends GLESRuntimeException {
	
	public GLESBadConfigException(String str) {
		super(str);
	}

	public GLESBadConfigException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
