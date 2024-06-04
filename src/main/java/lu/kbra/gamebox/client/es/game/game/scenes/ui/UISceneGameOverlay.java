package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.util.Optional;

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
import lu.kbra.gamebox.client.es.game.game.data.Achievements;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader.FillMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthIndicatorShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthIndicatorShader.HealthIndicatorMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.MaterialListShader;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UISceneGameOverlay extends UISceneState {

	private static final Vector2f CLICK_OFFSET_END = new Vector2f(0f, 0.1f);
	private static final float CLICK_ANIMATION_SPEED = 10f, ACCEPTED_ANIMATION_SPEED = 10f, DENIED_ANIMATION_SPEED = 10f, CLICK_ANIMATION_AMPLITUDE = 0.8f, HEALTH_FILL_SPEED = 1f;
	private static final Vector4f DENIED_COLOR_START = new Vector4f(1, 0, 0, 1), ACCEPTED_COLOR_START = new Vector4f(0, 1, 0, 1), IDLE_COLOR_START = new Vector4f(1, 1, 1, 1);
	private static final float DEATH_BG_DARKEN_SPEED = 0.75f;

	private boolean treeViewActive = false, gameEndActive = false;

	private Entity uiBG;

	// material list - - -
	private Entity materialList;
	private TextEmitter aminoAcidText, glucoseText, lipidText;

	// health indicator - - -
	private static final Vector3f HEALTH_INDICATOR_TEXT_BASE = new Vector3f(-2.9f, 2.28f, 1.1f), HEALTH_INDICATOR_BASE = new Vector3f(-3.9f, 2.4f, 1);

	private Entity maxHealthIndicator, maxHealthIndicatorTextEntity;
	private TextEmitter maxHealthIndicatorText;
	private float healthRestoreClickProgress = 1, healthRestoreDeniedProgress = 1, healthRestoreAcceptedProgress = 1, healthFillProgress = 1, healthEmptyProgress = 1;
	private float visibleHealth = 1, oldVisibleHealth = 0;

	// game end - - -
	private static final Vector3f GAME_OVER_BASE = new Vector3f(0, 1, 2f), GAME_OVER_TITLES_BASE = new Vector3f(-4.2f, 0.5f, 2f), GAME_OVER_VALUES_BASE = new Vector3f(4.1f, 0.5f, 2f);

	private float gameEndProgress = 1;
	private Entity gameOverText, gameOverStatsTitles, gameOverStatsValues;
	private TextEmitter gameOverStatsTitlesText, gameOverStatsValuesText;

	public UISceneGameOverlay(UIScene3D scene) {
		super("MajorUpgradeTree", scene);

		Mesh bgMesh = cache.newQuadMesh("uiBGMesh", cache.loadOrGetMaterial(FillMaterial.NAME, FillShader.FillMaterial.class, GlobalConsts.TRANS_BG), new Vector2f(12, 6));
		uiBG = scene.addEntity("uiBG", new MeshComponent(bgMesh), new Transform3DComponent(new Vector3f(0, 0, 1.6f))).setActive(false);
		
		System.err.println("ACT: "+uiBG.isActive());

		// material list - - -
		Mesh materialListMesh = GlobalUtils.loadCompiledMesh(cache, "materialList", () -> {
			return cache.loadMesh("materialList",
					cache.loadOrGetMaterial(MaterialListShader.MaterialListMaterial.NAME, MaterialListShader.MaterialListMaterial.class,
							cache.loadOrGetSingleTexture(MaterialListShader.MaterialListMaterial.TEXTURE_NAME, MaterialListShader.MaterialListMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
					MaterialListShader.MaterialListMaterial.MESH_PATH);
		});
		materialList = scene.addEntity("materialList", new MeshComponent(materialListMesh), new Transform3DComponent(new Vector3f(-4f, -2, 1), new Quaternionf(), new Vector3f(2)), new RenderComponent(15));

		aminoAcidText = GlobalUtils.createUIText(cache, "aminoAcidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		glucoseText = GlobalUtils.createUIText(cache, "glucoseText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		lipidText = GlobalUtils.createUIText(cache, "lipidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		materialList.addComponent(new SubEntitiesComponent(new Entity("aminoAcidText", new TextEmitterComponent(aminoAcidText), new Transform3DComponent(new Vector3f(-3.1f, -1.8f, 1.1f))),
				new Entity("glucoseText", new TextEmitterComponent(glucoseText), new Transform3DComponent(new Vector3f(-3.1f, -2.25f, 1.1f))),
				new Entity("lipidText", new TextEmitterComponent(lipidText), new Transform3DComponent(new Vector3f(-3.1f, -2.7f, 1.1f)))));

		// health indicator - - -
		Mesh healthIndicatorMesh = GlobalUtils.loadCompiledMesh(cache, "healthIndicator", () -> {
			return cache.loadMesh("healthIndicator",
					cache.loadOrGetMaterial(HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.class, HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME,
							cache.loadOrGetRenderShader(HealthIndicatorShader.NAME, HealthIndicatorShader.class),
							cache.loadOrGetSingleTexture(HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_PATH, TextureFilter.NEAREST)),
					HealthIndicatorShader.HealthIndicatorMaterial.MESH_HEALTH_PATH);
		});
		maxHealthIndicator = scene.addEntity("healthIndicator", new MeshComponent(healthIndicatorMesh),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(HEALTH_INDICATOR_BASE), new Quaternionf(), new Vector3f(1.5f)), new RenderComponent(15));

		maxHealthIndicatorText = GlobalUtils.createUIText(cache, "healthIndicatorText", 5, GlobalLang.get("ui.hp") + " 00", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(IDLE_COLOR_START);
		maxHealthIndicatorText.setCharOffset(new Vector2f(0.1f, 0));
		maxHealthIndicatorTextEntity = new Entity("healthIndicatorText", new TextEmitterComponent(maxHealthIndicatorText),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(HEALTH_INDICATOR_TEXT_BASE), new Quaternionf(), new Vector3f(0.9f)));
		maxHealthIndicator.addComponent(new SubEntitiesComponent(maxHealthIndicatorTextEntity));

		healthFillProgress = 0;

		// game end - - -
		gameOverStatsTitlesText = GlobalUtils.createUIText(cache, "gameOverStatsTitlesText", 128, GlobalLang.get("game.over.titles"), Alignment.TEXT_LEFT, true).getTextEmitter(cache);
		gameOverStatsTitlesText.getCharoffset().add(0, 0.4f);
		gameOverStatsValuesText = GlobalUtils.createUIText(cache, "gameOverStatsValuesText", 128, GlobalLang.get("game.over.values"), Alignment.TEXT_RIGHT, true).getTextEmitter(cache);
		gameOverStatsValuesText.getCharoffset().add(0, 0.4f);
		gameOverText = scene.addEntity("gameOverText", GlobalUtils.createUIText(cache, "gameOverText", 16, GlobalLang.get("game.over"), Alignment.TEXT_CENTER), new Transform3DComponent(GAME_OVER_BASE, new Quaternionf(), new Vector3f(2.5f))).setActive(false);
		((TextMaterial) gameOverText.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getMesh().getMaterial()).setFgColor(new Vector4f(1, 0, 0, 1));
		gameOverStatsTitles = scene.addEntity("gameOverStatsTitles", new TextEmitterComponent(gameOverStatsTitlesText), new Transform3DComponent(GAME_OVER_TITLES_BASE)).setActive(false);
		gameOverStatsValues = scene.addEntity("gameOverStatsValues", new TextEmitterComponent(gameOverStatsValuesText), new Transform3DComponent(GAME_OVER_VALUES_BASE)).setActive(false);

		// GlobalUtils.compileMeshes(cache);

		cache.dump(System.err);
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		// health
		if (healthRestoreAcceptedProgress < 1) {
			healthRestoreAcceptedProgress = Math.clamp(0, 1, healthRestoreAcceptedProgress + dTime * ACCEPTED_ANIMATION_SPEED);

			((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(ACCEPTED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(healthRestoreAcceptedProgress), new Vector4f()));
		}

		if (healthRestoreDeniedProgress < 1) {
			healthRestoreDeniedProgress = Math.clamp(0, 1, healthRestoreDeniedProgress + dTime * DENIED_ANIMATION_SPEED);

			((TextMaterial) maxHealthIndicatorText.getMesh().getMaterial()).setFgColor(DENIED_COLOR_START.lerp(IDLE_COLOR_START, Interpolators.QUAD_IN_OUT.evaluate(healthRestoreDeniedProgress), new Vector4f()));
		}

		if (healthFillProgress < 1) {
			healthFillProgress = Math.clamp(0, 1, healthFillProgress + dTime * HEALTH_FILL_SPEED);

			HealthIndicatorMaterial mat = (HealthIndicatorMaterial) maxHealthIndicator.getComponent(MeshComponent.class).getMesh(cache).getMaterial();
			oldVisibleHealth = Math.lerp(visibleHealth, GlobalUtils.INSTANCE.playerData.getHealth(), Interpolators.QUART_OUT.evaluate(healthFillProgress));
			mat.setRedProgress(GlobalUtils.INSTANCE.playerData.getHealth() / GlobalUtils.INSTANCE.playerData.getMaxHealth());
			mat.setGreenProgress(oldVisibleHealth / GlobalUtils.INSTANCE.playerData.getMaxHealth());

			if (healthFillProgress >= 1) {
				visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
			}
		}

		if (healthEmptyProgress < 1) {
			healthEmptyProgress = Math.clamp(0, 1, healthEmptyProgress + dTime * HEALTH_FILL_SPEED);

			HealthIndicatorMaterial mat = (HealthIndicatorMaterial) maxHealthIndicator.getComponent(MeshComponent.class).getMesh(cache).getMaterial();
			visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
			mat.setGreenProgress(visibleHealth / GlobalUtils.INSTANCE.playerData.getMaxHealth());
			mat.setRedProgress(Math.lerp(oldVisibleHealth, visibleHealth, Interpolators.QUART_OUT.evaluate(healthEmptyProgress)) / GlobalUtils.INSTANCE.playerData.getMaxHealth());
		}

		if (healthRestoreClickProgress < 1) {
			healthRestoreClickProgress = Math.clamp(0, 1, healthRestoreClickProgress + dTime * CLICK_ANIMATION_SPEED);

			float change = Interpolators.QUAD_IN_OUT.evaluate(healthRestoreClickProgress);
			Vector2f offset = CLICK_OFFSET_END.mul(change, new Vector2f()).mul(CLICK_ANIMATION_AMPLITUDE);

			maxHealthIndicator.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(HEALTH_INDICATOR_BASE)).updateMatrix();
			maxHealthIndicatorTextEntity.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(offset.x, offset.y, 0).add(HEALTH_INDICATOR_TEXT_BASE)).updateMatrix();
		}

		if (gameEndActive) {

			if (gameEndProgress < 1) {
				gameEndProgress = Math.clamp(0, 1, gameEndProgress + dTime * DEATH_BG_DARKEN_SPEED);

				((FillMaterial) uiBG.getComponent(MeshComponent.class).getMesh(cache).getMaterial()).setColor(new Vector4f(0).lerp(GlobalConsts.TRANS_BG, Interpolators.QUAD_IN_OUT.evaluate(gameEndProgress)));
			}
			
			// uiBG.getComponent(Transform3DComponent.class).getTransform().rotate(0.1f, 0.1f, 0.1f).updateMatrix();

		}

	}

	@Override
	public void render(float dTime) {
		aminoAcidText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getAminoAcid()) + "")).updateText();
		glucoseText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getGlucose()) + "")).updateText();
		lipidText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getLipid()) + "")).updateText();

		maxHealthIndicatorText.setText(GlobalLang.get("ui.hp") + MathUtils.fillPrefix(2, '0', Math.clamp(0, 99, GlobalUtils.INSTANCE.playerData.getMaxHealth()) + "")).updateText();

		gameOverStatsTitlesText.setText(GlobalLang.get("game.over.titles")).updateText();
		gameOverStatsValuesText.setText(fillGameOverValues(GlobalLang.get("game.over.values"))).updateText();
	}

	private String fillGameOverValues(String str) {
		return str.replace("{MAX_HP}", Integer.toString(GlobalUtils.INSTANCE.playerData.getMaxHealth())).replace("{ENNEMIES_KILLED}", Integer.toString(GlobalUtils.INSTANCE.playerData.getEnnemyKillCount()))
				.replace("{ACHIEVEMENTS}", Integer.toString(GlobalUtils.INSTANCE.playerData.getAchievements().size())).replace("{MAX_ACHIEVEMENTS}", Integer.toString(Achievements.values().length));
	}

	public void setTreeViewActive(boolean b) {
		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent(c -> c.setPaused(b));

		gameEndActive = false;
		treeViewActive = b;

		uiBG.setActive(b);
	}

	public void startGameEndActive() {
		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent(c -> c.setPaused(true));

		treeViewActive = false;
		gameEndActive = true;

		gameEndProgress = 0;
		
		maxHealthIndicator.setActive(false);
		materialList.setActive(false);
		
		gameOverText.setActive(true);
		gameOverStatsTitles.setActive(true);
		gameOverStatsValues.setActive(true);

		uiBG.setActive(true);
	}

	public void endGameEndActive() {
		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent(c -> c.setPaused(false));

		treeViewActive = false;
		gameEndActive = false;

		maxHealthIndicator.setActive(true);
		materialList.setActive(true);
		
		gameOverText.setActive(false);
		gameOverStatsTitles.setActive(false);
		gameOverStatsValues.setActive(false);

		uiBG.setActive(false);
	}

	public void startHealthRestoreDenied() {
		healthRestoreClickProgress = healthRestoreDeniedProgress = 0;
	}

	public void startHealthRestoreAccepted() {
		healthRestoreClickProgress = healthRestoreAcceptedProgress = healthFillProgress = 0;
		healthEmptyProgress = 1;
		visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
	}

	public void startHealthEmpty() {
		healthEmptyProgress = 0;
		healthFillProgress = 1;
		visibleHealth = GlobalUtils.INSTANCE.playerData.getHealth();
		oldVisibleHealth = visibleHealth;
	}

}
