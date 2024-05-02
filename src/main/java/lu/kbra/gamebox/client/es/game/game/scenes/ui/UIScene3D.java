package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity.UISliderComponent;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UIScene3D extends Scene3D {

	private CacheManager cache;
	private Window window;

	private UISceneState state;
	private UISceneMajorUpgradeTree treeState;

	public UIScene3D(String name, CacheManager cache, Window window) {
		super(name);
		this.cache = new CacheManager(cache);
		this.window = window;
	}

	public void setupStartMenu() {
		state = new UISceneStartMenuState(this);
	}

	public void setupGame() {
		treeState = new UISceneMajorUpgradeTree(this);
	}
	
	public void clearMainMenu() {
		if(state instanceof UISceneStartMenuState) {
			GlobalUtils.cleanup(state);
			state = null;
		}
	}
	
	public void showUpgradeTree(boolean b) {
		if (b) {
			state = treeState;
		} else {
			state = null;
		}
	}

	public void input(float dTime) {
		if (state != null) {
			state.input(dTime);
		}
	}

	public void setupScene() {
		camera.getProjection().setPerspective(false);
		camera.getProjection().setSize(180f);
		camera.getProjection().update(1920, 1080);

		((Camera3D) camera).setPosition(new Vector3f(0, 0, 5));
		((Camera3D) camera).setUp(GameEngine.Y_POS);
		((Camera3D) camera).lookAt(new Vector3f(0, 0, 5), new Vector3f(0, 0, 0));
		((Camera3D) camera).updateMatrix();
	}

	public CacheManager getCache() {
		return cache;
	}

	public Window getWindow() {
		return window;
	}

}
