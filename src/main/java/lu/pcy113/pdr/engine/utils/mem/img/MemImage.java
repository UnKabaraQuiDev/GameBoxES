package lu.pcy113.pdr.engine.utils.mem.img;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class MemImage {

	private int width, height, channels;
	private ByteBuffer buffer;
	private MemImageOrigin origin;

	public MemImage(int width, int height, int channels, ByteBuffer buffer, MemImageOrigin origin) {
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.buffer = buffer;
		this.origin = origin;
	}

	public void free() {
		if (MemImageOrigin.STBI.equals(origin) && buffer != null) {
			STBImage.stbi_image_free(buffer);
			buffer = null;
		} else if (MemImageOrigin.OPENGL.equals(origin) && buffer != null) {
			MemoryUtil.memFree(buffer);
			buffer = null;
		}
	}

	public boolean isFromOGL() {
		return MemImageOrigin.OPENGL.equals(origin);
	}

	public boolean isFromStbi() {
		return MemImageOrigin.STBI.equals(origin);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getChannels() {
		return channels;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

}
