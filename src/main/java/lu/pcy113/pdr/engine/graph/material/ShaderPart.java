package lu.pcy113.pdr.engine.graph.material;

import java.util.logging.Level;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.utils.Logger;

public class ShaderPart implements UniqueID, Cleanupable {
	
	private final String file;
	private final int sid;
	private final int type;
	
	public ShaderPart(String file) {
		this.file = file;
		this.type = file.substring(file.lastIndexOf(".")+1).equals("vert") ? GL30.GL_VERTEX_SHADER : GL30.GL_FRAGMENT_SHADER;
		
		this.sid = GL30.glCreateShader(type);
		GL30.glShaderSource(sid, FileUtils.readFile(file));
		GL30.glCompileShader(sid);
		
		if(GL30.glGetShaderi(sid, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
			Logger.log(Level.SEVERE, GL30.glGetShaderInfoLog(sid, 1024));
			cleanup();
		}
	}
	
	@Override
	public void cleanup() {
		GL30.glDeleteShader(sid);
	}
	
	@Override
	public String getId() {return file;}
	public int getSid() {return sid;}
	public String getFile() {return file;}
	public int getType() {return type;}
	
}
