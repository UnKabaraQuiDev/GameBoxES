package lu.kbra.gamebox.client.es.engine.exceptions.openal;

public class ALInvalidDeviceException extends ALRuntimeException {

	public ALInvalidDeviceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

	public ALInvalidDeviceException(String str) {
		super(str);
	}

}
