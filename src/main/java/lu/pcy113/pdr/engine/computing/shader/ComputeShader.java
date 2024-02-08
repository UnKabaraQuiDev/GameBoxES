package lu.pcy113.pdr.engine.computing.shader;

import org.joml.Vector3i;

import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.impl.AbstractShader;

public abstract class ComputeShader extends AbstractShader {

	public ComputeShader(String name, Vector3i batchSize, Vector3i execSize, ShaderPart part) {
		super(name, part);
	}
	
}
