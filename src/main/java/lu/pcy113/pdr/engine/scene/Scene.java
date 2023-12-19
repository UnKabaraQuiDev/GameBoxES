package lu.pcy113.pdr.engine.scene;

import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.scene.camera.Camera;

public abstract class Scene
		implements
		UniqueID,
		Cleanupable,
		Renderable {

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
	public String getId() { return name; }

	public Camera getCamera() { return camera; }

}
