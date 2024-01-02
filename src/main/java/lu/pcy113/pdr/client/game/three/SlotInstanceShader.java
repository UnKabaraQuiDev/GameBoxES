package lu.pcy113.pdr.client.game.three;

import java.util.HashMap;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.texture.Texture;

public class SlotInstanceShader extends Shader {
	
	public static final String TEXTURE = "txt1";
	
	public SlotInstanceShader() {
		super(SlotInstanceShader.class.getName(),
				true,
				new ShaderPart("./resources/shaders/ui/plain_inst.vert"),
				new ShaderPart("./resources/shaders/ui/txt1_inst.frag"));
	}
	
	@Override
	public void createUniforms() {
		createUniform(Shader.PROJECTION_MATRIX);
		createUniform(Shader.TRANSFORMATION_MATRIX);
		createUniform(Shader.VIEW_MATRIX);

		createUniform(TEXTURE);
	}
	
	public static class SlotInstanceMaterial extends TextureMaterial {
		
		public SlotInstanceMaterial(Texture texture) {
			super(SlotInstanceMaterial.class.getName(),
					new SlotInstanceShader(),
					new HashMap<String, Texture>(1) {{put(TEXTURE, texture);}});
		}
		
	}

}
