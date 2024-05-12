package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader.FillMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthSpeedIndicatorShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.MaterialListShader;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UISceneMajorUpgradeTree extends UISceneState {

	private static final Vector2f CLICK_OFFSET_END = new Vector2f(0f, 0.1f);
	private static final float CLICK_ANIMATION_SPEED = 10f, ACCEPTED_ANIMATION_SPEED = 10f, DENIED_ANIMATION_SPEED = 10f, CLICK_ANIMATION_AMPLITUDE = 0.8f, HEALTH_FILL_SPEED = 1f;
	private static final Vector4f DENIED_COLOR_START = new Vector4f(1, 0, 0, 1), ACCEPTED_COLOR_START = new Vector4f(0, 1, 0, 1), IDLE_COLOR_START = new Vector4f(1, 1, 1, 1);

	private boolean treeViewActive = false;

	private Entity uiBG;

	private Entity materialList;
	private TextEmitter aminoAcidText, glucoseText, lipidText;

	private static final Vector3f HEALTH_INDICATOR_TEXT_BASE = new Vector3f(-3.1f, 2.48f, 1.1f), HEALTH_INDICATOR_BASE = new Vector3f(-4f, 2.7f, 1);

	private Entity maxHealthIndicator, maxHealthIndicatorTextEntity;
	private TextEmitter maxHealthIndicatorText;

	private float healthUpgradeClickProgress = 1, healthUpgradeDeniedProgress = 1, healthUpgradeAcceptedProgress = 1, healthFillProgress = 1, healthEmptyProgress = 1;
	private float visibleHealth = 1, oldVisibleHealth = 1;

	private static final Vector3f SPEED_INDICATOR_TEXT_BASE = new Vector3f(3.1f, 2.48f, 1.1f), SPEED_INDICATOR_BASE = new Vector3f(4f, 2.7f, 1);

	private Entity speedIndicator, speedIndicatorTextEntity;
	private TextEmitter speedIndicatorText;

	private float speedUpgradeClickProgress = 1, speedUpgradeDeniedProgress = 1, speedUpgradeAcceptedProgress = 1;

	public UISceneMajorUpgradeTree(UIScene3D scene) {
		super("MajorUpgradeTree", scene);

		Mesh bgMesh = cache.newQuadMesh("uiBGMesh", cache.loadOrGetMaterial(FillMaterial.NAME, FillShader.FillMaterial.class, GlobalConsts.BG), new Vector2f(2));
		uiBG = scene.addEntity("uiBG", new MeshComponent(bgMesh)).setActive(false);

		Mesh materialListMesh = cache.loadMesh("materialList", cache.loadOrGetMaterial(MaterialListShader.MaterialListMaterial.NAME, MaterialListShader.MaterialListMaterial.class,
				cache.loadOrGetSingleTexture(MaterialListShader.MaterialListMaterial.TEXTURE_NAME, MaterialListShader.MaterialListMaterial.TEXTURE_PATH, TextureFilter.NEAREST)), MaterialListShader.MaterialListMaterial.MESH_PATH);
		materialList = scene.addEntity("materialList", new MeshComponent(materialListMesh), new Transform3DComponent(new Vector3f(-4f, -2, 1), new Quaternionf(), new Vector3f(2)), new RenderComponent(15));

		aminoAcidText = GlobalUtils.createUIText(cache, "aminoAcidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		glucoseText = GlobalUtils.createUIText(cache, "glucoseText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		lipidText = GlobalUtils.createUIText(cache, "lipidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		materialList.addComponent(new SubEntitiesComponent(new Entity("aminoAcidText", new TextEmitterComponent(aminoAcidText), new Transform3DComponent(new Vector3f(-3.1f, -1.8f, 1.1f))),
				new Entity("glucoseText", new TextEmitterComponent(glucoseText), new Transform3DComponent(new Vector3f(-3.1f, -2.25f, 1.1f))),
				new Entity("lipidText", new TextEmitterComponent(lipidText), new Transform3DComponent(new Vector3f(-3.1f, -2.7f, 1.1f)))));

		// health indicator
		Mesh healthIndicatorMesh = cache.loadMesh("healthIndicator",
				cache.loadOrGetMaterial(HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.HEALTH_NAME, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.class, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.HEALTH_NAME,
						cache.loadOrGetRenderShader(HealthSpeedIndicatorShader.NAME, HealthSpeedIndicatorShader.class),
						cache.loadOrGetSingleTexture(HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.TEXTURE_HEALTH_NAME, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.TEXTURE_HEALTH_PATH, TextureFilter.NEAREST)),
				HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.MESH_HEALTH_PATH);
		maxHealthIndicator = scene.addEntity("healthIndicator", new MeshComponent(healthIndicatorMesh),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(HEALTH_INDICATOR_BASE), new Quaternionf(), new Vector3f(1.5f)), new RenderComponent(15));

		maxHealthIndicatorText = GlobalUtils.createUIText(cache, "healthIndicatorText", 3, "000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(IDLE_COLOR_START);
		maxHealthIndicatorText.setCharOffset(new Vector2f(0.1f, 0));
		maxHealthIndicatorTextEntity = new Entity("healthIndicatorText", new TextEmitterComponent(maxHealthIndicatorText),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(HEALTH_INDICATOR_TEXT_BASE), new Quaternionf(), new Vector3f(0.9f)));
		maxHealthIndicator.addComponent(new SubEntitiesComponent(maxHealthIndicatorTextEntity));

		// speed indicator
		Mesh speedIndicatorMesh = cache.loadMesh("speedIndicator",
				cache.loadOrGetMaterial(HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.SPEED_NAME, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.class, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.SPEED_NAME,
						cache.loadOrGetRenderShader(HealthSpeedIndicatorShader.NAME, HealthSpeedIndicatorShader.class),
						cache.loadOrGetSingleTexture(HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.TEXTURE_SPEED_NAME, HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.TEXTURE_SPEED_PATH, TextureFilter.NEAREST)),
				HealthSpeedIndicatorShader.HealthSpeedIndicatorMaterial.MESH_SPEED_PATH);
		speedIndicator = scene.addEntity("speedIndicator", new MeshComponent(speedIndicatorMesh),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(SPEED_INDICATOR_BASE), new Quaternionf(), new Vector3f(1.5f)), new RenderComponent(15));

		speedIndicatorText = GlobalUtils.createUIText(cache, "speedIndicatorText", 3, "000", Alignment.TEXT_LEFT).getTextEmitter(cache);
		((TextMaterial) speedIndicatorText.getMesh().getMaterial()).setFgColor(IDLE_COLOR_START);
		speedIndicatorText.setCharOffset(new Vector2f(0.1f, 0));
		speedIndicatorTextEntity = new Entity("speedIndicatorText", new TextEmitterComponent(speedIndicatorText),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(SPEED_INDICATOR_TEXT_BASE), new Quaternionf(), new Vector3f(0.9f)));
		speedIndicator.addComponent(new SubEntitiesComponent(speedIndicatorTextEntity));

		cache.dump(System.err);
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		// health
		if (healthUpgradeAcceptedProgress < 1) {
			healthUpgradeAcceptedProgress = Math.clamp(0, 1, healthUpgradeAcceptedProgress + dTime * ACCEPTED_ANIMATION_SPEED);

			((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(ACCEPTED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(healthUpgradeAcceptedProgress), new Vector4f()));
		}

		if (healthUpgradeDeniedProgress < 1) {
			healthUpgradeDeniedProgress = Math.clamp(0, 1, healthUpgradeDeniedProgress + dTime * DENIED_ANIMATION_SPEED);

			((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(DENIED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(healthUpgradeDeniedProgress), new Vector4f()));
		}

		if (healthFillProgress < 1) {
			healthFillProgress = Math.clamp(0, 1, healthFillProgress + dTime * HEALTH_FILL_SPEED);

			HealthSpeedIndicatorMaterial mat = (HealthSpeedIndicatorMaterial) maxHealthIndicator.getComponent(MeshComponent.class).getMesh(cache).getMaterial();
			oldVisibleHealth = Math.lerp(visibleHealth, GlobalUtils.INSTANCE.playerData.getMaxHealth(), Interpolators.QUART_OUT.evaluate(healthFillProgress));
			mat.setRedProgress(1);
			mat.setGreenProgress(oldVisibleHealth / GlobalUtils.INSTANCE.playerData.getMaxHealth());
			
			if(healthFillProgress >= 1) {
				visibleHealth = GlobalUtils.INSTANCE.playerData.getMaxHealth();
			}
		}
		
		if (healthEmptyProgress < 1) {
			healthEmptyProgress = Math.clamp(0, 1, healthEmptyProgress + dTime * HEALTH_FILL_SPEED);
			
			HealthSpeedIndicatorMaterial mat = (HealthSpeedIndicatorMaterial) maxHealthIndicator.getComponent(MeshComponent.class).getMesh(cache).getMaterial();
			visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
			mat.setGreenProgress(visibleHealth / GlobalUtils.INSTANCE.playerData.getMaxHealth());
			mat.setRedProgress(Math.lerp(oldVisibleHealth, visibleHealth, Interpolators.QUART_OUT.evaluate(healthEmptyProgress)) / GlobalUtils.INSTANCE.playerData.getMaxHealth());
		}

		if (healthUpgradeClickProgress < 1) {
			healthUpgradeClickProgress = Math.clamp(0, 1, healthUpgradeClickProgress + dTime * CLICK_ANIMATION_SPEED);

			float change = Interpolators.QUAD_IN_OUT.evaluate(healthUpgradeClickProgress);
			Vector2f offset = CLICK_OFFSET_END.mul(change, new Vector2f()).mul(CLICK_ANIMATION_AMPLITUDE);

			maxHealthIndicator.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(HEALTH_INDICATOR_BASE)).updateMatrix();
			maxHealthIndicatorTextEntity.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(HEALTH_INDICATOR_TEXT_BASE)).updateMatrix();
		}

		// speed
		if (speedUpgradeAcceptedProgress < 1) {
			speedUpgradeAcceptedProgress = Math.clamp(0, 1, speedUpgradeAcceptedProgress + dTime * ACCEPTED_ANIMATION_SPEED);

			((TextMaterial) speedIndicatorText.getMesh().getMaterial()).setFgColor(ACCEPTED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(speedUpgradeAcceptedProgress), new Vector4f()));
		}

		if (speedUpgradeDeniedProgress < 1) {
			speedUpgradeDeniedProgress = Math.clamp(0, 1, speedUpgradeDeniedProgress + dTime * DENIED_ANIMATION_SPEED);

			((TextMaterial) speedIndicatorText.getMesh().getMaterial()).setFgColor(DENIED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(speedUpgradeDeniedProgress), new Vector4f()));
		}

		if (speedUpgradeClickProgress < 1) {
			speedUpgradeClickProgress = Math.clamp(0, 1, speedUpgradeClickProgress + dTime * CLICK_ANIMATION_SPEED);

			float change = Interpolators.QUAD_IN_OUT.evaluate(speedUpgradeClickProgress);
			Vector2f offset = CLICK_OFFSET_END.mul(change, new Vector2f()).mul(CLICK_ANIMATION_AMPLITUDE);

			speedIndicator.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(SPEED_INDICATOR_BASE)).updateMatrix();
			speedIndicatorTextEntity.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(SPEED_INDICATOR_TEXT_BASE)).updateMatrix();
		}
	}

	@Override
	public void render(float dTime) {
		aminoAcidText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getAminoAcid()) + "")).updateText();
		glucoseText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getGlucose()) + "")).updateText();
		lipidText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getLipid()) + "")).updateText();

		maxHealthIndicatorText.setText(MathUtils.fillPrefix(3, '0', Math.clamp(0, 999, GlobalUtils.INSTANCE.playerData.getMaxHealth()) + "")).updateText();
		speedIndicatorText.setText(MathUtils.fillPrefix(3, '0', Math.clamp(0, 999, GlobalUtils.INSTANCE.playerData.getSpeed()) + "")).updateText();
	}

	public void setTreeViewActive(boolean b) {
		treeViewActive = b;

		uiBG.setActive(b);
	}

	public void startHealthUpgradeDenied() {
		healthUpgradeClickProgress = healthUpgradeDeniedProgress = 0;
	}

	public void startHealthUpgradeAccepted() {
		healthUpgradeClickProgress = healthUpgradeAcceptedProgress = healthFillProgress = 0;
		healthEmptyProgress = 1;
		visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
	}
	
	public void startHealthEmpty() {
		healthEmptyProgress = 0;
		healthFillProgress = 1;
		visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
	}

	public void startSpeedUpgradeDenied() {
		speedUpgradeClickProgress = speedUpgradeDeniedProgress = 0;
	}

	public void startSpeedUpgradeAccepted() {
		speedUpgradeClickProgress = speedUpgradeAcceptedProgress = 0;
	}

}
