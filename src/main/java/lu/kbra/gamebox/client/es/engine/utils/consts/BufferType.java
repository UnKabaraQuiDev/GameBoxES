package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengl.ARBIndirectParameters;
import org.lwjgl.opengles.GLES30;

public enum BufferType implements GLConstant {

	ARRAY(GLES30.GL_ARRAY_BUFFER), ELEMENT_ARRAY(GLES30.GL_ELEMENT_ARRAY_BUFFER), PIXEL_PACK(GLES30.GL_PIXEL_PACK_BUFFER), PIXEL_UNPACK(GLES30.GL_PIXEL_UNPACK_BUFFER), TRANSFORM_FEEDBACK(GLES30.GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(GLES30.GL_UNIFORM_BUFFER), COPY_READ(GLES30.GL_COPY_READ_BUFFER), COPY_WRITE(GLES30.GL_COPY_WRITE_BUFFER), PARAMETER_ARB(ARBIndirectParameters.GL_PARAMETER_BUFFER_ARB);

	private int glId;

	private BufferType(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
