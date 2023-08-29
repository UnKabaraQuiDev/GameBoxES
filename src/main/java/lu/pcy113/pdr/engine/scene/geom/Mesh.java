package lu.pcy113.pdr.engine.scene.geom;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.impl.Bindable;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Mesh implements Cleanupable {
	
	protected int numVertices;
	protected int vaoId;
	protected List<Integer> vboIdList;
	
	public Mesh(float[] pos, float[] uv, int[] ids) {
		Logger.log();
		
		try(MemoryStack stack = MemoryStack.stackPush()) {
			numVertices = ids.length;
			vboIdList = new ArrayList<>();
			
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			
			// Pos vbo
			int vboId = GL30.glGenBuffers();
			vboIdList.add(vboId);
			FloatBuffer posBuffer = stack.callocFloat(pos.length);
			posBuffer.put(pos).flip();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, posBuffer, GL30.GL_STATIC_DRAW);
			GL30.glEnableVertexAttribArray(0);
			GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
			
			// UV vbo
			vboId = GL30.glGenBuffers();
			vboIdList.add(vboId);
			FloatBuffer uvBuffer = MemoryUtil.memAllocFloat(uv.length);
			uvBuffer.put(uv).flip();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, uvBuffer, GL30.GL_STATIC_DRAW);
			GL30.glEnableVertexAttribArray(1);
			GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 0, 0);
			
			// Index vbo
			vboId = GL30.glGenBuffers();
			vboIdList.add(vboId);
			IntBuffer idBuffer = stack.callocInt(ids.length);
			idBuffer.put(ids).flip();
			GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, idBuffer, GL30.GL_STATIC_DRAW);
			
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
			
			/*if(posBuffer != null)
				MemoryUtil.memFree(posBuffer);
			if(posBuffer != null)
				MemoryUtil.memFree(colBuffer);
			if(posBuffer != null)
				MemoryUtil.memFree(idBuffer);*/
		}
	}
	
	public void bind(UniformsMap uniforms) {
		GL30.glBindVertexArray(vaoId);
		//GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vaoId);
	}
	public void unbind() {
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		vboIdList.forEach(GL30::glDeleteBuffers);
		GL30.glDeleteVertexArrays(vaoId);
	}
	
	public int getNumVertices() {return numVertices;}
	public int getVaoId() {return vaoId;}

}
