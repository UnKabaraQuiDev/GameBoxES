package lu.kbra.gamebox.client.es.game.game.utils.bake;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Math;

import lu.kbra.gamebox.client.es.game.game.utils.NoiseGenerator;

public class NoiseMain {

	public static void main(String[] args) {

		// Create a noise generator
		NoiseGenerator noiseGenerator = new NoiseGenerator();

		// Define texture dimensions
		int width = 2048;
		int height = 2048;

		// Create buffered image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(double factor = 1; factor < 100; factor *= 2) {
			for(int scale = 1; scale < 15; scale++) {
				// Generate texture
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						// Generate noise value for current pixel
						double noiseValue = Math.clamp(0, 1, noiseGenerator.noise(x/factor, y/factor, 0, scale));

						// Convert noise value to color
						int color = getColorFromNoise(noiseValue/2+0.5);

						// Set pixel color in buffered image
						image.setRGB(x, y, color);
					}
				}

				// Save image
				try {
					File output = new File("./resources/bakes/noise/texture-"+scale+"-"+factor+".png");
					ImageIO.write(image, "png", output);
					System.out.println("Texture generated and saved successfully: "+output.getName());
				} catch (IOException e) {
					System.out.println("Error saving texture: " + e.getMessage());
				}
			}
		}
	}

	private static int getColorFromNoise(double noiseValue) {
		// System.out.println(noiseValue);
		int color = (int) (noiseValue * 255);
		return new Color(color, color, color).getRGB();
	}

}
