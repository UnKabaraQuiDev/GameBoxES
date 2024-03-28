package lu.kbra.gamebox.client.es.engine.computing.shader;

import org.joml.Vector3i;

import lu.kbra.gamebox.client.es.engine.computing.shader.part.ComputeShaderPart;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShader;

public abstract class ComputeShader extends AbstractShader {

	public ComputeShader(String name, Vector3i batchSize, Vector3i execSize, ComputeShaderPart part) {
		super(name, part);
	}

}
