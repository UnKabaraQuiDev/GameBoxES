package lu.pcy113.pdr.engine.graph;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import lu.pcy113.pdr.utils.Logger;

public class UniformsMap {
	
	private int programId;
	private Map<String, Integer> uniforms;
	
	public UniformsMap(int programId) {
		Logger.log();
		
		this.programId = programId;
		uniforms = new HashMap<>();
	}
	
	public void createUniform(String uniformName) {
		Logger.log();
		
		int uniformLocation = GL20.glGetUniformLocation(programId, uniformName);
		if(uniformLocation < 0)
			throw new RuntimeException("Could not find uniform "+uniformName+" in shader program "+programId+".");
		uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String name, Vector3f value) {
		Logger.log();
		
		/*try(MemoryStack stack = MemoryStack.stackPush()) {
			Integer loc = uniforms.get(name);
			if(loc == null)
				throw new RuntimeException("Could not find uniform "+name+".");*/
			GL20.glUniform3f(uniforms.get(name), value.x, value.y, value.z);
		//}
	}
	
	public void setUniform(String name, Vector4f value) {
		Logger.log();
		
		GL20.glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String name, boolean value) {
		Logger.log();
		
		GL20.glUniform1f(uniforms.get(name), value ? 1 : 0);
	}
	
	public void setUniform(String name, int value) {
		Logger.log();
		
		GL20.glUniform1i(uniforms.get(name), value);
	}
	
	public void setUniform(String name, float value) {
		Logger.log();
		
		GL20.glUniform1f(uniforms.get(name), value);
	}
	
	public void setUniform(String name, Matrix4f value) {
		Logger.log();
		
		try(MemoryStack stack = MemoryStack.stackPush()) {
			Integer loc = uniforms.get(name);
			if(loc == null)
				throw new RuntimeException("Could not find uniform "+name+".");
			GL20.glUniformMatrix4fv(loc.intValue(), false, value.get(stack.mallocFloat(16)));
		}
	}
	
}
