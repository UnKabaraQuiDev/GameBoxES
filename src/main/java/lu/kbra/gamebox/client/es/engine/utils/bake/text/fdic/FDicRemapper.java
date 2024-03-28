package lu.kbra.gamebox.client.es.engine.utils.bake.text.fdic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.text.fdic.FDicFile;
import lu.kbra.gamebox.client.es.engine.utils.text.fdic.FDicGlyph;
import lu.kbra.gamebox.client.es.engine.utils.text.fdic.FDicLoader;

public final class FDicRemapper {

	public static void main(String[] args) throws IOException {

		if (args.length != 3)
			throw new IllegalArgumentException("Arguments: [file path w/o extension] [image size] [char resolution]");

		final String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

		final String mainPath = args[0];
		final int imageSize = Integer.parseInt(args[1]);
		final int charResolution = Integer.parseInt(args[2]);

		File imageFile = new File(mainPath + ".png");
		if (!imageFile.exists())
			throw new IllegalArgumentException("Could not find image file at " + imageFile);
		File fdicFile = new File(mainPath + ".fdic");
		if (!fdicFile.exists())
			throw new IllegalArgumentException("Could not find fdic file at " + fdicFile);

		FDicFile dic = FDicLoader.load(fdicFile);

		int charCount = dic.getGlyphs().size();

		BufferedImage input = ImageIO.read(imageFile);

		BufferedImage output = new BufferedImage(charCount * charResolution, charResolution, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2d = output.createGraphics();

		char[] cc = chars.toCharArray();
		for (int index = 0; index < chars.length(); index++) {
			char c = cc[index];

			FDicGlyph glyph = dic.getGlyph(c);
			if (glyph == null) {
				System.out.println("Glyph for char: " + c + " not found, passing");
				continue;
			} else {
				System.out.println("Adding glyph for char: " + c + " " + glyph);
			}

			copyGlyph(input, g2d, glyph, dic, charResolution, index, imageSize);
		}

		g2d.dispose();

		File outFile = new File(FileUtils.appendName(imageFile.getPath(), "_std"));

		ImageIO.write(output, "PNG", outFile);
	}

	private static void copyGlyph(BufferedImage input, Graphics2D output, FDicGlyph glyph, FDicFile dic, int charRes, int index, int imageRes) {
		int x = index * charRes;
		int y = 0;

		if (glyph.getWidth() == 0 || glyph.getHeight() == 0)
			return;

		int subimageX = (int) (glyph.gettCoordX() * imageRes - dic.getTextureSpread());
		int subimageY = (int) ((glyph.gettCoordY() + glyph.getHeight()) * imageRes + dic.getTextureSpread());
		int subimageWidth = (int) (glyph.getWidth() * 32);
		int subimageHeight = (int) (glyph.getHeight() * 32);
		int subImageX2 = (int) ((glyph.gettCoordX() + glyph.getWidth()) * imageRes + dic.getTextureSpread());
		int subImageY2 = (int) (glyph.gettCoordY() * imageRes - dic.getTextureSpread());

		// Create a subimage representing the glyph
		// BufferedImage glyphImage = input.getSubimage(subimageX, subimageY,
		// subimageWidth, subimageHeight);
		output.drawImage(input, x, y, x + charRes, y + charRes, subimageX, subimageY, subImageX2, subImageY2, null);
		// Draw the subimage onto the output graphics
		// output.drawImage(glyphImage, x, y, charRes, charRes, null);
	}

}
