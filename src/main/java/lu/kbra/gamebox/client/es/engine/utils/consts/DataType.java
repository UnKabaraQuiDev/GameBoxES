package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum DataType implements GLConstant {

	// Base type
	BYTE(GLES30.GL_BYTE, null), SHORT(GLES30.GL_SHORT, null), INT(GLES30.GL_INT, null), HALF_FLOAT(GLES30.GL_HALF_FLOAT, null), FLOAT(GLES30.GL_FLOAT, null),

	// Unsigned base types
	UBYTE(GLES30.GL_UNSIGNED_BYTE, BYTE), USHORT(GLES30.GL_UNSIGNED_SHORT, SHORT), UINT(GLES30.GL_UNSIGNED_INT, INT),

	USHORT565(GLES30.GL_UNSIGNED_SHORT_5_6_5, USHORT), USHORT4444(GLES30.GL_UNSIGNED_SHORT_4_4_4_4, USHORT), USHORT5551(GLES30.GL_UNSIGNED_SHORT_5_5_5_1, USHORT), UINT2101010REV(GLES30.GL_UNSIGNED_INT_2_10_10_10_REV, UINT),
	UINT248(GLES30.GL_UNSIGNED_INT_24_8, UINT), UINT10F11F11FREV(GLES30.GL_UNSIGNED_INT_10F_11F_11F_REV, UINT), UINT5999REV(GLES30.GL_UNSIGNED_INT_5_9_9_9_REV, UINT),

	FLOAT32UINT248REV(GLES30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV, null);

	private int glId;
	private DataType base;

	private DataType(int glId, DataType base) {
		this.glId = glId;
	}

	@Override
	public int getGlId() {
		return glId;
	}

	public DataType getBase() {
		return base;
	}

	public boolean isBase() {
		return base == null;
	}

}
