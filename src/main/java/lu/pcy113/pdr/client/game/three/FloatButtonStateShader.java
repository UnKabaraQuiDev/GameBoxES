package lu.pcy113.pdr.client.game.three;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class FloatButtonStateShader extends RenderShader {
	
	public static final String NAME = FloatButtonStateShader.class.getName();
	
	public static final String RADIUS = "radius";
	public static final String COLOR = "color";
	public static final String THRESHOLD = "threshold";
	public static final String VALUE = "value";
	
	public FloatButtonStateShader() {
		super(NAME,
				AbstractShaderPart.load("./resources/shaders/ui/float_button.frag"),
				AbstractShaderPart.load("./resources/shaders/ui/plain.vert"));
	}
	
	@Override
	public void createUniforms() {
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		createUniform(RenderShader.VIEW_POSITION);
		
		createUniform(RADIUS);
		createUniform(COLOR);
		createUniform(THRESHOLD);
		createUniform(VALUE);
	}
	
	public static class FloatButtonStateMaterial extends Material {
		
		public static final String NAME = FloatButtonStateMaterial.class.getName();
		
		private float radius, value, threshold;
		private Vector4f color;
		
		public FloatButtonStateMaterial() {
			super(NAME, new FloatButtonStateShader());
		}
		
		public FloatButtonStateMaterial(FloatButtonStateShader shader) {
			super(NAME, shader);
		}
		
		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(COLOR, color);
			setProperty(RADIUS, radius);
			setProperty(THRESHOLD, threshold);
			setProperty(VALUE, value);
			
			super.bindProperties(cache, parent, shader);
		}
		
		public void setRadius(float radius) {
			this.radius = radius;
		}
		public void setValue(float value) {
			this.value = value;
		}
		public void setThreshold(float threshold) {
			this.threshold = threshold;
		}
		public void setColor(Vector4f color) {
			this.color = color;
		}
		
	}
	
}
