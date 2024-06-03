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

public class MaterialListShader extends RenderShader {

	public static final String NAME = MaterialListShader.class.getName();
	public static final String TXT1 = "txt1";

	public MaterialListShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/txt1.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();
		
		createUniform(TXT1);
	}

	public static class MaterialListMaterial extends TextureMaterial {

		public static final String NAME = MaterialListMaterial.class.getName();
		
		public static final String TEXTURE_NAME = "MaterialListTxt1";
		public static final String TEXTURE_PATH = "./resources/textures/ui/material_list.png";
		public static final String MESH_PATH = "./resources/models/ui/material_list.obj";

		public MaterialListMaterial(MaterialListShader shader, SingleTexture txt1) {
			super(NAME, shader, new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}
		
		public MaterialListMaterial(SingleTexture txt1) {
			this(new MaterialListShader(), txt1);
		}
		
		@DefaultObjectDecoderMethod
		public static MaterialListMaterial create() {
			CacheManager cache = GlobalUtils.currentLoadCache;
			return cache.loadOrGetMaterial(MaterialListShader.MaterialListMaterial.NAME,
					MaterialListShader.MaterialListMaterial.class,
					cache.loadOrGetSingleTexture(MaterialListShader.MaterialListMaterial.TEXTURE_NAME,
							MaterialListShader.MaterialListMaterial.TEXTURE_PATH, TextureFilter.NEAREST));
		}

	}

}
