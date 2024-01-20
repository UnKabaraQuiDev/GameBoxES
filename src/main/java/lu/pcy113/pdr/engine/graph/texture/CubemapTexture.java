package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.utils.FileUtils;
import lu.pcy113.pdr.engine.utils.consts.TextureType;

public class CubemapTexture extends Texture {
	
	private static final String[] FACES = "top;bottom;left;right;front;back".split(";");
	
	public CubemapTexture(String path) {
		super(path, path, TextureOperation.FILE_BUFFER_LOAD);
		txtType = TextureType.CUBE_MAP;
	}
	
	public CubemapTexture(String name, String path) {
		super(name, path, TextureOperation.FILE_BUFFER_LOAD);
		txtType = TextureType.CUBE_MAP;
	}
	
	@Override
	public boolean setup() {
		if(isValid()) {
			throw new RuntimeException("Cannot setup already loaded Cubemap Texture");
		}
		
		if(TextureOperation.FILE_BUFFER_LOAD.equals(textureOperation)) {
			generateFileBufferCubeMapTexture();
		}
		return isValid();
	}
	
	private void generateFileBufferCubeMapTexture() {
		String[] faces = new String[6];
		for (int i = 0; i < 6; i++) {
			faces[i] = FileUtils.appendName(path, FACES[i]);
			if (!Files.exists(Paths.get(faces[i]))) {
				throw new RuntimeException("Texture does not exist: " + faces[i]);
			}
		}
		GlobalLogger.log(Arrays.toString(faces));
		
		gen();
		bind();
		//GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
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
			
			format = getFormatByChannels(channels);
			internalFormat = getInternalFormatByChannels(channels);
			if (format == null) {
				cleanup();
				throw new RuntimeException("Invalid channel count: " + channels);
			}
			
			if (imageBuffer != null) {
				System.out.println("format "+format+"; internalFormat "+internalFormat+", dataType "+dataType+", width "+width+", height "+height);
				GL40.glTexImage2D(TextureType.CM_PX.getGlId() + i, 0, internalFormat.getGlId(), width, height, 0, format.getGlId(), dataType.getGlId(), imageBuffer);
				STBImage.stbi_image_free(imageBuffer);
			} else {
				cleanup();
				throw new RuntimeException("Failed to load texture");
			}
		}
		
		applyFilter();
		applyWrap();
		
		unbind();
	}
	
}
