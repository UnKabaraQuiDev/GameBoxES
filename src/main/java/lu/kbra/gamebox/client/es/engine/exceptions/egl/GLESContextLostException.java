package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class GLESContextLostException extends GLESRuntimeException {
	
	public GLESContextLostException(String str) {
		super(str);
	}

	public GLESContextLostException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
