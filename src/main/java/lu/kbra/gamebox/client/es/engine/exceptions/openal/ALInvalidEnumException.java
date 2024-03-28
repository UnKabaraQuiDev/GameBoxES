package lu.kbra.gamebox.client.es.engine.exceptions.openal;

public class ALInvalidEnumException extends ALRuntimeException {

	public ALInvalidEnumException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

	public ALInvalidEnumException(String str) {
		super(str);
	}

}
