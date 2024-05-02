package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.FillShader.FillMaterial;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;

public class UISceneMajorUpgradeTree extends UISceneState {

	public UISceneMajorUpgradeTree(UIScene3D scene) {
		super(scene);
		
		Mesh bgMesh = Mesh.newQuad("uiBGMesh", cache.loadOrGetMaterial(FillMaterial.NAME, FillShader.FillMaterial.class, GlobalConsts.BG), new Vector2f(2));
		cache.addMesh(bgMesh);
		scene.addEntity("uiBG", new MeshComponent(bgMesh));
	}

	@Override
	public void input(float dTime) {

	}

}
