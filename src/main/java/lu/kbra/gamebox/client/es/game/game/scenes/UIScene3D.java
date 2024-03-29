package lu.kbra.gamebox.client.es.game.game.scenes;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;

public class UIScene3D extends Scene3D {

	public UIScene3D(String name, CacheManager cache) {
		super(name);
	}

	public void setupScene() {
		((Camera3D) camera).setPosition(new Vector3f(-1, 0, 0));
		((Camera3D) camera).lookAt(new Vector3f(-5, 0, 0), new Vector3f(-1, 0, 0));
		camera.updateMatrix();

		camera.getProjection().update(1920, 1080);
	}

}
