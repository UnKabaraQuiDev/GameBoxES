package lu.kbra.gamebox.client.es.engine.impl.shader;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.computing.shader.part.ComputeShaderPart;
import lu.kbra.gamebox.client.es.engine.graph.shader.part.FragmentShaderPart;
import lu.kbra.gamebox.client.es.engine.graph.shader.part.GeometryShaderPart;
import lu.kbra.gamebox.client.es.engine.graph.shader.part.VertexShaderPart;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

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

		if(!Files.exists(Paths.get(file))) {
			PDRUtils.throwGLError("File: "+file+" not found");
			this.sid = -1;
			return;
		}
		
		if (type == -1) {
			PDRUtils.throwGLError("Unknown shader type: " + file);
			this.sid = -1;
			return;
		}

		this.sid = GL40.glCreateShader(type);
		PDRUtils.checkGlError("CreateShader(" + type + ") (" + file + ")");
		GL40.glShaderSource(sid, FileUtils.readStringFile(file));
		PDRUtils.checkGlError("ShaderSource(" + sid + ") (" + file + ")");
		GL40.glCompileShader(sid);
		PDRUtils.checkGlError("CompileShader(" + sid + ") (" + file + ")");

		if (GL40.glGetShaderi(sid, GL40.GL_COMPILE_STATUS) == GL40.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GL40.glGetShaderInfoLog(sid, 1024));
			cleanup();
			throw new IllegalStateException(file + "(" + sid + "): Failed to compile shader!");
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
			PDRUtils.throwGLError("Unknown shader part type: " + file);
			return null;
		}
	}

	public boolean recompile() {
		GL40.glShaderSource(sid, FileUtils.readStringFile(file));
		PDRUtils.checkGlError("ShaderSource(" + sid + ") (" + file + ")");
		GL40.glCompileShader(sid);
		PDRUtils.checkGlError("CompileShader(" + sid + ") (" + file + ")");

		if (GL40.glGetShaderi(sid, GL40.GL_COMPILE_STATUS) == GL40.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GL40.glGetShaderInfoLog(sid, 1024));
			// throw new IllegalStateException(file + "(" + sid + "): Failed to recompile
			// shader!");
			return false;
		} else {
			GlobalLogger.log(Level.INFO, "ShaderPart " + file + " (" + sid + ") (" + type + ") recompiled successfully");
			return true;
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
		PDRUtils.throwGLError("Unknown shader type: " + type);
		return -1;
	}

}
