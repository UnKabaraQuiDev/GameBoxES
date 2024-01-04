package lu.pcy113.pdr.client.game.three;

import org.joml.Vector4f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.impl.Renderable;

public class FourButtonStateShader extends Shader {
	
	public static final String NAME = FourButtonStateShader.class.getName();
	
	public static final String BUTTONS = "buttons";
	
	public FourButtonStateShader() {
		super(NAME,
				new ShaderPart("./resources/shaders/ui/four_button.frag"),
				new ShaderPart("./resources/shaders/ui/plain.vert"));
	}
	
	@Override
	public void createUniforms() {
		createUniform(Shader.PROJECTION_MATRIX);
		createUniform(Shader.VIEW_MATRIX);
		createUniform(Shader.TRANSFORMATION_MATRIX);
		createUniform(Shader.VIEW_POSITION);
		
		createUniform(BUTTONS);
	}
	
	public static class FourButtonStateMaterial extends Material {
		
		public static final String NAME = FourButtonStateMaterial.class.getName();
		
		private Vector4f buttons;
		
		public FourButtonStateMaterial() {
			super(NAME, new FourButtonStateShader());
		}
		
		public FourButtonStateMaterial(Shader shader) {
			super(NAME, shader);
		}
		
		@Override
		public void bindProperties(CacheManager cache, Renderable parent, Shader shader) {
			setProperty(BUTTONS, buttons);
			
			super.bindProperties(cache, parent, shader);
		}
		
		public void setButtons(Vector4f buttons) {
			this.buttons = buttons;
		}
		
	}
	
}
