package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class SlotShader extends RenderShader {

	public static final String TEXTURE = "txt1";

	public SlotShader() {
		super(SlotShader.class.getName(), true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/txt1.frag"));
	}

	@Override
	public void createUniforms() {
		createUniform(RenderShader.PROJECTION_MATRIX);
		createUniform(RenderShader.TRANSFORMATION_MATRIX);
		createUniform(RenderShader.VIEW_MATRIX);

		createUniform(TEXTURE);
	}

	public static class SlotMaterial extends TextureMaterial {

		public SlotMaterial(SingleTexture texture) {
			super(SlotMaterial.class.getName(), new SlotShader(), new HashMap<String, Texture>(1) {
				{
					put(TEXTURE, texture);
				}
			});
		}

	}

}
