package lu.pcy113.pdr.engine.graph.shader;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.utils.Logger;

public class ShaderProgram implements Cleanupable {
	
	private final int programId;
	
	public ShaderProgram(List<ShaderModuleData> modules) {
		Logger.log();
		
		programId = GL30.glCreateProgram();
		if(programId == 0) {
			throw new RuntimeException("Could not create ShaderProgram");
		}
		
		List<Integer> shaderModules = new ArrayList<>();
		modules.forEach((s) -> shaderModules.add(createShader(FileUtils.getResource("shaders/"+s.getFile()), s.getType())));
		
		link(shaderModules);
	}
	
	public void bind() {
		Logger.log();
		
		GL30.glUseProgram(programId);
	}
	public void unbind() {
		Logger.log();
		
		GL30.glUseProgram(0);
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		unbind();
		if(programId != 0) {
			GL30.glDeleteProgram(programId);
		}
	}
	
	private void link(List<Integer> shaderModules) {
		Logger.log();
		
		GL30.glLinkProgram(programId);
		if(GL30.glGetProgrami(programId, GL30.GL_LINK_STATUS) == 0) {
			throw new RuntimeException("Error linking Shader code: "+GL30.glGetProgramInfoLog(programId, 1024));
		}
		
		shaderModules.forEach((s) -> GL30.glDetachShader(programId, s));
		shaderModules.forEach(GL30::glDeleteShader);
	}
	
	public void validate() {
		Logger.log();
		
		GL30.glValidateProgram(programId);
		if(GL30.glGetProgrami(programId, GL30.GL_VALIDATE_STATUS) == 0) {
			throw new RuntimeException("Error validating Shader code: "+GL30.glGetProgramInfoLog(programId, 1024));
		}
	}
	
	protected int createShader(String code, int type) {
		Logger.log();
		
		int shaderId = GL30.glCreateShader(type);
		if(shaderId == 0) {
			throw new RuntimeException("Error creating shader. Type: "+type);
		}
		
		GL30.glShaderSource(shaderId, code);
		GL30.glCompileShader(shaderId);
		
		if(GL30.glGetShaderi(shaderId, GL30.GL_COMPILE_STATUS) == 0) {
			throw new RuntimeException("Error compiling Shader code: "+GL30.glGetShaderInfoLog(shaderId, 1024));
		}
		
		GL30.glAttachShader(programId, shaderId);
		
		return shaderId;
	}
	
	public int getProgramId() {return programId;}
	
}
