package lu.pcy113.pdr.engine.computing.shader;

import org.joml.Vector3i;

import lu.pcy113.pdr.engine.impl.shader.AbstractShader;

public abstract class ComputeShader extends AbstractShader {

	public ComputeShader(String name, Vector3i batchSize, Vector3i execSize, ComputeShaderPart part) {
		super(name, part);
	}
	
}
