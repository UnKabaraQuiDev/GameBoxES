package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengl.ARBIndirectParameters;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum BufferType implements GLConstant {

	ARRAY(GL_W.GL_ARRAY_BUFFER), ELEMENT_ARRAY(GL_W.GL_ELEMENT_ARRAY_BUFFER), PIXEL_PACK(GL_W.GL_PIXEL_PACK_BUFFER), PIXEL_UNPACK(GL_W.GL_PIXEL_UNPACK_BUFFER), TRANSFORM_FEEDBACK(GL_W.GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(GL_W.GL_UNIFORM_BUFFER), COPY_READ(GL_W.GL_COPY_READ_BUFFER), COPY_WRITE(GL_W.GL_COPY_WRITE_BUFFER), PARAMETER_ARB(ARBIndirectParameters.GL_PARAMETER_BUFFER_ARB);

	private int glId;

	private BufferType(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
