package lu.pcy113.pdr.client.game;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.impl.Renderable;

public class BackgroundMaterial extends Material {

	public static final String NAME = BackgroundMaterial.class.getName();

	private Vector4f color = new Vector4f(0, 0, 0, 1);

	public BackgroundMaterial(int index, BackgroundShader shader) {
		super(NAME + "-" + index, shader);
	}

	@Override
	public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
		super.setProperty(BackgroundShader.COLOR, color);

		super.bindProperties(cache, parent, shader);
	}

	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector4f color) {
		this.color = color;
	}

}
