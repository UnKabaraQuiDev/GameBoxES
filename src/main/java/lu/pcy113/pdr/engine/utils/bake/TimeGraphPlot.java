package lu.pcy113.pdr.engine.utils.bake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.PDRUtils;

public class TimeGraphPlot {
	
	private static final double PX_PER_MS_FACTOR = 100;
	private static final int GRAPH_WIDTH = (int) (100*PX_PER_MS_FACTOR);
	private static final int GRAPH_HEIGHT = 1000;
	
	public static void main(String[] args) {
		System.out.println("Args: " + Arrays.toString(args));
		if (args.length != 1) {
			System.out.println("Usage: TimeGraphPlot <file>");
			return;
		}
		String filePath = args[0];
		
		List<String> dataList = Arrays.asList(FileUtils.readFile(filePath).split("\n"));
		
		double firstStartNano = Long.MAX_VALUE, firstStartMs = Long.MAX_VALUE;
		firstDatas: {
			String[] parts = dataList.get(0).split("/");
			String[] startParts = parts[0].split("\\>");
			String[] endParts = parts[1].split("\\>");
			String[] subStartParts = startParts[1].split(":");
			String[] subEndParts = endParts[1].split(":");
			String name = subStartParts[0];
			long startMs = Long.parseLong(subStartParts[1]);
			long startNano = Long.parseLong(subStartParts[2]);
			long endMs = Long.parseLong(subEndParts[0]);
			long endNano = Long.parseLong(subEndParts[1]);
			
			firstStartMs = startNano / 1_000_000.0;
			firstStartNano = startNano;
		}
		
		Collections.reverse(dataList);
		
		BufferedImage image = new BufferedImage(GRAPH_WIDTH, GRAPH_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, GRAPH_WIDTH, GRAPH_HEIGHT);
		
		Map<String, Color> colorMap = new HashMap<>();
		
		colorMap.put("u_update_loop", new Color(0, 255, 0));
		colorMap.put("u_input", new Color(255, 0, 0));
		colorMap.put("u_update", new Color(255, 0, 255));
		colorMap.put("u_pollEvents", new Color(0, 0, 255));
		
		colorMap.put("r_render_loop", new Color(0, 128, 0));
		colorMap.put("r_clear", new Color(128, 0, 0));
		colorMap.put("r_render", new Color(128, 0, 128));
		colorMap.put("r_swap", new Color(0, 0, 128));
		
		for (String data : dataList) {
			String[] parts = data.split("/");
			// System.out.println("parts = " + Arrays.toString(parts));
			String[] startParts = parts[0].split("\\>");
			String[] endParts = parts[1].split("\\>");
			// System.out.println("start parts = " + Arrays.toString(startParts));
			// System.out.println("end parts = " + Arrays.toString(endParts));
			String[] subStartParts = startParts[1].split(":");
			String[] subEndParts = endParts[1].split(":");
			// System.out.println("sub start parts = " + Arrays.toString(subStartParts));
			// System.out.println("sub end parts = " + Arrays.toString(subEndParts));
			String name = subStartParts[0];
			// System.out.println("name = " + name);
			double startMs = Long.parseLong(subStartParts[1]);
			long startNano = Long.parseLong(subStartParts[2]);
			// System.out.println("startMs = " + startMs);
			double endMs = Long.parseLong(subEndParts[0]);
			long endNano = Long.parseLong(subEndParts[1]);
			// System.out.println("endMs = " + endMs);
			
			startMs = startNano / 1_000_000.0;
			endMs = endNano / 1_000_000.0;
			
			/*if(firstStartMs > startMs)
				firstStartMs = startMs;
			if(firstStartNano > startNano)
				firstStartNano = startNano;*/
			
			// System.out.println("firstStartMs = " + firstStartMs);
			// System.out.println("firstStartNano = " + firstStartNano);
			
			startMs -= firstStartMs;
			endMs -= firstStartMs;
			startNano -= firstStartNano;
			endNano -= firstStartNano;
			
			// System.out.println("New startMs = " + startMs+"; New endMs = " + endMs+"; New startNano = " + startNano+"; New endNano = " + endNano);
			
			Color color = colorMap.computeIfAbsent(name, k -> PDRUtils.randomColor(false));
			// System.out.println("color = " + color);
			
			int startX = (int) (startMs * PX_PER_MS_FACTOR);
			int endX = (int) (endMs * PX_PER_MS_FACTOR);
			int width = endX - startX;
			int startY = 0;
			int height = 25;
			
			if(startX % GRAPH_WIDTH > 0) {
				startY = ((startX+GRAPH_WIDTH-1) / GRAPH_WIDTH)*(height*2);
				startX %= GRAPH_WIDTH;
			}
			
			if(name.startsWith("u_")) {
				//height -= 25;
				startY += 25;
			}else if(name.startsWith("r_")) {
				//height -= 25;
			}
			if(!name.endsWith("_loop")) {
				height /= 2;
				startY += height/2;
			}
			
			System.out.println("for name: " + name+", time:"+((endMs - startMs))+"ms, quad: "+startX+" "+startY+" "+width+" "+height+" "+color);
			
			g.setColor(color);
			g.fillRect(startX, startY, width, height);
		}
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5f));
		for(int i = 0; i < GRAPH_HEIGHT/50; i++) {
			g.drawLine(0, 50*i, GRAPH_WIDTH, 50*i);
		}
		
		g.setStroke(new BasicStroke(1f));
		for(int i = 0; i < GRAPH_WIDTH/PX_PER_MS_FACTOR; i++) {
			System.out.println("w: "+(GRAPH_WIDTH/PX_PER_MS_FACTOR)+" i:"+(i*PX_PER_MS_FACTOR));
			g.drawLine((int) (i*PX_PER_MS_FACTOR), 0, (int) (i*PX_PER_MS_FACTOR), GRAPH_HEIGHT);
		}
		
		g.dispose();
		
		try {
			File outputImage = new File(FileUtils.changeExtension(filePath, "png"));
			ImageIO.write(image, "png", outputImage);
			System.out.println("Graph plotted successfully.");
		} catch (IOException e) {
			System.err.println("Error writing image: " + e.getMessage());
		}
	}
	
}
