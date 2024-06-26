package lu.kbra.gamebox.client.es.engine.impl.shader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import lu.kbra.gamebox.client.es.engine.graph.shader.part.FragmentShaderPart;
import lu.kbra.gamebox.client.es.engine.graph.shader.part.VertexShaderPart;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W_GL40;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public abstract class AbstractShaderPart implements UniqueID, Cleanupable {

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

		this.sid = GL_W.glCreateShader(type);
		PDRUtils.checkGL_WError("CreateShader(" + type + ") (" + file + ")");
		GL_W.glShaderSource(sid, PCUtils.readStringFile(file).replace("{version}", GL_W.WRAPPER instanceof GL_W_GL40 ? "400" : "300 es"));
		PDRUtils.checkGL_WError("ShaderSource(" + sid + ") (" + file + ")");
		GL_W.glCompileShader(sid);
		PDRUtils.checkGL_WError("CompileShader(" + sid + ") (" + file + ")");

		if (GL_W.glGetShaderi(sid, GL_W.GL_COMPILE_STATUS) == GL_W.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GL_W.glGetShaderInfoLog(sid, 1024));
			cleanup();
			throw new IllegalStateException(file + "(" + sid + "): Failed to compile shader!");
		} else {
			GlobalLogger.log(Level.INFO, "ShaderPart " + file + " (" + sid + ") (" + type + ") created successfully");
		}
	}

	public static AbstractShaderPart load(String file) {
		int type = shaderType(file.substring(file.lastIndexOf(".") + 1));
		if (type == GL_W.GL_VERTEX_SHADER) {
			return new VertexShaderPart(file);
		} else if (type == GL_W.GL_FRAGMENT_SHADER) {
			return new FragmentShaderPart(file);
		} else {
			PDRUtils.throwGLESError("Unknown shader part type: " + file);
			return null;
		}
	}

	public boolean recompile() {
		GL_W.glShaderSource(sid, PCUtils.readStringFile(file));
		PDRUtils.checkGL_WError("ShaderSource(" + sid + ") (" + file + ")");
		GL_W.glCompileShader(sid);
		PDRUtils.checkGL_WError("CompileShader(" + sid + ") (" + file + ")");

		if (GL_W.glGetShaderi(sid, GL_W.GL_COMPILE_STATUS) == GL_W.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, file + "> " + GL_W.glGetShaderInfoLog(sid, 1024));
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

		GL_W.glDeleteShader(sid);
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
			return GL_W.GL_VERTEX_SHADER;
		case "frag":
			return GL_W.GL_FRAGMENT_SHADER;
		case "geo":
		case "comp":
			PDRUtils.throwGLESError("Cannot load Compute/Geo shader using GLES");
		}
		PDRUtils.throwGLESError("Unknown shader type: " + type);
		return -1;
	}

}
