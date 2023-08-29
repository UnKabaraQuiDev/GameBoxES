package lu.pcy113.pdr.engine.scene.geom;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.graph.UniformsMap;

public class MeshInstance extends Mesh {
	
	protected FloatBuffer modelViewBuffer;
	
	public MeshInstance(int count, float[] pos, float[] uv, int[] ids) {
		super(pos, uv, ids);
		
		int modelViewVBO = GL30.glGenBuffers();
		vboIdList.add(modelViewVBO);
		modelViewBuffer = MemoryUtil.memAllocFloat(count * 4*4);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, modelViewVBO);
		GL33.glVertexAttribPointer(3, 4, GL30.GL_FLOAT_MAT4, false, 1, modelViewBuffer);
		GL33.glVertexAttribDivisor(3, 1);
	}
	
	@Override
	public void bind(UniformsMap uniforms) {
		super.bind(uniforms);
		GL30.glEnableVertexAttribArray(3);
	}
	
	public void unbind() {
		super.unbind();
		GL30.glDisableVertexAttribArray(3);
	}

}
