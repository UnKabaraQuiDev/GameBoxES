package lu.pcy113.pdr.engine.graph.texture;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import org.lwjgl.stb.STBImage;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class Texture implements Cleanupable, UniqueID {

	private final String path;
	private final String name;
	private int tid = -1;
	private int filter, txtResType = GL20.GL_TEXTURE_2D;

	public Texture(String name, int w, int h, ByteBuffer buffer) {
		this(name, w, h, buffer, GL40.GL_LINEAR, GL20.GL_TEXTURE_2D);
	}

	public Texture(String name, int w, int h, ByteBuffer buffer, int filter, int txtResType) {
		this.name = name;
		this.path = null;
		this.filter = filter;
		this.txtResType = txtResType;

		this.tid = generateTexture(w, h, buffer);
	}

	public Texture(String name, String path, int filter, int txtResType) {
		this.path = path;
		this.name = name;
		this.filter = filter;
		this.txtResType = txtResType;

		int[] w = new int[1];
		int[] h = new int[1];
		int[] c = new int[1];

		ByteBuffer buffer = STBImage.stbi_load(path, w, h, c, 4);
		if (buffer == null)
			throw new RuntimeException("Failed to load texture");
		this.tid = generateTexture(w[0], h[0], buffer);
	}

	public Texture(String path) {
		this(path, path, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D);
	}

	public Texture(String name, String path) {
		this(name, path, GL40.GL_LINEAR, GL40.GL_TEXTURE_2D);
	}

	private int generateTexture(int w, int h, ByteBuffer buffer) {
		int tid = GL40.glGenTextures();

		GL40.glBindTexture(txtResType, tid);
		GL40.glPixelStorei(GL40.GL_UNPACK_ALIGNMENT, 1);
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_MIN_FILTER, filter);
		GL40.glTexParameteri(txtResType, GL40.GL_TEXTURE_MAG_FILTER, filter);
		if (txtResType == GL40.GL_TEXTURE_1D) {
			GL40.glTexImage1D(txtResType, 0, GL40.GL_RGBA, w, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
		} else if (txtResType == GL40.GL_TEXTURE_2D) {
			GL40.glTexImage2D(txtResType, 0, GL40.GL_RGBA, w, h, 0, GL40.GL_RGBA, GL40.GL_UNSIGNED_BYTE, buffer);
		}
		GL40.glGenerateMipmap(txtResType);

		return tid;
	}

	public void bind(int i) {
		if (i > 31)
			return;
		GL40.glActiveTexture(GL40.GL_TEXTURE0 + i);
		GL40.glBindTexture(txtResType, tid);
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
