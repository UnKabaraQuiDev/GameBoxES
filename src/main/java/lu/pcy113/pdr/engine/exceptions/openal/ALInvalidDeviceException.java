package lu.pcy113.pdr.engine.exceptions.openal;

public class ALInvalidDeviceException extends ALRuntimeException {

	public ALInvalidDeviceException(String caller, int status, String msg) {
		super(caller, status, msg);
	}

	public ALInvalidDeviceException(String str) {
		super(str);
	}

}
