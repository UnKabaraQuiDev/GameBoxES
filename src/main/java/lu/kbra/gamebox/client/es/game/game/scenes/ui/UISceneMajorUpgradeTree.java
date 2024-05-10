package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader.FillMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.MaterialListShader;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UISceneMajorUpgradeTree extends UISceneState {

	private boolean active = false;

	private Entity uiBG;
	
	private Entity materialList;
	private TextEmitter aminoAcidText, glucoseText, lipidText;

	public UISceneMajorUpgradeTree(UIScene3D scene) {
		super("MajorUpgradeTree", scene);

		Mesh bgMesh = cache.newQuadMesh("uiBGMesh", cache.loadOrGetMaterial(FillMaterial.NAME, FillShader.FillMaterial.class, GlobalConsts.BG), new Vector2f(2));
		uiBG = scene.addEntity("uiBG", new MeshComponent(bgMesh)).setActive(false);

		Mesh materialListMesh = cache.loadMesh(
				"materialList",
				cache.loadOrGetMaterial(
						MaterialListShader.MaterialListMaterial.NAME,
						MaterialListShader.MaterialListMaterial.class,
						cache.loadOrGetSingleTexture(
								MaterialListShader.MaterialListMaterial.TEXTURE_NAME,
								MaterialListShader.MaterialListMaterial.TEXTURE_PATH,
								TextureFilter.NEAREST
						)
				),
				MaterialListShader.MaterialListMaterial.MESH_PATH);
		materialList = scene.addEntity("materialList", new MeshComponent(materialListMesh), new Transform3DComponent(new Vector3f(-4f, -2, 1), new Quaternionf(), new Vector3f(2)), new RenderComponent(15));
		aminoAcidText = GlobalUtils.createUIText(cache, "aminoAcidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		glucoseText = GlobalUtils.createUIText(cache, "glucoseText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		lipidText = GlobalUtils.createUIText(cache, "lipidText", 4, "0000", Alignment.TEXT_RIGHT).getTextEmitter(cache);
		materialList.addComponent(new SubEntitiesComponent(
				new Entity("aminoAcidText", new TextEmitterComponent(aminoAcidText), new Transform3DComponent(new Vector3f(-3.1f, -1.8f, 1.1f))),
				new Entity("glucoseText", new TextEmitterComponent(glucoseText), new Transform3DComponent(new Vector3f(-3.1f, -2.25f, 1.1f))),
				new Entity("lipidText", new TextEmitterComponent(lipidText), new Transform3DComponent(new Vector3f(-3.1f, -2.7f, 1.1f)))
		));
		
		cache.dump(System.err);
	}

	@Override
	public void input(float dTime) {

	}

	@Override
	public void update(float dTime) {
		
	}
	
	@Override
	public void render(float dTime) {
		aminoAcidText.setText(Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getAminoAcid())+"").updateText();
		glucoseText.setText(Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getGlucose())+"").updateText();
		lipidText.setText(Math.clamp(0, 9999, GlobalUtils.INSTANCE.playerData.getLipid())+"").updateText();
	}

	public void setActive(boolean b) {
		active = b;

		uiBG.setActive(b);
	}

}
