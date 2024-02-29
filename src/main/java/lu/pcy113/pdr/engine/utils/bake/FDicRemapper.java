package lu.pcy113.pdr.engine.utils.bake;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.text.fdic.FDicFile;
import lu.pcy113.pdr.engine.utils.text.fdic.FDicGlyph;
import lu.pcy113.pdr.engine.utils.text.fdic.FDicLoader;

public final class FDicRemapper {

	public static void main(String[] args) throws IOException {

		if (args.length != 3)
			throw new IllegalArgumentException(
					"Arguments: [file path w/o extension] [image size] [char resolution]");

		final String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

		final String mainPath = args[0];
		final int imageSize = Integer.parseInt(
				args[1]);
		final int charResolution = Integer.parseInt(
				args[2]);

		File imageFile = new File(
				mainPath + ".png");
		if (!imageFile.exists())
			throw new IllegalArgumentException(
					"Could not find image file at " + imageFile);
		File fdicFile = new File(
				mainPath + ".fdic");
		if (!fdicFile.exists())
			throw new IllegalArgumentException(
					"Could not find fdic file at " + fdicFile);

		FDicFile dic = FDicLoader.load(
				fdicFile);

		int charCount = dic.getGlyphs().size();

		BufferedImage input = ImageIO.read(
				imageFile);

		BufferedImage output = new BufferedImage(
				charCount * charResolution,
				charResolution,
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = output.createGraphics();

		char[] cc = chars.toCharArray();
		for (int index = 0; index < chars.length(); index++) {
			char c = cc[index];

			FDicGlyph glyph = dic.getGlyph(
					c);
			if (glyph == null) {
				System.out.println(
						"Glyph for char: " + c + " not found, passing");
				continue;
			} else {
				System.out.println(
						"Adding glyph for char: " + c + " " + glyph);
			}

			copyGlyph(
					input,
					g2d,
					glyph,
					dic,
					charResolution,
					index,
					imageSize);
		}

		g2d.dispose();

		File outFile = new File(
				FileUtils.appendName(
						imageFile.getPath(),
						"_std"));

		ImageIO.write(
				output,
				"PNG",
				outFile);
	}

	private static void copyGlyph(BufferedImage input, Graphics2D output, FDicGlyph glyph, FDicFile dic, int res, int index, int sRes) {
		int x = index * res;
		int y = 0;

		int toX1 = (int) Math.rint(
				(glyph.gettCoordX() - dic.getTextureSpread()) * sRes), // + (glyph.gethBearingX()*glyph.getWidth())) * sRes),
				toY1 = (int) Math.rint(
						(glyph.gettCoordY() + glyph.gettHeight()) * sRes), // + (glyph.gethBearingY()*glyph.gethAdvance())) * sRes),
				toX2 = (int) Math.rint(
						(glyph.gettCoordX() + glyph.gettWidth()) * sRes), // + (glyph.getvBearingX()*glyph.getWidth())) * sRes),
				toY2 = (int) Math.rint(
						(glyph.gettCoordY() - dic.getTextureSpread()) * sRes); // + (glyph.getvBearingY()*glyph.getHeight())) * sRes);

		output.drawImage(
				input,
				x,
				y,
				x + res,
				y + res,
				toX1,
				toY1,
				toX2,
				toY2,
				null);

		// output.drawImage(input, x, y, x + res, y + res, (int) Math.rint(glyph.gettCoordX() * sRes), (int)
		// Math.rint(glyph.gettCoordY() * sRes),
		// (int) Math.rint((glyph.gettCoordX() + glyph.gettWidth()) * sRes), (int) Math.rint((glyph.gettCoordY() +
		// glyph.gettHeight()) * sRes), null);
	}

}
