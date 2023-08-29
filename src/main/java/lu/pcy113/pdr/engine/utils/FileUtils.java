package lu.pcy113.pdr.engine.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL30;

public final class FileUtils {
	
	public static final String RESOURCES = "./resources/";
	
	public static final String SHADERS = "shaders/";
	public static final String MODELS = "models/";
	public static final String TEXTURES = "textures/";
	
	public static String readFile(String filePath) {
		String str;
		try {
			str = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return str;
	}

	public static String getResource(String string) {
		return readFile(RESOURCES+string);
	}

	public static List<String> getShaderUniforms(String string) {
		return Arrays.asList(readFile(RESOURCES+SHADERS+string+"/info.txt").replace("^(?:[\t ]*(?:\r?\n|\r))+|\b *", "").split("\n"));
	}

	public static String getShader(String string, int type) {
		return readFile(RESOURCES+SHADERS+string+"/"+string+"."+(type == GL30.GL_VERTEX_SHADER ? "vert" : (type == GL30.GL_FRAGMENT_SHADER ? "frag" : null)));
	}
	
}
