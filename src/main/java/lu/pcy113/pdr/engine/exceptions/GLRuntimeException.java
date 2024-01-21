package lu.pcy113.pdr.engine.exceptions;

public class GLRuntimeException extends RuntimeException {

	public GLRuntimeException(String str) {
		super(str);
	}
	
	public GLRuntimeException(String caller, int status, String msg) {
		super(""+caller+" triggered : "+status+" ("+msg+")");
	}

}
