package lu.kbra.gamebox.client.es.engine.utils.mem.img;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;

public class MemImage implements Cleanupable {

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

	@Override
	public void cleanup() {
		if(buffer == null)
			return;
		
		if (MemImageOrigin.STBI.equals(origin)) {
			STBImage.stbi_image_free(buffer);
			buffer = null;
		} else if (MemImageOrigin.OPENGL.equals(origin)) {
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
