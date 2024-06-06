package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.codec.DefaultObjectDecoderMethod;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class MajorUpgradeTreeShader extends RenderShader {

	public static final String NAME = MajorUpgradeTreeShader.class.getName();

	private static final String TXT1 = "txt1";

	public MajorUpgradeTreeShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/txt1.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);
	}

	public static class MajorUpgradeTreeMaterial extends TextureMaterial {

		public static final String NAME = MajorUpgradeTreeMaterial.class.getName();

		public MajorUpgradeTreeMaterial(SingleTexture txt1) {
			super(NAME, new MajorUpgradeTreeShader(), new HashMap<String, Texture>(1) {
				{
					put(TXT1, txt1);
				}
			});
		}
		
		public MajorUpgradeTreeMaterial(String name, SingleTexture txt1) {
			super(name, new MajorUpgradeTreeShader(), new HashMap<String, Texture>(1) {
				{
					put(TXT1, txt1);
				}
			});
		}
		
		public MajorUpgradeTreeMaterial(MajorUpgradeTreeShader shader, SingleTexture txt1) {
			super(NAME, shader, new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}
		
		@DefaultObjectDecoderMethod
		public static MajorUpgradeTreeMaterial create() {
			CacheManager cache = GlobalUtils.currentLoadCache;
			return cache.loadOrGetMaterial(NAME, MajorUpgradeTreeMaterial.class, cache.loadOrGetRenderShader(MajorUpgradeTreeShader.NAME, MajorUpgradeTreeShader.class), cache.loadOrGetSingleTexture(NAME, "./resources/textures/ui/icons.png"));
		}

	}

}
