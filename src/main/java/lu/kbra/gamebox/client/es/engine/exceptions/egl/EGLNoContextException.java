package lu.kbra.gamebox.client.es.engine.exceptions.egl;

@Deprecated
public class EGLNoContextException extends EGLRuntimeException {
	
	public EGLNoContextException(String str) {
		super(str);
	}

	public EGLNoContextException(String caller, int status, String msg) {
		super(caller, status, msg);
	}
	
}
