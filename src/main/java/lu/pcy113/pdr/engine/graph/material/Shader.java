package lu.pcy113.pdr.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.utils.Logger;

public abstract class Shader implements UniqueID, Cleanupable {
	
	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String VIEW_MATRIX = "viewMatrix";
	public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
	public static final String VIEW_POSITION = "viewPos";
	
	protected final String name;
	protected int shaderProgram = -1;
	protected Map<Integer, ShaderPart> parts;
	protected Map<String, Integer> uniforms;
	
	public Shader(String name, ShaderPart... parts) {
		this.name = name;
		
		this.shaderProgram = GL40.glCreateProgram();
		this.parts = new HashMap<>();
		for(ShaderPart sp : parts) {
			this.parts.put(sp.getType(), sp);
			GL40.glAttachShader(shaderProgram, sp.getSid());
		}
		GL40.glLinkProgram(shaderProgram);
		
		if(GL40.glGetProgrami(shaderProgram, GL40.GL_LINK_STATUS) == GL40.GL_FALSE) {
			Logger.log(Level.SEVERE, GL40.glGetProgramInfoLog(shaderProgram, 1024));
			cleanup();
		}
		
		bind();
		this.uniforms = new HashMap<>();
		createUniforms();
		unbind();
	}
	
	public abstract void createUniforms();
	
	public void setUniform(String key, Object value) {
		//System.err.println(value.getClass().getName());
		
		if(value instanceof Integer) {
			GL40.glUniform1i(getUniform(key), (int) value);
		}else if(value instanceof Float) {
			GL40.glUniform1f(getUniform(key), (float) value);
		}else if(value instanceof Matrix4f) {
			GL40.glUniformMatrix4fv(getUniform(key), false, ((Matrix4f) value).get(new float[4*4]));
		}else if(value instanceof Vector3f) {
			GL40.glUniform3f(getUniform(key), ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);
		}else if(value instanceof Vector3i) {
			GL40.glUniform3i(getUniform(key), ((Vector3i) value).x, ((Vector3i) value).y, ((Vector3i) value).z);
		}else if(value instanceof Vector4f) {
			GL40.glUniform4f(getUniform(key), ((Vector4f) value).x, ((Vector4f) value).y, ((Vector4f) value).z, ((Vector4f) value).w);
		}else if(value instanceof Double) {
			GL40.glUniform1d(getUniform(key), (double) value);
		}
	}
	public int getUniform(String name) {
		if(!hasUniform(name))
			uniforms.put(name, GL40.glGetUniformLocation(shaderProgram, name));
		return uniforms.get(name);
	}
	public boolean hasUniform(String name) {
		return uniforms.containsKey(name);
	}
	
	public void bind() {
		GL40.glUseProgram(shaderProgram);
	}
	public void unbind() {
		GL40.glUseProgram(0);
	}
	
	@Override
	public void cleanup() {
		if(shaderProgram != -1) {
			parts.values().forEach(ShaderPart::cleanup);
			GL40.glDeleteProgram(shaderProgram);
			shaderProgram = -1;
		}
	}
	
	@Override
	public String getId() {return name;}
	public Map<Integer, ShaderPart> getParts() {return parts;}
	public Map<String, Integer> getUniforms() {return uniforms;}

}
