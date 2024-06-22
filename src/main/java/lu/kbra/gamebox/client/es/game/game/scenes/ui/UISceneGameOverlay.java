package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import java.util.List;
import java.util.Optional;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pclib.logger.GlobalLogger;

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
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.game.game.data.Achievements;
import lu.kbra.gamebox.client.es.game.game.data.EvolutionTreeNode;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader.FillMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthIndicatorShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.HealthIndicatorShader.HealthIndicatorMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.MajorUpgradeTreeNodeShader.MajorUpgradeTreeNodeMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.MaterialListShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PriceListShader.PriceListMaterial;
import lu.kbra.gamebox.client.es.game.game.utils.ControllerInputWatcher;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UISceneGameOverlay extends UISceneState {

	private static final Vector2f CLICK_OFFSET_END = new Vector2f(0f, 0.1f);
	public static final float CLICK_ANIMATION_SPEED = 10f, ACCEPTED_ANIMATION_SPEED = 10f, DENIED_ANIMATION_SPEED = 10f, CLICK_ANIMATION_AMPLITUDE = 0.8f, HEALTH_FILL_SPEED = 1f;
	private static final Vector4f DENIED_COLOR_START = new Vector4f(1, 0, 0, 1), ACCEPTED_COLOR_START = new Vector4f(0, 1, 0, 1), IDLE_COLOR_START = new Vector4f(1, 1, 1, 1);
	public static final float BG_DARKEN_SPEED = 0.75f, SIDE_BG_SHOW_SPEED = 0.9f;

	private boolean treeViewActive = false, gameEndActive = false;

	private Entity uiBG, sideUiBG;
	private float showBGProgress = 1, showSideBGProgress = 1;
	private static final Vector3f SIDE_UI_START_POS = new Vector3f(7.2f, 0, GlobalConsts.UI_BG_HEIGHT + 0.01f), SIDE_UI_END_POS = new Vector3f(3.5f, 0, GlobalConsts.UI_BG_HEIGHT + 0.01f);

	// material list - - -
	private Entity materialList;
	private TextEmitter aminoAcidText, glucoseText, lipidText;

	// health indicator - - -
	private static final Vector3f HEALTH_INDICATOR_TEXT_BASE = new Vector3f(-2.8f, 2.28f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT), HEALTH_INDICATOR_BASE = new Vector3f(-3.9f, 2.4f, GlobalConsts.UI_COMPONENTS_HEIGHT);

	private Entity maxHealthIndicator, maxHealthIndicatorTextEntity;
	private TextEmitter maxHealthIndicatorText;
	private float healthRestoreClickProgress = 1, healthRestoreDeniedProgress = 1, healthRestoreAcceptedProgress = 1, healthFillProgress = 1, healthEmptyProgress = 1;
	private float visibleHealth = 1, oldVisibleHealth = 0;

	// game end - - -
	private static final Vector3f GAME_OVER_TEXT_BASE = new Vector3f(0, 1, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT), GAME_OVER_TITLES_BASE = new Vector3f(-4.2f, 0.5f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT),
			GAME_OVER_VALUES_BASE = new Vector3f(4.1f, 0.5f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT);

	private Entity gameOverText, gameOverStatsTitles, gameOverStatsValues;
	private TextEmitter gameOverStatsTitlesText, gameOverStatsValuesText;

	// major upgrade tree - - -
	public static final float UPGRADE_TREE_LOADING_SPEED = 25f, UPGRADE_TREE_UNLOADING_SPEED = 10f;
	private TextEmitter majorUpgradeTreePriceText;
	private Entity majorUpgradeTreeParent, majorUpgradeTreePrice;
	private int upgradeTreeSelection = -1;

	private ControllerInputWatcher cic;
	private boolean init = false;

	public UISceneGameOverlay(UIScene3D scene) {
		super("MajorUpgradeTree", scene);

		Mesh bgMesh = cache.newQuadMesh("uiBGMesh", cache.loadOrGetMaterial(FillMaterial.NAME, FillShader.FillMaterial.class, GlobalConsts.TRANS_BG), new Vector2f(12, 6));
		uiBG = scene.addEntity("uiBG", new MeshComponent(bgMesh), new Transform3DComponent(new Vector3f(0, 0, GlobalConsts.UI_BG_HEIGHT)), new RenderComponent(-10f)).setActive(false);

		FillMaterial fm = new FillMaterial("sideUiBgFill", (FillShader) cache.getRenderShader(FillShader.NAME), GlobalConsts.BG);
		cache.addMaterial(fm);
		Mesh sideBgMesh = cache.newQuadMesh("sideUiBGMesh", fm, new Vector2f(4f, 6f));
		sideUiBG = scene.addEntity("sideUiBG", new MeshComponent(sideBgMesh), new Transform3DComponent(new Vector3f(0, 0, GlobalConsts.UI_BG_HEIGHT + 0.01f)), new RenderComponent(-11f)).setActive(false);

		// material list - - -
		Mesh materialListMesh = GlobalUtils.loadCompiledMesh(cache, "materialList", () -> {
			return cache.loadMesh("materialList",
					cache.loadOrGetMaterial(MaterialListShader.MaterialListMaterial.NAME, MaterialListShader.MaterialListMaterial.class,
							cache.loadOrGetSingleTexture(MaterialListShader.MaterialListMaterial.TEXTURE_NAME, MaterialListShader.MaterialListMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
					MaterialListShader.MaterialListMaterial.MESH_PATH);
		});
		materialList = scene.addEntity("materialList", new MeshComponent(materialListMesh), new Transform3DComponent(new Vector3f(-4f, -2, GlobalConsts.UI_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(2))).setActive(true);

		aminoAcidText = GlobalUtils.createUIText(cache, "aminoAcidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		glucoseText = GlobalUtils.createUIText(cache, "glucoseText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		lipidText = GlobalUtils.createUIText(cache, "lipidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		materialList.addComponent(new SubEntitiesComponent(new Entity("glucoseText", new TextEmitterComponent(glucoseText), new Transform3DComponent(new Vector3f(-3f, -2.25f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT))),
				new Entity("lipidText", new TextEmitterComponent(lipidText), new Transform3DComponent(new Vector3f(-3f, -2.7f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT))),
				new Entity("aminoAcidText", new TextEmitterComponent(aminoAcidText), new Transform3DComponent(new Vector3f(-3f, -1.8f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT)))));

		// health indicator - - -
		Mesh healthIndicatorMesh = GlobalUtils.loadCompiledMesh(cache, "healthIndicator", () -> {
			return cache.loadMesh("healthIndicator",
					cache.loadOrGetMaterial(HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.class, HealthIndicatorShader.HealthIndicatorMaterial.HEALTH_NAME,
							cache.loadOrGetRenderShader(HealthIndicatorShader.NAME, HealthIndicatorShader.class),
							cache.loadOrGetSingleTexture(HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_NAME, HealthIndicatorShader.HealthIndicatorMaterial.TEXTURE_HEALTH_PATH, TextureFilter.NEAREST)),
					HealthIndicatorShader.HealthIndicatorMaterial.MESH_HEALTH_PATH);
		});
		maxHealthIndicator = scene.addEntity("healthIndicator", new MeshComponent(healthIndicatorMesh),
				new Transform3DComponent(new Vector3f(CLICK_OFFSET_END.x, CLICK_OFFSET_END.y, 0).add(HEALTH_INDICATOR_BASE), new Quaternionf(), new Vector3f(1.5f)));

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
		gameOverText = scene
				.addEntity("gameOverText", GlobalUtils.createUIText(cache, "gameOverText", 16, GlobalLang.get("game.over"), Alignment.TEXT_CENTER), new Transform3DComponent(GAME_OVER_TEXT_BASE, new Quaternionf(), new Vector3f(2.5f)))
				.setActive(false);
		((TextMaterial) gameOverText.getComponent(TextEmitterComponent.class).getTextEmitter(cache).getMesh().getMaterial()).setFgColor(new Vector4f(1, 0, 0, 1));
		gameOverStatsTitles = scene.addEntity("gameOverStatsTitles", new TextEmitterComponent(gameOverStatsTitlesText), new Transform3DComponent(GAME_OVER_TITLES_BASE)).setActive(false);
		gameOverStatsValues = scene.addEntity("gameOverStatsValues", new TextEmitterComponent(gameOverStatsValuesText), new Transform3DComponent(GAME_OVER_VALUES_BASE)).setActive(false);

		// major upgrade tree - - -
		Mesh majorUpgradeTreeParentMesh = GlobalUtils.loadCompiledMesh(cache, "upgradeTreeNode", () -> {
			return cache.loadMesh("upgradeTreeNodeParent", cache.loadOrGetMaterial(MajorUpgradeTreeNodeMaterial.NAME, MajorUpgradeTreeNodeMaterial.class,
					cache.loadOrGetSingleTexture(MajorUpgradeTreeNodeMaterial.NAME, "./resources/textures/ui/icons_list.png", TextureFilter.NEAREST)), "./resources/models/ui/upgrade_tree_node.obj");
		});
		majorUpgradeTreeParent = scene.addEntity("majorUpgradeTreeParent", new MeshComponent(majorUpgradeTreeParentMesh),
				new Transform3DComponent(new Vector3f(-2f, 0, GlobalConsts.UI_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(3f)));
		majorUpgradeTreeParent.setActive(false);

		Mesh majorUpgradeTreeHigherMesh = GlobalUtils.loadCompiledMesh(cache, "upgradeTreeNode", "upgradeTreeNodeTop");
		Mesh majorUpgradeTreeLowerMesh = GlobalUtils.loadCompiledMesh(cache, "upgradeTreeNode", "upgradeTreeNodeLow");

		majorUpgradeTreeParent.addComponent(
				new SubEntitiesComponent(new Entity("upgradeTreeNodeTop", new MeshComponent(majorUpgradeTreeHigherMesh), new Transform3DComponent(new Vector3f(0, 2f, GlobalConsts.UI_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(3f))),
						new Entity("upgradeTreeNodeLow", new MeshComponent(majorUpgradeTreeLowerMesh), new Transform3DComponent(new Vector3f(0, -2f, GlobalConsts.UI_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(3f)))));

		Mesh majorUpgradeTreePriceMesh = GlobalUtils.loadCompiledMesh(cache, "upgradeTreePrice", () -> {
			return cache.loadMesh("upgradeTreePrice",
					cache.loadOrGetMaterial(PriceListMaterial.NAME, PriceListMaterial.class, cache.loadOrGetSingleTexture(PriceListMaterial.TEXTURE_NAME, PriceListMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
					"./resources/models/ui/price_list.obj");
		});

		majorUpgradeTreePrice = scene.addEntity("majorUpgradeTreePrice", new MeshComponent(majorUpgradeTreePriceMesh), new Transform3DComponent(new Vector3f(4f, 2f, GlobalConsts.UI_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(2f)));
		majorUpgradeTreePrice.setActive(false);

		majorUpgradeTreePriceText = GlobalUtils.createUIText(cache, "majorUpgradeTreePriceText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);

		majorUpgradeTreePrice.addComponent(new SubEntitiesComponent(new Entity("majorUpgradeTreePriceText", new TextEmitterComponent(majorUpgradeTreePriceText),
				new Transform3DComponent(new Vector3f(4.95f, 1.95f, GlobalConsts.UI_OVER_COMPONENTS_HEIGHT), new Quaternionf(), new Vector3f(1.3f)))));

		// GlobalUtils.compileMeshes(cache);

		cic = new ControllerInputWatcher();
		cic.setSkipWaitingForNone(true);

		cache.dump(System.err);
		init = true;
	}

	@Override
	public void input(float dTime) {
		if (treeViewActive) {
			if (window.isJoystickPresent()) { // joystick
				if (!window.updateGamepad(0))
					return;

				cic.updateButton(window.getGamepad());
			} else { // window
				cic.updateButton(window);
			}

			// MajorUpgradeTreeNodeMaterial matParent = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(MeshComponent.class).getMesh(cache).getMaterial());
			MajorUpgradeTreeNodeMaterial matTop = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(SubEntitiesComponent.class).getEntities().get(0).getComponent(MeshComponent.class).getMesh(cache).getMaterial());
			MajorUpgradeTreeNodeMaterial matLow = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(SubEntitiesComponent.class).getEntities().get(1).getComponent(MeshComponent.class).getMesh(cache).getMaterial());

			if (cic.hasNextButton()) {
				Button btn = cic.consumeButton();

				upgradeTreeSelection = btn.equals(Button.NORTH) ? 0 : btn.equals(Button.SOUTH) ? 1 : -1;

				updateTreeInfo();

				matTop.setProgress(matTop.getProgress() + (btn.equals(Button.NORTH) ? 1f / UPGRADE_TREE_LOADING_SPEED : -1f / UPGRADE_TREE_UNLOADING_SPEED));
				matLow.setProgress(matLow.getProgress() + (btn.equals(Button.SOUTH) ? 1f / UPGRADE_TREE_LOADING_SPEED : -1f / UPGRADE_TREE_UNLOADING_SPEED));
			} else {
				matTop.setProgress(matTop.getProgress() - 1f / UPGRADE_TREE_UNLOADING_SPEED);
				matLow.setProgress(matLow.getProgress() - 1f / UPGRADE_TREE_UNLOADING_SPEED);
			}
			matTop.setProgress(Math.clamp(0, 1, matTop.getProgress()));
			matLow.setProgress(Math.clamp(0, 1, matLow.getProgress()));

			if (upgradeTreeSelection != -1 && (matTop.getProgress() == 1 || matLow.getProgress() == 1)) {
				if (GlobalUtils.INSTANCE.playerData.selectUpgrade(upgradeTreeSelection)) {
					setTreeViewActive(false);
				}
			}
		}
	}

	private void updateTreeInfo() {
		// TODO
	}

	@Override
	public void update(float dTime) {
		if(!init) {
			return;
		}
		
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

		if (gameEndActive || treeViewActive) {

			if (showBGProgress < 1) {
				showBGProgress = Math.clamp(0, 1, showBGProgress + dTime * BG_DARKEN_SPEED);

				((FillMaterial) uiBG.getComponent(MeshComponent.class).getMesh(cache).getMaterial()).setColor(new Vector4f(0).lerp(GlobalConsts.TRANS_BG, Interpolators.QUAD_IN_OUT.evaluate(showBGProgress)));
			}

			if (showSideBGProgress < 1) {
				showSideBGProgress = Math.clamp(0, 1, showSideBGProgress + dTime * SIDE_BG_SHOW_SPEED);

				sideUiBG.getComponent(Transform3DComponent.class).getTransform().setTranslation(SIDE_UI_START_POS.lerp(SIDE_UI_END_POS, Interpolators.QUAD_IN_OUT.evaluate(showSideBGProgress), new Vector3f())).updateMatrix();
			}

		} else {
			if (showBGProgress > 0) {
				showBGProgress = Math.clamp(0, 1, showBGProgress - dTime * BG_DARKEN_SPEED);

				((FillMaterial) uiBG.getComponent(MeshComponent.class).getMesh(cache).getMaterial()).setColor(new Vector4f(0).lerp(GlobalConsts.TRANS_BG, Interpolators.QUAD_IN_OUT.evaluate(showBGProgress)));
			}

			if (showSideBGProgress > 0) {
				showSideBGProgress = Math.clamp(0, 1, showSideBGProgress - dTime * SIDE_BG_SHOW_SPEED);

				sideUiBG.getComponent(Transform3DComponent.class).getTransform().setTranslation(SIDE_UI_START_POS.lerp(SIDE_UI_END_POS, Interpolators.CIRC_IN.evaluate(showSideBGProgress), new Vector3f())).updateMatrix();
			}
		}

		if (gameEndActive && showBGProgress >= 0.999f && GlobalUtils.anyJoystickButton()) {
			GlobalUtils.triggerNewStartMenu();
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
				.replace("{ACHIEVEMENTS}", Integer.toString(GlobalUtils.INSTANCE.playerData.getAchievements().size())).replace("{MAX_ACHIEVEMENTS}", Integer.toString(Achievements.values().length))
				.replace("{SCORE}", Long.toString(GlobalUtils.INSTANCE.playerData.getScore()));
	}

	public void setTreeViewActive(boolean b) {
		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent(c -> c.setPaused(b));

		gameEndActive = false;
		treeViewActive = b;

		showBGProgress = 0;
		showSideBGProgress = 0;

		uiBG.setActive(b);
		sideUiBG.setActive(b);
		majorUpgradeTreeParent.setActive(b);
		majorUpgradeTreePrice.setActive(b);

		if (b) {
			if (GlobalUtils.INSTANCE.playerData.getCurrentTreeNode() == null) {
				GlobalLogger.warning("Current tree node is null !");
				return;
			}

			EvolutionTreeNode current = GlobalUtils.INSTANCE.playerData.getCurrentTreeNode();
			List<EvolutionTreeNode> children = current.getChildren();

			majorUpgradeTreePriceText.setText(MathUtils.fillPrefix(4, '0', Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getUpgradePrice()) + ""));
			GlobalUtils.pushRender(majorUpgradeTreePriceText::updateText);

			MajorUpgradeTreeNodeMaterial matParent = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(MeshComponent.class).getMesh(cache).getMaterial());
			MajorUpgradeTreeNodeMaterial matTop = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(SubEntitiesComponent.class).getEntities().get(0).getComponent(MeshComponent.class).getMesh(cache).getMaterial());
			MajorUpgradeTreeNodeMaterial matLow = ((MajorUpgradeTreeNodeMaterial) majorUpgradeTreeParent.getComponent(SubEntitiesComponent.class).getEntities().get(1).getComponent(MeshComponent.class).getMesh(cache).getMaterial());

			if (current.isLeaf()) {
				GlobalLogger.info("Upgrade tree ended");
				return;
			}

			matParent.setIcon(current.getIconId());
			matParent.setProgress(1);
			matParent.setTint(new Vector4f(0, 1, 0, 1));

			matTop.setIcon(children.get(0).getIconId());
			GlobalLogger.info("Upgrade tree n1: " + children.get(0));
			if (children.size() > 1) {
				matLow.setIcon(children.get(1).getIconId());
				GlobalLogger.info("Upgrade tree n2: " + children.get(1));
			} else {
				matLow.setIcon(children.get(0).getIconId());
				GlobalLogger.info("Upgrade tree n2: " + children.get(0));
			}

			matTop.setProgress(0);
			matLow.setProgress(0);

			((TextMaterial) majorUpgradeTreePriceText.getMesh().getMaterial()).setFgColor(GlobalUtils.INSTANCE.playerData.canSelectUpgrade() ? new Vector4f(0, 0.6f, 0, 1) : new Vector4f(0.6f, 0, 0, 1));

			matTop.setTint(((TextMaterial) majorUpgradeTreePriceText.getMesh().getMaterial()).getFgColor());
			matLow.setTint(matTop.getTint());

			GlobalLogger.info(matTop.getIcon() + " and low: " + matLow.getIcon());
		}
	}

	public void startGameEndActive() {
		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent(c -> c.setPaused(true));

		treeViewActive = false;
		gameEndActive = true;

		showBGProgress = 0;

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

	public boolean isTreeViewActive() {
		return treeViewActive;
	}

	public void toggleTreeViewActive() {
		setTreeViewActive(!treeViewActive);
	}

}
