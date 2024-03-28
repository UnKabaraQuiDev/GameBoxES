package lu.kbra.gamebox.client.es.engine.exceptions.openal;

public class ALInvalidNameException extends ALRuntimeException {

	public ALInvalidNameException(String str) {
		super(str);
	}

	public ALInvalidNameException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

}
