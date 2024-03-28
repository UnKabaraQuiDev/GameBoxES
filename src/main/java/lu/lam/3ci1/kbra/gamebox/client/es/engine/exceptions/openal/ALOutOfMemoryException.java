package lu.pcy113.pdr.engine.exceptions.openal;

public class ALOutOfMemoryException extends ALRuntimeException {

	public ALOutOfMemoryException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

	public ALOutOfMemoryException(String str) {
		super(str);
	}

}
