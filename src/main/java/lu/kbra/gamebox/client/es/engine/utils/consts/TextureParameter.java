package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengl.GL40;

public enum TextureParameter implements GLConstant {

	// DEPTH_STENCIL_MODE(GL40.GL_DEPTH_STENCIL_TEXTURE_MODE),
	BASE_LEVEL(GL40.GL_TEXTURE_BASE_LEVEL),

	// Compare
	COMPARE_FUNC(GL40.GL_TEXTURE_COMPARE_FUNC), COMPARE_MODE(GL40.GL_TEXTURE_COMPARE_MODE),

	// Interpolation filters
	MIN_FILTER(GL40.GL_TEXTURE_MIN_FILTER), MAG_FILTER(GL40.GL_TEXTURE_MAG_FILTER),

	// Lod
	LOD_BIAS(GL40.GL_TEXTURE_LOD_BIAS), MIN_LOD(GL40.GL_TEXTURE_MIN_LOD), MAX_LOD(GL40.GL_TEXTURE_MAX_LOD),

	MAX_LEVEL(GL40.GL_TEXTURE_MAX_LEVEL),

	// Swizzle
	SWIZZLE_R(GL40.GL_TEXTURE_SWIZZLE_R), SWIZZLE_G(GL40.GL_TEXTURE_SWIZZLE_G), SWIZZLE_B(GL40.GL_TEXTURE_SWIZZLE_B), SWIZZLE_A(GL40.GL_TEXTURE_SWIZZLE_A),

	// Wrap
	WRAP_S(GL40.GL_TEXTURE_WRAP_S), WRAP_T(GL40.GL_TEXTURE_WRAP_T), WRAP_R(GL40.GL_TEXTURE_WRAP_R), WRAP_VERTICAL(GL40.GL_TEXTURE_WRAP_T), WRAP_HORIZONTAL(GL40.GL_TEXTURE_WRAP_S), WRAP_DEPTH(GL40.GL_TEXTURE_WRAP_R);

	private int glId;

	private TextureParameter(int id) {
		this.glId = id;
	}

	@Override
	public int getGlId() {
		return glId;
	}

}