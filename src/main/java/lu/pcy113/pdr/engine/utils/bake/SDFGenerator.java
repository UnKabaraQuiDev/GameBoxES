package lu.pcy113.pdr.engine.utils.bake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lu.pcy113.pdr.engine.utils.PDRUtils;

public class SDFGenerator {
	
	// RES, FONT
	public static void main(String[] args) {
		int resolution = 128;
		String fontName = "Arial";
		
		if(args.length == 2) {
			resolution = Integer.parseInt(args[0]);
			fontName = args[1];
		}
		
		String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		
		Font font = new Font(fontName, Font.PLAIN, resolution);
		
		BufferedImage image = renderFontSDF(font, chars, resolution);
		
		try {
			File file = new File("./resources/bakes/" + font.getFamily() + "-" + font.getName() + "_sdf_" + resolution + "_" + chars.length() + ".png");
			if (!file.getParentFile().exists())
				file.mkdirs();
			if (!file.exists())
				file.createNewFile();
			ImageIO.write(image, "png", file);
			System.out.println("Saved to: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedImage renderFontSDF(Font font, String chars, int resolution) {
		
		int width = resolution * chars.length();
		int height = resolution;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.WHITE);
		
		FontRenderContext frc = g2d.getFontRenderContext();
		
		int cchar = 0;
		for (char c : chars.toCharArray()) {
			GlyphVector gv = font.createGlyphVector(frc, new char[] {c});
			AffineTransform at = gv.getGlyphTransform(0);
			Shape glyph = gv.getOutline();
			
			double[][] sdfMatrix = calculateSDF(glyph, at, resolution);
			renderSDFToImage(image, cchar++, sdfMatrix, resolution);
			System.out.println("Char: " + c + " index " + cchar);
			//printSdfMatrix(sdfMatrix, resolution);
		}
		
		g2d.dispose();
		
		return image;
	}
	
	private static void printSdfMatrix(double[][] sdfMatrix, int resolution) {
		// print matrix as a grid
		for (int y = 0; y < resolution; y++) {
			for (int x = 0; x < resolution; x++) {
				System.out.print(PDRUtils.fillString(String.valueOf((int) Math.round(sdfMatrix[x][y])), " ", 3) + " ");
			}
			System.out.println();
		}
	}
	
	private static double[][] calculateSDF(Shape shape, AffineTransform at, int resolution) {
		double[][] sdfMatrix = new double[resolution][resolution];
		
		Rectangle bounds = shape.getBounds();
		int width = bounds.width;
		int height = bounds.height;
		int shapeX = bounds.x;
		int shapeY = bounds.y;
		
		// Get the bounds of the shape
		double fScale = 0.7;
		double scaleFactor = Math.min(resolution / shape.getBounds2D().getWidth(), resolution / shape.getBounds2D().getHeight())*fScale;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
		shape = scaleTransform.createTransformedShape(shape);
		
		AffineTransform translateTransform = AffineTransform.getTranslateInstance((resolution-shape.getBounds().getCenterX()*(2.0-fScale))/2, -shape.getBounds().getY()*(2.0-fScale));
		shape = translateTransform.createTransformedShape(shape);
		
		for (int y = 0; y < resolution; y++) {
			for (int x = 0; x < resolution; x++) {
				sdfMatrix[x][y] = computeSDFAtPoint(x, y, shape, resolution);
			}
		}
		
		return sdfMatrix;
	}
	
	private static double computeSDFAtPoint(int x, int y, Shape shape, int resolution) {
		// Create a point at the specified coordinates
		Point point = new Point(x, y);
		
		PathIterator pathIterator = shape.getPathIterator(null);
		
		double minDistance = Double.MAX_VALUE;
		
		// Iterate through the shape's path segments
		double[] coords = new double[6];
		double lastX = 0, lastY = 0;
		
		while (!pathIterator.isDone()) {
			int segmentType = pathIterator.currentSegment(coords);
			
			switch (segmentType) {
			case PathIterator.SEG_MOVETO:
				lastX = coords[0];
				lastY = coords[1];
				break;
			
			case PathIterator.SEG_LINETO:
				// Calculate the distance from the point to the line segment
				double distance = pointToLineDistance(point, lastX, lastY, coords[0], coords[1]);
				
				// Update the minimum distance
				minDistance = Math.min(minDistance, distance);
				
				// Update lastX and lastY
				lastX = coords[0];
				lastY = coords[1];
				break;
			
			// Handle other segment types as needed
			
			default:
				// Handle other segment types if necessary
				break;
			}
			
			// Move to the next segment
			pathIterator.next();
		}
		
		return minDistance*(shape.contains(point) ? -1 : 1);
	}
	
	private static double pointToLineDistance(Point p, double x1, double y1, double x2, double y2) {
		double A = p.getX() - x1;
		double B = p.getY() - y1;
		double C = x2 - x1;
		double D = y2 - y1;
		
		double dot = A * C + B * D;
		double len_sq = C * C + D * D;
		double param = dot / len_sq;
		
		double xx, yy;
		
		if (param < 0 || (x1 == x2 && y1 == y2)) {
			xx = x1;
			yy = y1;
		} else if (param > 1) {
			xx = x2;
			yy = y2;
		} else {
			xx = x1 + param * C;
			yy = y1 + param * D;
		}
		
		double dx = p.getX() - xx;
		double dy = p.getY() - yy;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	
	private static void renderSDFToImage(BufferedImage image, int cchar, double[][] sdfMatrix, int squareResolution) {
		for (int y = 0; y < squareResolution; y++) {
			for (int x = 0; x < squareResolution; x++) {
				double sdf = sdfMatrix[x][y];
				
				int red = 255-org.joml.Math.clamp(0, 255, (int) (Math.round(Math.abs(sdf*10))));
				int blue = sdf < 0 ? 255 : 0;
				int green = Math.abs(sdf) < 1.5 ? 255 : 0;
				
				image.setRGB(cchar * squareResolution + x, y, PDRUtils.clampColor(red, green, blue).getRGB());
			}
		}
	}
	
}
