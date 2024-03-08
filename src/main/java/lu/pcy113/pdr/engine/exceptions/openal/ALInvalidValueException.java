package lu.pcy113.pdr.engine.exceptions.openal;

public class ALInvalidValueException extends ALRuntimeException {

	public ALInvalidValueException(String str) {
		super(str);
	}

	public ALInvalidValueException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

}
