package lu.kbra.gamebox.client.es.engine.exceptions.opengl;

public class GLOutOfMemoryException extends GLRuntimeException {

	public GLOutOfMemoryException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
