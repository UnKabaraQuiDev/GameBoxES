package lu.pcy113.pdr.engine.utils.text.fdic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.utils.file.FileUtils;

public final class FDicLoader {

	public static FDicFile load(File fdicFile) {
		String[] lines = FileUtils.readStringFile(fdicFile.getAbsolutePath()).replaceAll("\\ŧ", " ").split("\n+");
		/*
		 * for(int l = 0; l < lines.length; l++) { System.out.println("-- "+lines[l]); }
		 */

		float spreadInTexture = 0, spreadInFontMetrics = 0;
		boolean glyphs = false, kernings = false;

		Map<Character, FDicGlyph> glyphMap = new HashMap<>();

		for (int l = 0; l < lines.length; l++) {
			String line = lines[l];

			if (line.isEmpty())
				continue;

			if (line.startsWith("#"))
				continue;

			if (line.equals("SPREAD IN TEXTURE")) {
				line = lines[++l];
				spreadInTexture = Float.valueOf(line.trim());
				continue;
			}

			if (line.equals("SPREAD IN FONT METRICS")) {
				line = lines[++l];
				spreadInFontMetrics = Float.valueOf(line.trim());
				continue;
			}

			if (line.equals("GLYPHS")) {
				glyphs = true;
				kernings = false;
				continue;
			}

			if (line.equals("KERNINGS")) {
				kernings = true;
				glyphs = false;
				continue;
			}

			if (glyphs) {
				// System.out.println(line);
				String[] tokens = line.split(" ");
				// System.out.println(Arrays.toString(tokens));

				int id = Integer.valueOf(tokens[0].trim());
				float width = Float.valueOf(tokens[1].trim());
				float height = Float.valueOf(tokens[2].trim());
				float hBearingX = Float.valueOf(tokens[3].trim());
				float hBearingY = Float.valueOf(tokens[4].trim());
				float hAdvance = Float.valueOf(tokens[5].trim());
				float vBearingX = Float.valueOf(tokens[6].trim());
				float vBearingY = Float.valueOf(tokens[7].trim());
				float vAdvance = Float.valueOf(tokens[8].trim());
				float tCoordX = Float.valueOf(tokens[9].trim());
				float tCoordY = Float.valueOf(tokens[10].trim());
				float tWidth = Float.valueOf(tokens[11].trim());
				float tHeight = Float.valueOf(tokens[12].trim());

				glyphMap.put(Character.valueOf((char) id), new FDicGlyph((char) id, width, height, hBearingX, hBearingY, hAdvance, vBearingX, vBearingY, vAdvance, tCoordX, tCoordY, tWidth, tHeight));

				continue;
			}
		}

		FDicFile fDic = new FDicFile(fdicFile, spreadInTexture, spreadInFontMetrics, glyphMap);

		return fDic;
	}

}
