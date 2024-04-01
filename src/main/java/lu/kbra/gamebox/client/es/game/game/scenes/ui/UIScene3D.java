package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.entities.UISliderEntity.UISliderComponent;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UIScene3D extends Scene3D {

	private CacheManager cache;
	private Window window;

	public UIScene3D(String name, CacheManager cache, Window window) {
		super(name);
		this.cache = new CacheManager(cache);
		this.window = window;
	}

	UISliderEntity uiSliderEntity;

	public void setupStartMenu() {
		uiSliderEntity = new UISliderEntity(cache, new Vector2f(2.5f, 0.5f), new Vector2f(0, 1), 0.1f, 0.5f, new Transform3D(new Quaternionf().rotateXYZ(0, (float) Math.toRadians(90f), 0)));
		addEntity("uiSlider", uiSliderEntity);
	}

	public void input(float dTime) {

		GlobalUtils.projectUI(pos -> {
			Vector2f p2 = GeoPlane.YZ.projectToPlane(pos);
			if (uiSliderEntity.getComponent(UISliderComponent.class).contains(p2)) {
				if (window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					uiSliderEntity.getComponent(UISliderComponent.class).click(p2);
				}
				uiSliderEntity.getComponent(UISliderComponent.class).hover(p2);
			}
		});

	}

	public void setupScene() {
		camera.getProjection().setPerspective(false);
		camera.getProjection().setSize(180f);
		camera.getProjection().update(1920, 1080);

		((Camera3D) camera).setPosition(new Vector3f(5, 0, 0));
		((Camera3D) camera).lookAt(new Vector3f(5, 0, 0), new Vector3f(0, 0, 0));
		((Camera3D) camera).updateMatrix();
	}

	public CacheManager getCache() {
		return cache;
	}

}
