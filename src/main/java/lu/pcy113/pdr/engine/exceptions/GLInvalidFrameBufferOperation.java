package lu.pcy113.pdr.engine.exceptions;

public class GLInvalidFrameBufferOperation extends GLRuntimeException {

	public GLInvalidFrameBufferOperation(String caller, int status, String msg) {
		super("" + caller + " triggered : " + status + " (" + msg + ")");
	}

}
