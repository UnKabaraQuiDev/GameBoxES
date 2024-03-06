package lu.pcy113.pdr.engine.utils.text.fdic;

public class FDicGlyph {

	private char character;

	private float width, height, hBearingX, hBearingY, hAdvance, vBearingX, vBearingY, vAdvance, tCoordX, tCoordY, tWidth, tHeight;

	public FDicGlyph(char character, float width, float height, float hBearingX, float hBearingY, float hAdvance, float vBearingX, float vBearingY, float vAdvance, float tCoordX, float tCoordY, float tWidth, float tHeight) {
		this.character = character;
		this.width = width;
		this.height = height;
		this.hBearingX = hBearingX;
		this.hBearingY = hBearingY;
		this.hAdvance = hAdvance;
		this.vAdvance = vAdvance;
		this.vBearingX = vBearingX;
		this.vBearingY = vBearingY;
		this.tCoordX = tCoordX;
		this.tCoordY = tCoordY;
		this.tWidth = tWidth;
		this.tHeight = tHeight;
	}

	public char getCharacter() {
		return character;
	}

	public float getWidth() {
		return width;
	}

	public float gethAdvance() {
		return hAdvance;
	}

	public float gethBearingX() {
		return hBearingX;
	}

	public float gethBearingY() {
		return hBearingY;
	}

	public float getHeight() {
		return height;
	}

	public float gettCoordX() {
		return tCoordX;
	}

	public float gettCoordY() {
		return tCoordY;
	}

	public float gettHeight() {
		return tHeight;
	}

	public float gettWidth() {
		return tWidth;
	}

	public float getvAdvance() {
		return vAdvance;
	}

	public float getvBearingX() {
		return vBearingX;
	}

	public float getvBearingY() {
		return vBearingY;
	}

	@Override
	public String toString() {
		return "{char: " + character + ", width: " + width + ", height: " + height + ", hBearingX: " + hBearingX + ", hBearingY: " + hBearingY + ", hAdvance: " + hAdvance + ", vBearingX: " + vBearingX + ", vBearingY: " + vBearingY + ", vAdvance: "
				+ vAdvance + ", tCoordX: " + tCoordX + ", tCoordY: " + tCoordY + ", tWidth: " + tWidth + ", tHeight: " + tHeight + "}";
	}

}
