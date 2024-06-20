package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.lwjgl.opengles.GLES30;

public enum TexelInternalFormat implements GLConstant {

	// Base Internal Formats
	RED(GLES30.GL_RED, null), RG(GLES30.GL_RG, null), RGB(GLES30.GL_RGB, null), RGBA(GLES30.GL_RGBA, null),

	DEPTH_COMPONENT(GLES30.GL_DEPTH_COMPONENT, null), DEPTH_COMPONENT16(GLES30.GL_DEPTH_COMPONENT16, DEPTH_COMPONENT), DEPTH_COMPONENT24(GLES30.GL_DEPTH_COMPONENT24, DEPTH_COMPONENT),
	DEPTH_COMPONENT32F(GLES30.GL_DEPTH_COMPONENT32F, DEPTH_COMPONENT),

	DEPTH_STENCIL(GLES30.GL_DEPTH_STENCIL, null), DEPTH24_STENCIL8(GLES30.GL_DEPTH24_STENCIL8, DEPTH_STENCIL), DEPTH32F_STENCIL8(GLES30.GL_DEPTH32F_STENCIL8, DEPTH_STENCIL),

	STENCIL_INDEX8(GLES30.GL_STENCIL_INDEX8, null),

	// Sized Internal Formats
	R8(GLES30.GL_R8, RED), R8_SNORM(GLES30.GL_R8_SNORM, RED), RG8(GLES30.GL_RG8, RG), RG8_SNORM(GLES30.GL_RG8_SNORM, RG), RGB8(GLES30.GL_RGB8, RGB),
	RGB8_SNORM(GLES30.GL_RGB8_SNORM, RGB), RGBA4(GLES30.GL_RGBA4, RGBA), RGB5_A1(GLES30.GL_RGB5_A1, RGBA), RGBA8(GLES30.GL_RGBA8, RGBA), RGBA8_SNORM(GLES30.GL_RGBA8_SNORM, RGBA), RGB10A2(GLES30.GL_RGB10_A2, RGBA),
	RGB10A2UI(GLES30.GL_RGB10_A2UI, RGBA),  // RGBA16_SNORM(GLES30.GL_RGBA16_SNORM),
	SRGB8(GLES30.GL_SRGB8, RGB), SRGB8A8(GLES30.GL_SRGB8_ALPHA8, RGBA), R16F(GLES30.GL_R16F, RED), RG16F(GLES30.GL_RG16F, RG), RGB16F(GLES30.GL_RGB16F, RGB), RGBA16F(GLES30.GL_RGBA16F, RGBA), R32F(GLES30.GL_R32F, RED),
	RG32F(GLES30.GL_RG32F, RG), RGB32F(GLES30.GL_RGB32F, RGB), RGBA32F(GLES30.GL_RGBA32F, RGBA), R11FG11FB10F(GLES30.GL_R11F_G11F_B10F, RGB), RGB9E5(GLES30.GL_RGB9_E5, RGB), R8I(GLES30.GL_R8I, RED), R8UI(GLES30.GL_R8UI, RED),
	R16I(GLES30.GL_R16I, RED), R16UI(GLES30.GL_R16UI, RED), R32I(GLES30.GL_R32I, RED), R32UI(GLES30.GL_R32UI, RED), RG8I(GLES30.GL_RG8I, RG), RG8UI(GLES30.GL_RG8UI, RG), RG16I(GLES30.GL_RG16I, RG), RG16UI(GLES30.GL_RG16UI, RG),
	RG32I(GLES30.GL_RG32I, RG), RG32UI(GLES30.GL_RG32UI, RG), RGB8I(GLES30.GL_RGB8I, RGB), RGB8UI(GLES30.GL_RGB8UI, RGB), RGB16I(GLES30.GL_RGB16I, RGB), RGB16UI(GLES30.GL_RGB16UI, RGB), RGB32I(GLES30.GL_RGB32I, RGB),
	RGB32UI(GLES30.GL_RGB32UI, RGB), RGBA8I(GLES30.GL_RGBA8I, RGBA), RGBA8UI(GLES30.GL_RGBA8UI, RGBA), RGBA16I(GLES30.GL_RGBA16I, RGBA), RGBA16UI(GLES30.GL_RGBA16UI, RGBA), RGBA32I(GLES30.GL_RGBA32I, RGBA),
	RGBA32UI(GLES30.GL_RGBA32UI, RGBA);

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
