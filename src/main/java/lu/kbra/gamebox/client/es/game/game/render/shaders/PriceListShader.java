package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.codec.DefaultObjectDecoderMethod;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class PriceListShader extends RenderShader {

	public static final String NAME = PriceListShader.class.getName();
	public static final String TXT1 = "txt1";

	public PriceListShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/txt1.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);
	}

	public static class PriceListMaterial extends TextureMaterial {

		public static final String NAME = PriceListMaterial.class.getName();

		public static final String TEXTURE_NAME = "PriceListTxt1";
		public static final String TEXTURE_PATH = "./resources/textures/ui/price_list.png";
		public static final String MESH_PATH = "./resources/models/ui/price_list.obj";

		public PriceListMaterial(PriceListShader shader, SingleTexture txt1) {
			super(NAME, shader, new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}

		public PriceListMaterial(SingleTexture txt1) {
			this(new PriceListShader(), txt1);
		}

		@DefaultObjectDecoderMethod
		public static PriceListMaterial create() {
			CacheManager cache = GlobalUtils.currentLoadCache;
			return cache.loadOrGetMaterial(PriceListMaterial.NAME, PriceListMaterial.class, cache.loadOrGetSingleTexture(PriceListMaterial.TEXTURE_NAME, PriceListMaterial.TEXTURE_PATH, TextureFilter.NEAREST));
		}

	}

}
