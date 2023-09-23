package lu.pcy113.pdr.engine.graph.composition.blur.gaussian;

import java.util.Arrays;
import java.util.logging.Level;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.utils.Logger;

public class GaussianBlurMaterial extends Material {
	
	public static final String NAME = GaussianBlurMaterial.class.getName();
	
	private int width, height;
	private float[] kernel;
	
	public GaussianBlurMaterial(int width, int height) {
		super(NAME, GaussianBlurShader.NAME);
		
		if(width % 2 == 0 || height % 2 == 0)
			throw new RuntimeException("Kernel width/height cannot be even");
		
		this.width = width;
		this.height = height;
		
		kernel = new float[width*height];
		
		generateKernel();
		
		for(int y = 0; y < height; y++) {
			Logger.log(Level.INFO, Arrays.toString(Arrays.copyOfRange(kernel, y*width, (y+1)*width)));
		}
	}
	
	private void generateKernel() {
		double halfWidth = width/2;
		double halfHeight = height/2;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				kernel[y*width+x] = (float) (((halfWidth-Math.abs(x-halfWidth))/halfWidth)*((halfHeight-Math.abs(y-halfHeight))/halfHeight));
			}
		}
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				shader.setUniform(GaussianBlurShader.KERNEL+"["+(y*width+x)+"]", kernel[y*width+x]);
			}
		}
		
		super.bindProperties(cache, scene, shader);
	}
	
}
