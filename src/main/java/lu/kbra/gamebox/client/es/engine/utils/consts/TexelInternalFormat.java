package lu.kbra.gamebox.client.es.engine.utils.consts;

import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;

public enum TexelInternalFormat implements GLConstant {

	// Base Internal Formats
	RED(GL_W.GL_RED, null), RG(GL_W.GL_RG, null), RGB(GL_W.GL_RGB, null), RGBA(GL_W.GL_RGBA, null),

	DEPTH_COMPONENT(GL_W.GL_DEPTH_COMPONENT, null), DEPTH_COMPONENT16(GL_W.GL_DEPTH_COMPONENT16, DEPTH_COMPONENT), DEPTH_COMPONENT24(GL_W.GL_DEPTH_COMPONENT24, DEPTH_COMPONENT),
	DEPTH_COMPONENT32F(GL_W.GL_DEPTH_COMPONENT32F, DEPTH_COMPONENT),

	DEPTH_STENCIL(GL_W.GL_DEPTH_STENCIL, null), DEPTH24_STENCIL8(GL_W.GL_DEPTH24_STENCIL8, DEPTH_STENCIL), DEPTH32F_STENCIL8(GL_W.GL_DEPTH32F_STENCIL8, DEPTH_STENCIL),

	STENCIL_INDEX8(GL_W.GL_STENCIL_INDEX8, null),

	// Sized Internal Formats
	R8(GL_W.GL_R8, RED), R8_SNORM(GL_W.GL_R8_SNORM, RED), RG8(GL_W.GL_RG8, RG), RG8_SNORM(GL_W.GL_RG8_SNORM, RG), RGB8(GL_W.GL_RGB8, RGB),
	RGB8_SNORM(GL_W.GL_RGB8_SNORM, RGB), RGBA4(GL_W.GL_RGBA4, RGBA), RGB5_A1(GL_W.GL_RGB5_A1, RGBA), RGBA8(GL_W.GL_RGBA8, RGBA), RGBA8_SNORM(GL_W.GL_RGBA8_SNORM, RGBA), RGB10A2(GL_W.GL_RGB10_A2, RGBA),
	RGB10A2UI(GL_W.GL_RGB10_A2UI, RGBA),  // RGBA16_SNORM(GL_W.GL_RGBA16_SNORM),
	SRGB8(GL_W.GL_SRGB8, RGB), SRGB8A8(GL_W.GL_SRGB8_ALPHA8, RGBA), R16F(GL_W.GL_R16F, RED), RG16F(GL_W.GL_RG16F, RG), RGB16F(GL_W.GL_RGB16F, RGB), RGBA16F(GL_W.GL_RGBA16F, RGBA), R32F(GL_W.GL_R32F, RED),
	RG32F(GL_W.GL_RG32F, RG), RGB32F(GL_W.GL_RGB32F, RGB), RGBA32F(GL_W.GL_RGBA32F, RGBA), R11FG11FB10F(GL_W.GL_R11F_G11F_B10F, RGB), RGB9E5(GL_W.GL_RGB9_E5, RGB), R8I(GL_W.GL_R8I, RED), R8UI(GL_W.GL_R8UI, RED),
	R16I(GL_W.GL_R16I, RED), R16UI(GL_W.GL_R16UI, RED), R32I(GL_W.GL_R32I, RED), R32UI(GL_W.GL_R32UI, RED), RG8I(GL_W.GL_RG8I, RG), RG8UI(GL_W.GL_RG8UI, RG), RG16I(GL_W.GL_RG16I, RG), RG16UI(GL_W.GL_RG16UI, RG),
	RG32I(GL_W.GL_RG32I, RG), RG32UI(GL_W.GL_RG32UI, RG), RGB8I(GL_W.GL_RGB8I, RGB), RGB8UI(GL_W.GL_RGB8UI, RGB), RGB16I(GL_W.GL_RGB16I, RGB), RGB16UI(GL_W.GL_RGB16UI, RGB), RGB32I(GL_W.GL_RGB32I, RGB),
	RGB32UI(GL_W.GL_RGB32UI, RGB), RGBA8I(GL_W.GL_RGBA8I, RGBA), RGBA8UI(GL_W.GL_RGBA8UI, RGBA), RGBA16I(GL_W.GL_RGBA16I, RGBA), RGBA16UI(GL_W.GL_RGBA16UI, RGBA), RGBA32I(GL_W.GL_RGBA32I, RGBA),
	RGBA32UI(GL_W.GL_RGBA32UI, RGBA);

	private int glId;
	private TexelInternalFormat base;

	private TexelInternalFormat(int id, TexelInternalFormat base) {
		this.glId = id;
		this.base = base;
	}

	@Override
	public int getGlId() {
		return glId;
	}

	public TexelInternalFormat getBase() {
		return base;
	}

	public boolean isBase() {
		return base == null;
	}

	public static boolean isStencil(TexelInternalFormat format) {
		if (format == null)
			return false;
		if (format.equals(DEPTH_STENCIL))
			return true;
		return false;
	}

	public static boolean isColor(TexelInternalFormat format) {
		if (format == null)
			return false;
		if (format.equals(DEPTH_COMPONENT) || format.equals(DEPTH_STENCIL))
			return false;
		return true;
	}

	public static boolean isDepth(TexelInternalFormat format) {
		if (format == null)
			return false;
		if (format.isBase() ? format.equals(DEPTH_COMPONENT) : format.getBase().equals(DEPTH_COMPONENT) || format.equals(DEPTH_STENCIL))
			return true;
		return false;
	}

	public static boolean isInteger(TexelInternalFormat format) {
		if (format == null)
			return false;
		if (format.name().endsWith("I"))
			return true;
		return false;
	}

}
