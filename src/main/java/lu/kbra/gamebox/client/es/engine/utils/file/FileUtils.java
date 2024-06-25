package lu.kbra.gamebox.client.es.engine.utils.file;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImage;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImageOrigin;

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
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer buffer = STBImage.stbi_load(filePath, w, h, c, desiredChannels);

		return new MemImage(w[0], h[0], c[0], buffer, MemImageOrigin.STBI);
	}

	public static boolean STBISaveIncremental(String filePath, MemImage image) {
		filePath = PCUtils.getIncrement(filePath);
		return STBISave(filePath, image);
	}

	public static boolean STBISave(String filePath, MemImage image) {
		STBImageWrite.stbi_flip_vertically_on_write(image.isFromOGL());
		return STBImageWrite.stbi_write_png(filePath, image.getWidth(), image.getHeight(), image.getChannels(), image.getBuffer(), 0);
	}

	public static void STBIFree(ByteBuffer buffer) {
		STBImage.stbi_image_free(buffer);
	}

}
