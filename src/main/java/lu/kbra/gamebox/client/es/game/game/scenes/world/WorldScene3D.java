package lu.kbra.gamebox.client.es.game.game.scenes.world;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.utils.ObjLoader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellDescriptor;
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
		cam.getPosition()
				.add(new Vector3f((window.isCharPress('r') ? 1 : 0) - (window.isCharPress('f') ? 1 : 0), (window.isCharPress('z') ? 1 : 0) - (window.isCharPress('s') ? 1 : 0), (window.isCharPress('d') ? 1 : 0) - (window.isCharPress('q') ? 1 : 0)));

		if (window.isJoystickPresent()) {
			ce.getTransform().getTransform().translateAdd(new Vector3f(
					window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_X),
					-window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y),
					0));
			ce.getTransform().getTransform().updateMatrix();
			placeCamera(GeoPlane.XY.projectToPlane(ce.getTransform().getTransform().getTranslation()));
		}
		cam.updateMatrix();
	}

	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		super.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(10))));

		ce = addCellEntity("player", new CellDescriptor(CellType.PLAYER, "noname", "player"));

		camera.getProjection().setPerspective(true);
		((Camera3D) camera).setUp(GameEngine.Y_POS);
		camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().update();

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(pos.x, pos.y, distance), new Vector3f(pos.x, pos.y, 0));
		camera.updateMatrix();
	}

	public void placeCamera(Vector2f pos, float distance) {
		this.distance = distance;
		placeCamera(pos);
	}

	private CellEntity addCellEntity(String name, CellDescriptor desc) {
		CellEntity ce = CellEntity.load(cache, name, desc);

		return (CellEntity) addEntity(name, ce);
	}

	public CacheManager getCache() {
		return cache;
	}

}
