package lu.pcy113.pdr.engine.impl.shader;

import java.util.logging.Level;

import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.computing.shader.ComputeShaderPart;
import lu.pcy113.pdr.engine.graph.shader.part.FragmentShaderPart;
import lu.pcy113.pdr.engine.graph.shader.part.GeometryShaderPart;
import lu.pcy113.pdr.engine.graph.shader.part.VertexShaderPart;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.PDRUtils;

public abstract class AbstractShaderPart implements UniqueID, Cleanupable {
	
	public static final int VERTEX = GL40.GL_VERTEX_SHADER;
	public static final int GEOMETRY = GL40.GL_GEOMETRY_SHADER;
	public static final int FRAGMENT = GL40.GL_FRAGMENT_SHADER;
	public static final int COMPUTE = GL43.GL_COMPUTE_SHADER;
	
	private final String file;
	private final int sid;
	private final int type;

	public AbstractShaderPart(String file, int type) {
		this.file = file;
		this.type = type;

		if (type == -1) {
			PDRUtils.throwGLError("Unknown shader type: " + file);
			this.sid = -1;
			return;
		}

		this.sid = GL40.glCreateShader(type);
		GL40.glShaderSource(sid, FileUtils.readFile(file));
		GL40.glCompileShader(sid);

		if (GL40.glGetShaderi(sid, GL40.GL_COMPILE_STATUS) == GL40.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GL40.glGetShaderInfoLog(sid, 1024));
			cleanup();
		} else {
			GlobalLogger.log(Level.INFO, "ShaderPart " + file + " (" + sid + ") (" + type + ") created successfully");
		}
	}
	
	public static AbstractShaderPart load(String file) {
		int type = shaderType(file.substring(file.lastIndexOf(".") + 1));
		switch (type) {
		case VERTEX:
			return new VertexShaderPart(file);
		case GEOMETRY:
			return new GeometryShaderPart(file);
		case FRAGMENT:
			return new FragmentShaderPart(file);
		case COMPUTE:
			return new ComputeShaderPart(file);
		default:
			PDRUtils.throwGLError("Unknown shader part type: "+file);
			return null;
		}
	}
	
	@Override
	public void cleanup() {
		GL40.glDeleteShader(sid);
	}

	@Override
	public String getId() {
		return file;
	}

	public int getSid() {
		return sid;
	}

	public String getFile() {
		return file;
	}

	public int getType() {
		return type;
	}

	public static int shaderType(String type) {
		switch (type.toLowerCase()) {
		case "vert":
			return VERTEX;
		case "frag":
			return FRAGMENT;
		case "geo":
			return GEOMETRY;
		case "comp":
			return COMPUTE;
		}
		PDRUtils.throwGLError("Unknown shader type: "+type);
		return -1;
	}

}
