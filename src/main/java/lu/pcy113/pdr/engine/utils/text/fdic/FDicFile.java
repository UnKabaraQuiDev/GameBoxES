package lu.pcy113.pdr.engine.utils.text.fdic;

import java.io.File;
import java.util.Map;

public class FDicFile {

	private File file;

	private float textureSpread, fontMetricsSpread;
	private Map<Character, FDicGlyph> glyphs;

	public FDicFile(File file, float tSpread, float fMetricsSpread, Map<Character, FDicGlyph> glyphs) {
		this.file = file;
		this.textureSpread = tSpread;
		this.fontMetricsSpread = fMetricsSpread;
		this.glyphs = glyphs;
	}
	
	public FDicGlyph getGlyph(char c) {
		return glyphs.get(c);
	}
	
	public File getFile() {
		return file;
	}

	public float getTextureSpread() {
		return textureSpread;
	}

	public float getFontMetricsSpread() {
		return fontMetricsSpread;
	}

	public Map<Character, FDicGlyph> getGlyphs() {
		return glyphs;
	}

}
