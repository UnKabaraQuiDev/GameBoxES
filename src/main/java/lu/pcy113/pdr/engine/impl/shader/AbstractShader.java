package lu.pcy113.pdr.engine.impl.shader;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.joml.Matrix3f;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public abstract class AbstractShader implements UniqueID, Cleanupable {
	
	protected final String name;
	protected int shaderProgram = -1;
	protected Map<Integer, AbstractShaderPart> parts;
	protected Map<String, Integer> uniforms;
	
	public AbstractShader(String name, AbstractShaderPart... parts) {
		this.name = name;

		this.shaderProgram = GL20.glCreateProgram();
		this.parts = new HashMap<>();
		for (AbstractShaderPart sp : parts) {
			this.parts.put(sp.getType(), sp);
			GL20.glAttachShader(this.shaderProgram, sp.getSid());
		}
		GL20.glLinkProgram(this.shaderProgram);
		
		if (GL20.glGetProgrami(this.shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE, name+"("+shaderProgram+"): "+GL20.glGetProgramInfoLog(this.shaderProgram, 1024));
			this.cleanup();
			return;
		}else if(!GL40.glIsProgram(shaderProgram)) {
			GlobalLogger.log(Level.SEVERE, name+"("+shaderProgram+"): Is not a GL Shader Program!");
			this.cleanup();
			return;
		}else {
			GlobalLogger.log(Level.INFO, "ShaderProgram " + name + " (" + shaderProgram + ") created successfully");
		}

		this.bind();
		this.uniforms = new HashMap<>();

		this.createUniforms();
		this.unbind();
	}
	
	public abstract void createUniforms();

	public void setUniform(String key, Object value) {
		if (value instanceof Integer) {
			GL20.glUniform1i(this.getUniform(key), (int) value);
		} else if (value instanceof Float) {
			GL20.glUniform1f(this.getUniform(key), (float) value);
		} else if (value instanceof Matrix4f) {
			GL20.glUniformMatrix4fv(this.getUniform(key), false, ((Matrix4f) value).get(new float[4 * 4]));
		} else if (value instanceof Vector3f) {
			GL20.glUniform3f(this.getUniform(key), ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);
		} else if (value instanceof Vector3i) {
			GL20.glUniform3i(this.getUniform(key), ((Vector3i) value).x, ((Vector3i) value).y, ((Vector3i) value).z);
		} else if (value instanceof Vector4f) {
			GL20.glUniform4f(this.getUniform(key), ((Vector4f) value).x, ((Vector4f) value).y, ((Vector4f) value).z, ((Vector4f) value).w);
		} else if (value instanceof Double) {
			GL40.glUniform1d(this.getUniform(key), (double) value);
		} else if (value instanceof Character) {
			// System.out.println("is char: " + value + " > " + ((char) value) + " > " +
			// (Integer.valueOf((char) value)));
			assert value instanceof Character : "Trying to set char uniform";
			this.setUniform(key, Integer.valueOf((char) value));
		} else if (value instanceof Vector2f) {
			GL20.glUniform2f(this.getUniform(key), ((Vector2f) value).x, ((Vector2f) value).y);
		} else if (value instanceof Vector2i) {
			GL20.glUniform2i(this.getUniform(key), ((Vector2i) value).x, ((Vector2i) value).y);
		} else if (value instanceof Matrix3f) {
			GL20.glUniformMatrix3fv(this.getUniform(key), false, ((Matrix3f) value).get(new float[3 * 3]));
		} else if (value instanceof Matrix3x2f) {
			GL21.glUniformMatrix3x2fv(this.getUniform(key), false, ((Matrix3x2f) value).get(new float[3 * 2]));
		}
	}
	
	public int getUniform(String name) {
		if (!this.hasUniform(name))
			if (!this.createUniform(name))
				return -1;
		return this.uniforms.get(name);
	}

	public boolean hasUniform(String name) {
		return this.uniforms.containsKey(name);
	}

	public boolean createUniform(String name) {
		int loc = GL20.glGetUniformLocation(this.shaderProgram, name);
		if (loc != -1) {
			this.uniforms.put(name, loc);
			return true;
		} else {
			GlobalLogger.log(Level.SEVERE, "Could not get Uniform location for: " + name + " in program " + this.name + " (" + this.shaderProgram + ") (" + GL11.glGetError() + ")");
		}
		return false;
	}

	public void bind() {
		GL20.glUseProgram(this.shaderProgram);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	@Override
	public void cleanup() {
		if (this.shaderProgram != -1) {
			this.parts.values().forEach(AbstractShaderPart::cleanup);
			GL20.glDeleteProgram(this.shaderProgram);
			this.shaderProgram = -1;
		}
	}

	@Override
	public String getId() {
		return this.name;
	}

	public Map<Integer, AbstractShaderPart> getParts() {
		return this.parts;
	}

	public Map<String, Integer> getUniforms() {
		return this.uniforms;
	}
	
}
