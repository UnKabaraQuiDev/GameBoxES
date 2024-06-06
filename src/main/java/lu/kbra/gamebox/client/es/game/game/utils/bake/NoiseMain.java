package lu.kbra.gamebox.client.es.game.game.utils.bake;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.joml.Math;
import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.game.game.utils.noise.NoiseGenerator;

public class NoiseMain {

	public static void main(String[] args) throws IOException {

		// genNoiseImages();

		genNoiseStat();

	}

	private static void genNoiseStat() throws IOException {
		NoiseGenerator noiseGenerator = new NoiseGenerator();

		File file = new File("./resources/bakes/noise/distribution.txt");
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileWriter fw = new FileWriter(file);

		for (float i = 0; i < 100_000; i += 0.1) {

			fw.append(Double.toString(noiseGenerator.noise(i)) + "\n");

		}

		fw.flush();
		fw.close();

		System.out.println("Done to: " + file);
	}

	private static void genNoiseImages() {
		// Create a noise generator
		NoiseGenerator noiseGenerator = new NoiseGenerator();

		// Define texture dimensions
		int width = 2048;
		int height = 2048;

		// Create buffered image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (float factor = 1; factor < 100; factor *= 2) {
			for (int scale = 1; scale < 15; scale++) {
				// Generate texture
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						// Generate noise value for current pixel
						double noiseValue = Math.clamp(0, 1, noiseGenerator.noise(x / factor, y / factor, 0, scale));

						// Convert noise value to color
						int color = getColorFromNoise(noiseValue);

						// Set pixel color in buffered image
						image.setRGB(x, y, color);
					}
				}

				// Save image
				try {
					File output = new File("./resources/bakes/noise/texture-" + scale + "-" + factor + ".png");
					ImageIO.write(image, "png", output);
					System.out.println("Texture generated and saved successfully: " + output.getName());
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

	public static void map(NoiseGenerator noiseGenerator, String name, int from, int to) {
		map(noiseGenerator, name + "_normal", from, to, a -> a);
		map(noiseGenerator, name + "_squared", from, to, a -> a * a);
	}

	private static void map(NoiseGenerator noiseGenerator, String name, int from, int to, Function<Float, Float> transformer) {
		// Define texture dimensions
		int width = 2048;
		int height = 2048;

		// Create buffered image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Generate texture
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Generate noise value for current pixel
				double noiseValue = transformer.apply(Math.clamp(0, 1, noiseGenerator.noise(MathUtils.map(-width / 2 + x, 0, width, from, to), MathUtils.map(-height / 2 + y, 0, height, from, to))));

				// Convert noise value to color
				int color = getColorFromNoise(noiseValue);

				// Set pixel color in buffered image
				image.setRGB(x, y, color);
			}
		}

		// Save image
		try {
			File output = new File("./resources/bakes/noise/map-" + noiseGenerator.getScale() + "_" + name + ".png");
			ImageIO.write(image, "png", output);
			System.out.println("[" + Thread.currentThread().getName() + "] Texture generated and saved successfully: " + output.getName());
		} catch (IOException e) {
			System.out.println("[" + Thread.currentThread().getName() + "] Error saving texture: " + e.getMessage());
		}
	}

	public static void map(Function<Vector2f, Boolean> condition, String name, int from, int to) {
		// Define texture dimensions
		int width = 2048;
		int height = 2048;

		// Create buffered image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Generate texture
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Generate noise value for current pixel
				double noiseValue = condition.apply(new Vector2f(MathUtils.map(x, 0, width, from, to), MathUtils.map(y, 0, height, from, to))) ? 1 : 0;

				// Convert noise value to color
				int color = getColorFromNoise(noiseValue);

				// Set pixel color in buffered image
				image.setRGB(x, y, color);
			}
		}

		// Save image
		try {
			File output = new File("./resources/bakes/noise/map-" + name + ".png");
			ImageIO.write(image, "png", output);
			System.out.println("[" + Thread.currentThread().getName() + "] Texture generated and saved successfully: " + output.getName());
		} catch (IOException e) {
			System.out.println("[" + Thread.currentThread().getName() + "] Error saving texture: " + e.getMessage());
		}
	}

}
