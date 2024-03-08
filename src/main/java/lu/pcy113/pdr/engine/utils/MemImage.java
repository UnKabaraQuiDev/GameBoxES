package lu.pcy113.pdr.engine.utils;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class MemImage {

	private int width, height, channels;
	private ByteBuffer buffer;
	private boolean fromStbi, fromOGL;

	public MemImage(int width, int height, int channels, ByteBuffer buffer, boolean fromStbi, boolean fromOGL) {
		this.width = width;
		this.height = height;
		this.channels = channels;
		this.buffer = buffer;
		this.fromStbi = fromStbi;
		this.fromOGL = fromOGL;
	}

	public void free() {
		if (fromStbi && buffer != null) {
			STBImage.stbi_image_free(buffer);
			buffer = null;
		} else if (fromOGL && buffer != null) {
			MemoryUtil.memFree(buffer);
			buffer = null;
		}
	}

	public boolean isFromOGL() {
		return fromOGL;
	}

	public boolean isFromStbi() {
		return fromStbi;
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
