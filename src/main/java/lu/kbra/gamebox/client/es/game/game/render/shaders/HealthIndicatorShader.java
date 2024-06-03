package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import org.joml.Vector2f;

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

public class HealthIndicatorShader extends RenderShader {

	public static final String NAME = HealthIndicatorShader.class.getName();

	private static final String TXT1 = "txt1";
	private static final String PROGRESS_GREEN = "progressGreen";
	private static final String PROGRESS_RED = "progressRed";
	private static final String BAR_OFFSET = "barOffset";

	public HealthIndicatorShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/ui/health_indicator.vert"), AbstractShaderPart.load("./resources/shaders/ui/health_indicator.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TXT1);
		createUniform(PROGRESS_GREEN);
		createUniform(PROGRESS_RED);
		createUniform(BAR_OFFSET);
	}

	public static class HealthIndicatorMaterial extends TextureMaterial {

		public static final String TEXTURE_HEALTH_NAME = "HealthIndicatorTxt1";
		public static final String TEXTURE_HEALTH_PATH = "./resources/textures/ui/health_indicator.png";
		public static final String MESH_HEALTH_PATH = "./resources/models/ui/health_indicator.obj";

		public static final String TEXTURE_SPEED_NAME = "SpeedIndicatorTxt1";
		public static final String TEXTURE_SPEED_PATH = "./resources/textures/ui/speed_indicator.png";
		public static final String MESH_SPEED_PATH = "./resources/models/ui/speed_indicator.obj";

		public static final String HEALTH_NAME = HealthIndicatorMaterial.class.getName() + TEXTURE_HEALTH_NAME;
		public static final String SPEED_NAME = HealthIndicatorMaterial.class.getName() + TEXTURE_SPEED_NAME;

		private float progressGreen, progressRed;
		private Vector2f barOffset;

		public HealthIndicatorMaterial(String name, HealthIndicatorShader shader, SingleTexture texture) {
			super(name, shader, new HashMap<String, Texture>(1) {
				{
					put(TXT1, texture);
				}
			});

			setGreenProgress(0.5f);
			setRedProgress(0.9f);
			setBarOffset(new Vector2f(0));
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable scene, RenderShader shader) {
			setProperty(PROGRESS_GREEN, progressGreen);
			setProperty(PROGRESS_RED, progressRed);
			setProperty(BAR_OFFSET, barOffset);

			super.bindProperties(cache, scene, shader);
		}

		public void setGreenProgress(float p) {
			this.progressGreen = p;
		}

		public void setRedProgress(float p) {
			this.progressRed = p;
		}

		public void setBarOffset(Vector2f p) {
			this.barOffset = p;
		}

		public float getProgressGreen() {
			return progressGreen;
		}

		public float getProgressRed() {
			return progressRed;
		}

		public Vector2f getBarOffset() {
			return barOffset;
		}

		@DefaultObjectDecoderMethod
		public static HealthIndicatorMaterial create() {
			CacheManager cache = GlobalUtils.currentLoadCache;
			return cache.loadOrGetMaterial(HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.class,
					HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME, cache.loadOrGetRenderShader(HealthIndicatorShader.NAME, HealthIndicatorShader.class),
					cache.loadOrGetSingleTexture(HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_PATH, TextureFilter.NEAREST));
		}

	}

}
