package lu.pcy113.pdr.engine.graph.composition.color_filter;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.impl.Renderable;

public class ColorFilterMaterial extends Material {
	
	public static final String NAME = ColorFilterMaterial.class.getName();
	
	protected Vector4f mul, add;
	
	public ColorFilterMaterial() {
		super(NAME, ColorFilterShader.NAME);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
		super.setProperty(ColorFilterShader.MUL, mul);
		super.setProperty(ColorFilterShader.ADD, add);
		
		super.bindProperties(cache, parent, shader);
	}
	
	public Vector4f getMul() {
		return mul;
	}
	public void setMul(Vector4f mul) {
		this.mul = mul;
	}
	public Vector4f getAdd() {
		return add;
	}
	public void setAdd(Vector4f add) {
		this.add = add;
	}
	
}
