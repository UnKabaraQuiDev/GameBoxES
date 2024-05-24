package lu.kbra.gamebox.client.es.engine.utils.bake.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONObject;

import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

/**
 * https://fonts.varg.dev/
 */
public class FntFontMapRemapper {

	public static void main(String[] args) throws IOException {
		if (args.length != 1)
			throw new IllegalArgumentException("Args: <fnt-file>");

		File inFile = new File(args[0]);
		if (!inFile.exists())
			throw new FileNotFoundException(args[0]);
		File outFile = new File(FileUtils.changeExtension(args[0], "json"));
		outFile.createNewFile();

		map(inFile, outFile);
	}

	public static void map(File inFile, File outFile) throws IOException {
		List<String> inLines = loadIn(inFile);

		String infoName = null;
		int infoSize = 0;
		boolean infoBold = false, infoItalic = false;
		int sizeWidth = 0, sizeHeight = 0;

		JSONObject obj = new JSONObject();
		JSONObject chars = new JSONObject();

		for (int i = 0; i < inLines.size(); i++) {
			String line = inLines.get(i);
			String[] tokens = line.split(" ");
			if (line.startsWith("info")) {
				infoName = tokens[1].split("=")[1];
				infoSize = Integer.parseInt(tokens[2].split("=")[1]);
				infoBold = tokens[3].split("=")[1].equals("1");
				infoItalic = tokens[4].split("=")[1].equals("1");
			} else if (line.startsWith("common")) {
				sizeWidth = Integer.parseInt(tokens[5].split("=")[1]);
				sizeHeight = Integer.parseInt(tokens[6].split("=")[1]);
			} else if (line.startsWith("chars")) {
				continue;
			} else if (line.startsWith("char")) {
				JSONObject nObj = new JSONObject();
				
				char idChar = (char) Integer.parseInt(tokens[1].split("=")[1]);
				nObj.put("x", Integer.parseInt(tokens[2].split("=")[1]));
				nObj.put("y", Integer.parseInt(tokens[3].split("=")[1]));
				nObj.put("width", Integer.parseInt(tokens[4].split("=")[1]));
				nObj.put("height", Integer.parseInt(tokens[5].split("=")[1]));
				nObj.put("originX", Double.parseDouble(tokens[6].split("=")[1]));
				nObj.put("originY", Double.parseDouble(tokens[7].split("=")[1]));
				nObj.put("advance", Double.parseDouble(tokens[8].split("=")[1]));
				
				chars.put(idChar+"", nObj);
			} else {
				System.out.println("Ignored: " + i + ":: " + line);
				continue;
			}
		}

	}

	private static List<String> loadIn(File inFile) throws IOException {
		return Files.readAllLines(Paths.get(inFile.getPath()));
	}

}
