package lu.pcy113.pdr.engine.graph.material;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.utils.Logger;

public abstract class Shader implements UniqueID, Cleanupable {
	
	public static final String PROJECTION_MATRIX = "projectionMatrix";
	public static final String VIEW_MATRIX = "viewMatrix";
	public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
	
	protected final String name;
	protected final int shaderProgram;
	protected Map<Integer, ShaderPart> parts;
	protected Map<String, Integer> uniforms;
	
	public Shader(String name, ShaderPart... parts) {
		this.name = name;
		
		this.shaderProgram = GL30.glCreateProgram();
		this.parts = new HashMap<>();
		for(ShaderPart sp : parts) {
			this.parts.put(sp.getType(), sp);
			GL30.glAttachShader(shaderProgram, sp.getSid());
		}
		GL30.glLinkProgram(shaderProgram);
		
		if(GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
			Logger.log(Level.SEVERE, GL30.glGetProgramInfoLog(shaderProgram, 1024));
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
			GL30.glUniform1i(getUniform(key), (int) value);
		}else if(value instanceof Float) {
			GL30.glUniform1f(getUniform(key), (float) value);
		}else if(value instanceof Matrix4f) {
			GL30.glUniformMatrix4fv(getUniform(key), false, ((Matrix4f) value).get(new float[4*4]));
		}else if(value instanceof Vector3f) {
			GL30.glUniform3f(getUniform(key), ((Vector3f) value).x, ((Vector3f) value).y, ((Vector3f) value).z);
		}
	}
	public int getUniform(String name) {
		if(!hasUniform(name))
			uniforms.put(name, GL30.glGetUniformLocation(shaderProgram, name));
		return uniforms.get(name);
	}
	public boolean hasUniform(String name) {
		return uniforms.containsKey(name);
	}
	
	public void bind() {
		GL30.glUseProgram(shaderProgram);
	}
	public void unbind() {
		GL30.glUseProgram(0);
	}
	
	@Override
	public void cleanup() {
		parts.values().forEach(ShaderPart::cleanup);
		//parts.values().forEach(s -> {GL30.glDetachShader(shaderProgram, s.getSid()); s.cleanup();});
		GL30.glDeleteProgram(shaderProgram);
	}
	
	@Override
	public String getId() {return name;}
	public Map<Integer, ShaderPart> getParts() {return parts;}
	public Map<String, Integer> getUniforms() {return uniforms;}

}
