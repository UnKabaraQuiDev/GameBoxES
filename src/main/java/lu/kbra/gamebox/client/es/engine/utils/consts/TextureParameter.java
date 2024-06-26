package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TextureParameter implements GLConstant {

	// DEPTH_STENCIL_MODE(GL_W.GL_DEPTH_STENCIL_TEXTURE_MODE),
	BASE_LEVEL(GL_W.GL_TEXTURE_BASE_LEVEL),

	// Compare
	COMPARE_FUNC(GL_W.GL_TEXTURE_COMPARE_FUNC), COMPARE_MODE(GL_W.GL_TEXTURE_COMPARE_MODE),

	// Interpolation filters
	MIN_FILTER(GL_W.GL_TEXTURE_MIN_FILTER), MAG_FILTER(GL_W.GL_TEXTURE_MAG_FILTER),

	// Lod
	MIN_LOD(GL_W.GL_TEXTURE_MIN_LOD), MAX_LOD(GL_W.GL_TEXTURE_MAX_LOD),

	MAX_LEVEL(GL_W.GL_TEXTURE_MAX_LEVEL),

	// Swizzle
	SWIZZLE_R(GL_W.GL_TEXTURE_SWIZZLE_R), SWIZZLE_G(GL_W.GL_TEXTURE_SWIZZLE_G), SWIZZLE_B(GL_W.GL_TEXTURE_SWIZZLE_B), SWIZZLE_A(GL_W.GL_TEXTURE_SWIZZLE_A),

	// Wrap
	WRAP_S(GL_W.GL_TEXTURE_WRAP_S), WRAP_T(GL_W.GL_TEXTURE_WRAP_T), WRAP_R(GL_W.GL_TEXTURE_WRAP_R), WRAP_VERTICAL(GL_W.GL_TEXTURE_WRAP_T), WRAP_HORIZONTAL(GL_W.GL_TEXTURE_WRAP_S), WRAP_DEPTH(GL_W.GL_TEXTURE_WRAP_R);

	private int glId;

	private TextureParameter(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}
