package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Texture implements Cleanupable, UniqueID {

	private final String path;
	private final String name;
	private int tid = -1;
	private int filter;

	public Texture(String name, int w, int h, ByteBuffer buffer) {
		this(name, w, h, buffer, GL40.GL_LINEAR);
	}

	public Texture(String name, int w, int h, ByteBuffer buffer, int filter) {
		this.name = name;
		this.path = null;
		this.filter = filter;
		this.tid = generateTexture(w, h, buffer);
	}

	public Texture(String name, String path, int filter) {
		this.path = path;
		this.name = name;
		this.filter = filter;

		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];

		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		this.tid = generateTexture(w[0], h[0], buffer);
	}

	public Texture(String name, String path) {
		this(name, path, GL40.GL_LINEAR);
	}

	private int generateTexture(int w, int h, ByteBuffer buffer) {
		int tid = GL40.glGenTextures();

		GL40.glBindTexture(GL40.GL_TEXTURE_2D, tid);
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MIN_FILTER, filter);
		GL40.glTexParameteri(GL40.GL_TEXTURE_2D, GL40.GL_TEXTURE_MAG_FILTER, filter);
		GL40.glTexImage2D(GL40.GL_TEXTURE_2D, 0, GL40.GL_RGBA, w, h, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
		GL40.glGenerateMipmap(GL40.GL_TEXTURE_2D);

		return tid;
	}

	public void bind(int i) {
		if (i > 31)
			return;
		GL40.glActiveTexture(GL40.GL_TEXTURE0 + i);
		GL40.glBindTexture(GL40.GL_TEXTURE_2D, tid);
	}

	@Override
	public void cleanup() {
		if (tid != -1) {
			GL40.glDeleteTextures(tid);
			tid = -1;
		}
	}

	@Override
	public String getId() {
		return name;
	}

	public int getTid() {
		return tid;
	}

	public String getPath() {
		return path;
	}

}
