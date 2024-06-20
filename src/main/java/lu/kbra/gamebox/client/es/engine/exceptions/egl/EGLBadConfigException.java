package lu.kbra.gamebox.client.es.engine.exceptions.egl;

public class EGLBadConfigException extends EGLRuntimeException {
	
	public EGLBadConfigException(String str) {
		super(str);
	}

	public EGLBadConfigException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
