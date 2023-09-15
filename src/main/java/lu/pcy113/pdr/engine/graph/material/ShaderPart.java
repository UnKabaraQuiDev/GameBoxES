package lu.pcy113.pdr.engine.graph.material;

import java.util.logging.Level;

import org.lwjgl.opengl.GL40;

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
		this.type = shaderType(file.substring(file.lastIndexOf(".")+1));
		
		if(type == -1)
			throw new RuntimeException("Unknown shader type: "+file);
		
		this.sid = GL40.glCreateShader(type);
		GL40.glShaderSource(sid, FileUtils.readFile(file));
		GL40.glCompileShader(sid);
		
		if(GL40.glGetShaderi(sid, GL40.GL_COMPILE_STATUS) == GL40.GL_FALSE) {
			Logger.log(Level.SEVERE, GL40.glGetShaderInfoLog(sid, 1024));
			cleanup();
		}
	}
	
	@Override
	public void cleanup() {
		GL40.glDeleteShader(sid);
	}
	
	@Override
	public String getId() {return file;}
	public int getSid() {return sid;}
	public String getFile() {return file;}
	public int getType() {return type;}
	
	public static int shaderType(String type) {
		switch(type.toLowerCase()) {
		case "vert":
			return GL40.GL_VERTEX_SHADER;
		case "frag":
			return GL40.GL_FRAGMENT_SHADER;
		case "geom":
			return GL40.GL_GEOMETRY_SHADER;
		}
		return -1;
	}
	
}
