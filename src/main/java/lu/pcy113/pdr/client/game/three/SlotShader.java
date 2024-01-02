package lu.pcy113.pdr.client.game.three;

import java.util.HashMap;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.texture.Texture;

public class SlotShader extends Shader {
	
	public static final String TEXTURE = "txt1";
	
	public SlotShader() {
		super(SlotShader.class.getName(),
				true,
				new ShaderPart("./resources/shaders/ui/plain.vert"),
				new ShaderPart("./resources/shaders/ui/txt1.frag"));
	}
	
	@Override
	public void createUniforms() {
		createUniform(Shader.PROJECTION_MATRIX);
		createUniform(Shader.TRANSFORMATION_MATRIX);
		createUniform(Shader.VIEW_MATRIX);
		
		createUniform(TEXTURE);
	}
	
	public static class SlotMaterial extends TextureMaterial {
		
		public SlotMaterial(Texture texture) {
			super(SlotMaterial.class.getName(),
					new SlotShader(),
					new HashMap<String, Texture>(1) {{put(TEXTURE, texture);}});
		}
		
	}

}
