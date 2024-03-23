package lu.pcy113.pdr.client.game.three;

import java.util.HashMap;

import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class SlotInstanceShader extends RenderShader {

	public static final String TEXTURE = "txt1";

	public SlotInstanceShader() {
		super(SlotInstanceShader.class.getName(), true, AbstractShaderPart.load("./resources/shaders/ui/plain_inst.vert"), AbstractShaderPart.load("./resources/shaders/ui/txt1_inst.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);

		createUniform(TEXTURE);
	}

	public static class SlotInstanceMaterial extends TextureMaterial {

		public SlotInstanceMaterial(SingleTexture texture) {
			super(SlotInstanceMaterial.class.getName(), new SlotInstanceShader(), new HashMap<String, Texture>(1) {
				{
					put(TEXTURE, texture);
				}
			});
		}

	}

}
