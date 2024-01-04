package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.utils.FileUtils;

public class CubemapTexture extends Texture {
	
	private static final String[] FACES = "top;bottom;left;right;front;back".split(";");

	public CubemapTexture(String path) {
		this(path, path, GL40.GL_LINEAR, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public CubemapTexture(String name, String path) {
		this(name, path, GL40.GL_LINEAR, GL40.GL_CLAMP_TO_EDGE);
	}
	
	public CubemapTexture(String name, String path, int filter) {
		this(name, path, filter, GL40.GL_CLAMP_TO_EDGE);
	}
	public CubemapTexture(String name, String path, int filter, int wrap) {
		super(name, path, filter, GL40.GL_TEXTURE_CUBE_MAP, wrap);
		
		gen();
		bind();
		
		String[] faces = new String[6];
		for(int i = 0; i < 6; i++) {
			faces[i] = FileUtils.appendName(path, FACES[i]);
			if(!Files.exists(Paths.get(faces[i])))
				throw new RuntimeException("Texture does not exist: " + faces[i]);
		}
		GlobalLogger.log(Arrays.toString(faces));
		
		for (int i = 0; i < faces.length; i++) {
			ByteBuffer imageBuffer;
			int width, height, channels;
			int[] w = new int[1];
			int[] h = new int[1];
			int[] c = new int[1];

			// Load image data using STB Image
			imageBuffer = STBImage.stbi_load(faces[i], w, h, c, 0);
			
			width = w[0];
			height = h[0];
			channels = c[0];
			
			int colors = getColorByChannels(channels);
			if(colors == -1)
				throw new RuntimeException("Invalid channel count: " + channels);

			if (imageBuffer != null) {
				GL40.glTexImage2D(GL40.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, colors, width, height, 0, colors, GL40.GL_UNSIGNED_BYTE, imageBuffer);
				STBImage.stbi_image_free(imageBuffer);
			} else {
				throw new RuntimeException("Failed to load texture");
			}
		}

		filter();
		wrap();
	}

}
