package lu.kbra.gamebox.client.es.engine.utils.bake.text.json;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

/**
 * https://evanw.github.io/font-texture-generator/
 */
public class JSONFontRemapper {

	public static final String STRING = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	public static void main(String[] args) throws JSONException, IOException {
		if (args.length < 1)
			throw new IllegalArgumentException("Usage: JSONFontRemapper <dir>");

		for (String f : new File(args[0]).list()) {
			if (f.endsWith("json")) {
				try {
					map(args[0] + "/" + f);
				} catch (Exception e) {
					System.err.println("Couldn't map: " + f);
					e.printStackTrace();
				}
			}
		}
	}

	public static void map(String inJsonPath) throws JSONException, IOException {
		System.out.println("Mapping: "+inJsonPath);
		
		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get(inJsonPath))));

		String fontName = obj.getString("name");
		int fontSize = obj.getInt("size");
		boolean bold = obj.optBoolean("bold", false);
		boolean italic = obj.optBoolean("italic", false);
		int width = obj.getInt("width");
		int height = obj.getInt("height");

		JSONObject chars = obj.getJSONObject("characters");
		String inImgPath = FileUtils.changeExtension(inJsonPath, "png");
		BufferedImage input = ImageIO.read(new File(inImgPath));

		String outImgPath = FileUtils.appendName(inImgPath, "_norm");
		BufferedImage output = new BufferedImage(STRING.length() * fontSize, fontSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = output.createGraphics();
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, STRING.length()*fontSize, fontSize);
		
		for (int i = 0; i < STRING.length(); i++) {
			char c = STRING.charAt(i);

			JSONObject cObj = chars.getJSONObject(c + "");

			int cX = cObj.getInt("x"), cY = cObj.getInt("y"), cWidth = cObj.getInt("width"), cHeight = cObj.getInt("height"), cOriginX = cObj.getInt("originX"), cOriginY = cObj.getInt("originY"), cAdvance = cObj.getInt("advance");

			int inX1 = cX, inY1 = cY, inX2 = cX + cWidth, inY2 = cY + cHeight, outX1 = i * fontSize + (fontSize - cWidth) / 2, outY1 = 0 + (fontSize - cHeight) / 2, outX2 = (i + 1) * fontSize - (fontSize - cWidth) / 2,
					outY2 = fontSize - (fontSize - cHeight) / 2;

			g2d.drawImage(input, outX1, outY1, outX2, outY2, inX1, inY1, inX2, inY2, Color.BLUE, null);

			System.out.println("Char: " + c + " (" + i + "), from: (" + inX1 + "," + inY1 + ")-(" + inX2 + "," + inY2 + "), to: (" + outX1 + "," + outY1 + ")-(" + outX2 + "," + outY2 + ")");
		}

		g2d.dispose();
		
		// output = makeDarkTransparent(output);
		
		ImageIO.write(output, "PNG", new File(outImgPath));
	}

	public static BufferedImage makeDarkTransparent(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		// Create a new BufferedImage with transparency
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// Iterate over each pixel of the image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Get the color of the current pixel
				Color color = new Color(image.getRGB(x, y));

				// Calculate luminosity (based on RGB to grayscale conversion)
				int luminosity = (int) (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());

				// Set alpha value based on luminosity
				int alpha = (luminosity > 128) ? 255 : 0;

				// Set the color with calculated alpha to the new image
				Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), luminosity);
				newImage.setRGB(x, y, newColor.getRGB());
			}
		}

		return newImage;
	}

}
