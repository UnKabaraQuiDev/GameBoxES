package lu.kbra.gamebox.client.es.engine.exceptions.openal;

public class ALOutOfMemoryException extends ALRuntimeException {

	public ALOutOfMemoryException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

	public ALOutOfMemoryException(String str) {
		super(str);
	}

}
