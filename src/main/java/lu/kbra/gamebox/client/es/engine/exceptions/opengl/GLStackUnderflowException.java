package lu.kbra.gamebox.client.es.engine.exceptions.opengl;

public class GLStackUnderflowException extends GLRuntimeException {

	public GLStackUnderflowException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
