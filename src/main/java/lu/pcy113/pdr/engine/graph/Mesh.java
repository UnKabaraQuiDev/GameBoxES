package lu.pcy113.pdr.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Mesh implements Cleanupable {
	
	private int numVertices;
	private int vaoId;
	private List<Integer> vboIdList;
	
	public Mesh(float[] pos, float[] col, int[] ids) {
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
			
			// Color vbo
			vboId = GL30.glGenBuffers();
			vboIdList.add(vboId);
			FloatBuffer colBuffer = stack.callocFloat(col.length);
			colBuffer.put(col).flip();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, colBuffer, GL30.GL_STATIC_DRAW);
			GL30.glEnableVertexAttribArray(1);
			GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 0, 0);
			
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

	@Override
	public void cleanup() {
		Logger.log();
		
		vboIdList.forEach(GL30::glDeleteBuffers);
		GL30.glDeleteVertexArrays(vaoId);
	}
	
	public int getNumVertices() {return numVertices;}
	public int getVaoId() {return vaoId;}
	
}
