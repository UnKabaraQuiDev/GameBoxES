package lu.kbra.gamebox.client.es.engine.impl.shader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.graph.shader.part.FragmentShaderPart;
import lu.kbra.gamebox.client.es.engine.graph.shader.part.VertexShaderPart;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

public abstract class AbstractShaderPart implements UniqueID, Cleanupable {

	public static final int VERTEX = GLES30.GL_VERTEX_SHADER;
	// public static final int GEOMETRY = GLES30.GL_GEOMETRY_SHADER;
	public static final int FRAGMENT = GLES30.GL_FRAGMENT_SHADER;
	// public static final int COMPUTE = GLES30.GL_COMPUTE_SHADER;

	private final String file;
	private int sid;
	private final int type;

	public AbstractShaderPart(String file, int type) {
		this.file = file;
		this.type = type;

		if (!Files.exists(Paths.get(file))) {
			PDRUtils.throwGLESError("File: " + file + " not found");
			this.sid = -1;
			return;
		}

		if (type == -1) {
			PDRUtils.throwGLESError("Unknown shader type: " + file);
			this.sid = -1;
			return;
		}

		this.sid = GLES30.glCreateShader(type);
		PDRUtils.checkGlESError("CreateShader(" + type + ") (" + file + ")");
		GLES30.glShaderSource(sid, FileUtils.readStringFile(file));
		PDRUtils.checkGlESError("ShaderSource(" + sid + ") (" + file + ")");
		GLES30.glCompileShader(sid);
		PDRUtils.checkGlESError("CompileShader(" + sid + ") (" + file + ")");

		if (GLES30.glGetShaderi(sid, GLES30.GL_COMPILE_STATUS) == GLES30.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GLES30.glGetShaderInfoLog(sid, 1024));
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
		/*case GEOMETRY:
			return new GeometryShaderPart(file);*/
		case FRAGMENT:
			return new FragmentShaderPart(file);
		/*case COMPUTE:
			return new ComputeShaderPart(file);*/
		default:
			PDRUtils.throwGLESError("Unknown shader part type: " + file);
			return null;
		}
	}

	public boolean recompile() {
		GLES30.glShaderSource(sid, FileUtils.readStringFile(file));
		PDRUtils.checkGlESError("ShaderSource(" + sid + ") (" + file + ")");
		GLES30.glCompileShader(sid);
		PDRUtils.checkGlESError("CompileShader(" + sid + ") (" + file + ")");

		if (GLES30.glGetShaderi(sid, GLES30.GL_COMPILE_STATUS) == GLES30.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GLES30.glGetShaderInfoLog(sid, 1024));
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
		GlobalLogger.log("Cleaning up: " + file + " (" + sid + ")");

		if (sid == -1)
			return;

		GLES30.glDeleteShader(sid);
		sid = -1;
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
		case "comp":
			PDRUtils.throwGLESError("Cannot load Compute/Geo shader using GLES");
		}
		PDRUtils.throwGLESError("Unknown shader type: " + type);
		return -1;
	}

}
