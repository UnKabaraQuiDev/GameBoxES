package lu.kbra.gamebox.client.es.engine.exceptions.openal;

public class ALRuntimeException extends RuntimeException {

	public ALRuntimeException(String str) {
		super(str);
	}

	public ALRuntimeException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
