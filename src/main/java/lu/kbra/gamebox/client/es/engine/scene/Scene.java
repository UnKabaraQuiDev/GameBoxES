package lu.kbra.gamebox.client.es.engine.scene;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera;

public abstract class Scene implements UniqueID, Cleanupable, Renderable {

	protected String name;
	protected Camera camera;

	public Scene(String name, Camera cam) {
		this.name = name;
		this.camera = cam;
	}

	@Override
	public void cleanup() {
		if (name != null) {
			name = null;
		}
	}

	@Override
	public String getId() {
		return name;
	}

	public Camera getCamera() {
		return camera;
	}

}
