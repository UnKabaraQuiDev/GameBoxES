package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;
import lu.kbra.gamebox.client.es.engine.utils.codec.DefaultObjectDecoderMethod;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class MajorUpgradeTreeNodeShader extends RenderShader {

	public static final String NAME = MajorUpgradeTreeNodeShader.class.getName();

	private static final String TXT1 = "txt1";

	private static final String PROGRESS = "progress";
	private static final String ICONS = "icon";
	private static final String TINT = "tint";

	public MajorUpgradeTreeNodeShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/upgrade_tree_node.vert"), AbstractShaderPart.load("./resources/shaders/ui/upgrade_tree_node.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);

		createUniform(PROGRESS);
		createUniform(ICONS);
		createUniform(TINT);
	}

	public static class MajorUpgradeTreeNodeMaterial extends TextureMaterial {

		public static final String NAME = MajorUpgradeTreeNodeMaterial.class.getName();

		private float progress = 0, icon = 0;
		private Vector4f tint;

		public MajorUpgradeTreeNodeMaterial(SingleTexture txt1) {
			super(NAME + Math.random(), new MajorUpgradeTreeNodeShader(), new HashMap<String, Texture>(1) {
				{
					put(TXT1, txt1);
				}
			});
		}

		public MajorUpgradeTreeNodeMaterial(String name, SingleTexture txt1) {
			super(name + Math.random(), new MajorUpgradeTreeNodeShader(), new HashMap<String, Texture>(1) {
				{
					put(TXT1, txt1);
				}
			});
		}

		public MajorUpgradeTreeNodeMaterial(MajorUpgradeTreeNodeShader shader, SingleTexture txt1) {
			super(NAME + Math.random(), shader, new HashMap<String, Texture>() {
				{
					put(TXT1, txt1);
				}
			});
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene, RenderShader shader) {
			super.setProperty(PROGRESS, progress);
			super.setProperty(ICONS, icon);
			super.setProperty(TINT, tint);

			super.bindProperties(cache, scene, shader);
		}

		public Vector4f getTint() {
			return tint;
		}

		public void setTint(Vector4f tint) {
			this.tint = tint;
		}

		public float getProgress() {
			return progress;
		}

		public void setProgress(float progress) {
			this.progress = progress;
		}

		public float getIcon() {
			return icon;
		}

		public void setIcon(float icon) {
			this.icon = icon;
		}

		@DefaultObjectDecoderMethod
		public static MajorUpgradeTreeNodeMaterial create() {
			CacheManager cache = GlobalUtils.currentLoadCache;
			return cache.loadMaterial(MajorUpgradeTreeNodeMaterial.class, cache.loadOrGetRenderShader(MajorUpgradeTreeNodeShader.NAME, MajorUpgradeTreeNodeShader.class),
					cache.loadOrGetSingleTexture(NAME, "./resources/textures/ui/icons_list.png", TextureFilter.NEAREST));
		}

	}

}
