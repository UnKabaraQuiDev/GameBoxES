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
import org.lwjgl.system.MemoryStack;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pclib.Pair;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.Property;

public abstract class AbstractShader implements UniqueID, Cleanupable {

	protected final String name;
	protected int shaderProgram = -1;
	protected Map<Integer, AbstractShaderPart> parts;
	protected Map<String, Pair<Property<Object>, Integer>> uniforms;

	public AbstractShader(String name, AbstractShaderPart... parts) {
		this.name = name;

		this.shaderProgram = GL20.glCreateProgram();
		PDRUtils.checkGlError("CreateProgram() (" + name + ")");
		if (this.shaderProgram == -1) {
			PDRUtils.throwGLError(name + ": Failed to create shader program!");
		}
		this.parts = new HashMap<>();
		for (AbstractShaderPart sp : parts) {
			this.parts.put(sp.getType(), sp);
			GL20.glAttachShader(this.shaderProgram, sp.getSid());
			PDRUtils.checkGlError("AttachShader("+this.shaderProgram+")");
		}
		GL20.glLinkProgram(this.shaderProgram);
		PDRUtils.checkGlError("LinkProgram("+this.shaderProgram+")");

		if (GL20.glGetProgrami(this.shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			GlobalLogger.log(Level.SEVERE,
					name + "(" + shaderProgram + "): " + GL20.glGetProgramInfoLog(this.shaderProgram, 1024));
			this.cleanup();
			throw new IllegalStateException(name + "(" + shaderProgram + "): Failed to link shader program!");
		} else if (!GL40.glIsProgram(shaderProgram)) {
			this.cleanup();
			throw new IllegalStateException(name + "(" + shaderProgram + "): Is not a GL Shader Program!");
		} else {
			GlobalLogger.log(Level.INFO, "ShaderProgram " + name + " (" + shaderProgram + ") created successfully");
		}

		this.bind();
		this.uniforms = new HashMap<>();

		this.createUniforms();
		this.unbind();
	}

	public void recompile() {
		for (AbstractShaderPart part : parts.values()) {
			part.recompile();
		}
		for (AbstractShaderPart sp : parts.values()) {
			GL20.glAttachShader(this.shaderProgram, sp.getSid());
			PDRUtils.checkGlError("AttachShader("+this.shaderProgram+")");
		}
		GL20.glLinkProgram(this.shaderProgram);
		PDRUtils.checkGlError("LinkProgram("+this.shaderProgram+")");
	}
	
	public abstract void createUniforms();

	public void setUniform(String key, Object value) {
		GameEngine.DEBUG.start("r_uniforms_bind_single_lookup");
		Pair<Property<Object>, Integer> unif = uniforms.get(key);
		if (unif == null) {
			return;
		}
		Property<Object> prop = unif.hasKey() ? unif.getKey() : new Property<Object>();
		prop.setValue(value);
		if (!prop.isChanged()) {
			return;
		}
		GameEngine.DEBUG.end("r_uniforms_bind_single_lookup");

		GameEngine.DEBUG.start("r_uniforms_bind_single_bind");
		if (value instanceof Integer) {
			GL20.glUniform1i(unif.getValue(), (int) value);
		} else if (value instanceof Float) {
			GL20.glUniform1f(unif.getValue(), (float) value);
		} else if (value instanceof Matrix4f) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				GL20.glUniformMatrix4fv(unif.getValue(), false, ((Matrix4f) value).get(stack.mallocFloat(16)));
			}
		} else if (value instanceof Vector3f) {
			GL20.glUniform3f(unif.getValue(), ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);
		} else if (value instanceof Vector3i) {
			GL20.glUniform3i(unif.getValue(), ((Vector3i) value).x, ((Vector3i) value).y, ((Vector3i) value).z);
		} else if (value instanceof Vector4f) {
			GL20.glUniform4f(unif.getValue(), ((Vector4f) value).x, ((Vector4f) value).y, ((Vector4f) value).z,
					((Vector4f) value).w);
		} else if (value instanceof Double) {
			GL40.glUniform1d(unif.getValue(), (double) value);
		} else if (value instanceof Character) {
			// System.out.println("is char: " + value + " > " + ((char) value) + " > " +
			// (Integer.valueOf((char) value)));
			assert value instanceof Character : "Trying to set char uniform";
			this.setUniform(key, Integer.valueOf((char) value));
		} else if (value instanceof Vector2f) {
			GL20.glUniform2f(unif.getValue(), ((Vector2f) value).x, ((Vector2f) value).y);
		} else if (value instanceof Vector2i) {
			GL20.glUniform2i(unif.getValue(), ((Vector2i) value).x, ((Vector2i) value).y);
		} else if (value instanceof Matrix3f) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				GL20.glUniformMatrix3fv(unif.getValue(), false, ((Matrix3f) value).get(stack.mallocFloat(9)));
			}
		} else if (value instanceof Matrix3x2f) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				GL21.glUniformMatrix3x2fv(unif.getValue(), false, ((Matrix3x2f) value).get(stack.mallocFloat(6)));
			}
		}
		GameEngine.DEBUG.end("r_uniforms_bind_single_bind");
	}

	public int getUniformLocation(String name) {
		if (!this.hasUniform(name))
			if (!this.createUniform(name))
				return -1;

		return this.uniforms.get(name).getValue();
	}

	public boolean hasUniform(String name) {
		return this.uniforms.containsKey(name);
	}

	public boolean createUniform(String name) {
		int loc = GL20.glGetUniformLocation(this.shaderProgram, name);
		PDRUtils.checkGlError();

		if (loc != -1) {
			this.uniforms.put(name, new Pair<>(new Property<>(), loc));
			return true;
		} else {
			GlobalLogger.log(Level.SEVERE, "Could not get Uniform location for: " + name + " in program " + this.name
					+ " (" + this.shaderProgram + ") (" + GL11.glGetError() + ")");
		}

		return false;
	}

	public void bind() {
		if (this.shaderProgram == -1)
			System.out.println("Shader program is -1");
		GL20.glUseProgram(this.shaderProgram);
		PDRUtils.checkGlError("UseProgram(" + shaderProgram + ") (" + name + ")");
	}

	public void unbind() {
		GL20.glUseProgram(0);
		PDRUtils.checkGlError("UseProgram(0) (" + name + ")");
	}

	@Override
	public void cleanup() {
		// GlobalLogger.log();

		if (this.shaderProgram != -1) {
			this.parts.values().forEach(AbstractShaderPart::cleanup);
			GL20.glDeleteProgram(this.shaderProgram);
			PDRUtils.checkGlError("DeleteProgram(" + shaderProgram + ") (" + name + ")");
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

	public Map<String, Pair<Property<Object>, Integer>> getUniforms() {
		return uniforms;
	}

}
