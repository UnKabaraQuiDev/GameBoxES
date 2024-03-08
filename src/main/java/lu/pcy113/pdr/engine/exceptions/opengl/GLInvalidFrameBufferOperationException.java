package lu.pcy113.pdr.engine.exceptions.opengl;

public class GLInvalidFrameBufferOperationException extends GLRuntimeException {

	public GLInvalidFrameBufferOperationException(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
