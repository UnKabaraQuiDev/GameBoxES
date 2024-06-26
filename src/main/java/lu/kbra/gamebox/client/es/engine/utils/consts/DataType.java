package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum DataType implements GLConstant {

	// Base type
	BYTE(GL_W.GL_BYTE, null), SHORT(GL_W.GL_SHORT, null), INT(GL_W.GL_INT, null), HALF_FLOAT(GL_W.GL_HALF_FLOAT, null), FLOAT(GL_W.GL_FLOAT, null),

	// Unsigned base types
	UBYTE(GL_W.GL_UNSIGNED_BYTE, BYTE), USHORT(GL_W.GL_UNSIGNED_SHORT, SHORT), UINT(GL_W.GL_UNSIGNED_INT, INT),

	USHORT565(GL_W.GL_UNSIGNED_SHORT_5_6_5, USHORT), USHORT4444(GL_W.GL_UNSIGNED_SHORT_4_4_4_4, USHORT), USHORT5551(GL_W.GL_UNSIGNED_SHORT_5_5_5_1, USHORT), UINT2101010REV(GL_W.GL_UNSIGNED_INT_2_10_10_10_REV, UINT),
	UINT248(GL_W.GL_UNSIGNED_INT_24_8, UINT), UINT10F11F11FREV(GL_W.GL_UNSIGNED_INT_10F_11F_11F_REV, UINT), UINT5999REV(GL_W.GL_UNSIGNED_INT_5_9_9_9_REV, UINT),

	FLOAT32UINT248REV(GL_W.GL_FLOAT_32_UNSIGNED_INT_24_8_REV, null);

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
