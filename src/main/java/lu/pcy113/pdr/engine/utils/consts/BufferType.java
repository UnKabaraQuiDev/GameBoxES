package lu.pcy113.pdr.engine.utils.consts;

import org.lwjgl.opengl.ARBIndirectParameters;
import org.lwjgl.opengl.GL43;

public enum BufferType implements GLConstant {
	
	ARRAY(GL43.GL_ARRAY_BUFFER), ELEMENT_ARRAY(GL43.GL_ELEMENT_ARRAY_BUFFER),
	PIXEL_PACK(GL43.GL_PIXEL_PACK_BUFFER), PIXEL_UNPACK(GL43.GL_PIXEL_UNPACK_BUFFER),
	TRANSFORM_FEEDBACK(GL43.GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(GL43.GL_UNIFORM_BUFFER),
	TEXTURE(GL43.GL_TEXTURE_BUFFER),
	COPY_READ(GL43.GL_COPY_READ_BUFFER), COPY_WRITE(GL43.GL_COPY_WRITE_BUFFER),
	DRAW_INDIRECT(GL43.GL_DRAW_INDIRECT_BUFFER),
	ATOMIC_COUNTER(GL43.GL_ATOMIC_COUNTER_BUFFER),
	DISPATCH_INDIRECT(GL43.GL_DISPATCH_INDIRECT_BUFFER),
	SHADER_STORAGE(GL43.GL_SHADER_STORAGE_BUFFER),
	PARAMETER_ARB(ARBIndirectParameters.GL_PARAMETER_BUFFER_ARB);
	
	private int glId;

	private BufferType(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {return glId;}
	
}
