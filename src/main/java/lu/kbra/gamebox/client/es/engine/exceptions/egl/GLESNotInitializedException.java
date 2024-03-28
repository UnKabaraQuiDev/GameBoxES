package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESNotInitializedException extends GLESRuntimeException {
	
	public GLESNotInitializedException(String str) {
		super(str);
	}

	public GLESNotInitializedException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
