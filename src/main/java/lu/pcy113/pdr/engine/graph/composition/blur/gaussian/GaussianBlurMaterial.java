package lu.pcy113.pdr.engine.graph.composition.blur.gaussian;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.scene.Scene;

public class GaussianBlurMaterial extends Material {
	
	public static final String NAME = GaussianBlurMaterial.class.getName();
	
	private int width, height;
	
	public GaussianBlurMaterial(int width, int height) {
		super(NAME, null, null, GaussianBlurShader.NAME);
		
		if(width % 2 == 0 || height %2 == 0)
			throw new RuntimeException("Kernel width/height cannot be even");
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void bindProperties(CacheManager cache, Scene scene, Shader shader) {
		double halfWidth = width/2;
		double halfHeight = height/2;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				shader.setUniform(GaussianBlurShader.KERNEL+"["+(x*width+y)+"]", ((halfWidth-Math.abs(x-halfWidth))/halfWidth)*((halfHeight-Math.abs(y-halfHeight))/halfHeight));
			}
		}
		
		super.bindProperties(cache, scene, shader);
	}
	
}
