package lu.kbra.gamebox.client.es.engine.exceptions.opengles;

public class GLESOutOfMemoryException extends GLESRuntimeException {

	public GLESOutOfMemoryException(String str) {
		super(str);
	}

	public GLESOutOfMemoryException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

}
