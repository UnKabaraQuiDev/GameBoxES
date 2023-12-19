package lu.pcy113.pdr.client.game;

import java.util.HashMap;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.text.TextMaterial;
import lu.pcy113.pdr.engine.impl.Renderable;

public class FillTextMaterial
		extends
		TextMaterial {

	public Vector4f color;

	public FillTextMaterial(String name, FillTextShader shader, String raster, String rasterIndex, Vector4f color) {
		super(name, shader, new HashMap<String, String>() {
			{
				put("raster", raster);
				put("rasterIndex", rasterIndex);
			}
		});
		this.color = color;
	}

	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		super.setProperty(FillTextShader.FILL_COLOR, color);

		super.bindProperties(cache, scene, shader);
	}

	public Vector4f getColor() { return color; }

	public void setColor(Vector4f color) { this.color = color; }

}
