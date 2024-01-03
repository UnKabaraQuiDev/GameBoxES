package lu.pcy113.pdr.engine.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

	public static final String RESOURCES = "./resources/";

	public static final String SHADERS = "shaders/";
	public static final String MODELS = "models/";
	public static final String TEXTURES = "textures/";

	public static String readFile(String filePath) {
		String str;
		if (!Files.exists(Paths.get(filePath))) {
			throw new RuntimeException("File [" + filePath + "] does not exist");
		}
		try {
			str = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return str;
	}

	public static String recursiveTree(String path) throws IOException {
		String list = "";
		// list all the files in the 'path' directory and add them to the string 'list'
		File directory = new File(path);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					list += file + "\n";
				} else {
					list += recursiveTree(file.getCanonicalPath());
				}
			}
		}
		return list;
	}

	/*
	 * public static List<String> getShaderUniforms(String string) { return
	 * Arrays.asList(readFile(RESOURCES+SHADERS+string+"/info.txt").
	 * replace("^(?:[\t ]*(?:\r?\n|\r))+|\b *", "").split("\n")); }
	 * 
	 * public static String getShader(String string, int type) { return
	 * readFile(RESOURCES+SHADERS+string+"/"+string+"."+(type ==
	 * GL40.GL_VERTEX_SHADER ? "vert" : (type == GL40.GL_FRAGMENT_SHADER ? "frag" :
	 * null))); }
	 */

}
