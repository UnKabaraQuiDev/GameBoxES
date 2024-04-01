package lu.kbra.gamebox.client.es.game.game.scenes.world;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.utils.ObjLoader;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellType;

public class WorldScene3D extends Scene3D {

	private CacheManager cache;
	private Window window;
	
	private float distance;

	public WorldScene3D(String name, CacheManager parentCache, Window window) {
		super(name);
		this.cache = new CacheManager(parentCache);
		this.window = window;
	}
	
	CellEntity ce;
	
	public void input(float dTime) {
		Camera3D cam = ((Camera3D) super.getCamera());
		cam.getPosition().add(new Vector3f(
				(window.isCharPress('r') ? 1 : 0) - (window.isCharPress('f') ? 1 : 0),
				(window.isCharPress('z') ? 1 : 0) - (window.isCharPress('s') ? 1 : 0),
				(window.isCharPress('d') ? 1 : 0) - (window.isCharPress('q') ? 1 : 0)
		));
		
		if(window.isJoystickPresent()) {
			ce.getTransform().getTransform().translateAdd(new Vector3f(
					0,
					window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y),
					window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_X)
			).negate());
			ce.getTransform().getTransform().updateMatrix();
			placeCamera(GeoPlane.YZ.projectToPlane(ce.getTransform().getTransform().getTranslation()));
		}
		cam.updateMatrix();
	}
	
	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		super.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(10))));

		ce = addCellEntity("player", CellType.PLAYER);

		camera.getProjection().setPerspective(true);
		camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().update();

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(distance, pos.x, pos.y), new Vector3f(0, pos.x, pos.y));
		camera.updateMatrix();
	}

	public void placeCamera(Vector2f pos, float distance) {
		this.distance = distance;
		placeCamera(pos);
	}

	private CellEntity addCellEntity(String name, CellType type) {
		CellEntity ce = CellEntity.load(cache, name, type);

		return (CellEntity) addEntity(name, ce);
	}

	public CacheManager getCache() {
		return cache;
	}

}
