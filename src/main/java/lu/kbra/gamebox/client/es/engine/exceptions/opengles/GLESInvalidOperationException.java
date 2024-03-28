package lu.kbra.gamebox.client.es.engine.exceptions.opengles;

public class GLESInvalidOperationException extends GLESRuntimeException {

	public GLESInvalidOperationException(String str) {
		super(str);
	}

	public GLESInvalidOperationException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

}
