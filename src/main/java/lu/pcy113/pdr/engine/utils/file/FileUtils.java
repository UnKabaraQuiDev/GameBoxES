package lu.pcy113.pdr.engine.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import lu.pcy113.pdr.engine.impl.shader.AbstractShader;
import lu.pcy113.pdr.engine.utils.MemImage;

public final class FileUtils {

	public static final String RESOURCES = "./resources/";

	public static final String SHADERS = "shaders/";
	public static final String MODELS = "models/";
	public static final String TEXTURES = "textures/";

	public static MemImage STBILoad(String filePath) {
		return STBILoadRGBA(filePath);
	}

	public static MemImage STBILoadRGBA(String filePath) {
		return STBILoad(filePath, 4);
	}

	public static MemImage STBILoadRGB(String filePath) {
		return STBILoad(filePath, 3);
	}

	public static MemImage STBILoad(String filePath, int desiredChannels) {
		int[] w = new int[1], h = new int[1], c = new int[1];

		ByteBuffer buffer = STBImage.stbi_load(filePath, w, h, c, desiredChannels);

		return new MemImage(w[0], h[0], c[0], buffer, true, false);
	}

	public static boolean STBISaveIncremental(String filePath, MemImage image) {
		filePath = FileUtils.getIncrement(filePath);
		return STBISave(filePath, image);
	}

	public static String getIncrement(String filePath) {
		String woExt = removeExtension(filePath);
		String ext = getExtension(filePath);

		int index = 1;
		while (Files.exists(Paths.get(woExt + "-" + index + "." + ext))) {
			index++;
		}

		return woExt + "-" + index + "." + ext;
	}

	public static boolean STBISave(String filePath, MemImage image) {
		STBImageWrite.stbi_flip_vertically_on_write(image.isFromOGL());
		return STBImageWrite.stbi_write_png(filePath, image.getWidth(), image.getHeight(), image.getChannels(),
				image.getBuffer(), 0);
	}

	public static void STBIFree(ByteBuffer buffer) {
		STBImage.stbi_image_free(buffer);
	}

	public static String readStringFile(String filePath) {
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

	public static String appendName(String path, String suffix) {
		return path.replaceAll("(.+)(\\.[^.]+)$", "$1" + suffix + "$2");
	}

	public static String changeExtension(String path, String ext) {
		return path.replaceAll("(.+)(\\.[^.]+)$", "$1." + ext);
	}

	public static String removeExtension(String path) {
		return path.replaceAll("(.+)(\\.[^.]+)$", "$1");
	}

	public static String getExtension(String path) {
		return path.replaceAll("(.+\\.)([^.]+)$", "$2");
	}

}
