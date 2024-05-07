package lu.kbra.gamebox.client.es.game.game.scenes.world;

import java.util.HashMap;
import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.utils.ObjLoader;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.BackgroundShader;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;
import lu.kbra.gamebox.client.es.game.game.world.World;
import lu.pcy113.pclib.GlobalLogger;

public class WorldScene3D extends Scene3D {

	private CacheManager cache;
	private Window window;

	private float distance;

	private World world;
	private Entity background;

	private HashMap<Vector2f, List<Entity>> generatedChunks = new HashMap<>();
	private final int chunkSize = 20;

	public WorldScene3D(String name, CacheManager parentCache, Window window) {
		super(name);
		this.cache = new CacheManager("WorldScene3D", parentCache);
		this.window = window;
	}

	float size = 1;

	public void input(float dTime) {
		Camera3D cam = ((Camera3D) super.getCamera());
		cam.getPosition()
				.add(new Vector3f((window.isCharPress('r') ? 1 : 0) - (window.isCharPress('f') ? 1 : 0),
						(window.isCharPress('z') ? 1 : 0) - (window.isCharPress('s') ? 1 : 0),
						(window.isCharPress('d') ? 1 : 0) - (window.isCharPress('q') ? 1 : 0)));

		if (window.isJoystickPresent() && world != null && world.getPlayer() != null) {
			world.getPlayer().getTransform().getTransform().translateAdd(
					new Vector3f(window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_X),
							-window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y), 0));
			world.getPlayer().getTransform().getTransform().updateMatrix();

			placeCamera(GeoPlane.XY.projectToPlane(world.getPlayer().getTransform().getTransform().getTranslation()));
		}
		cam.updateMatrix();

		System.err
				.println("axis: "
						+ GlobalUtils.applyMinThreshold(
								window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X))
						+ " = " + size);
		size += (window.getJoystickButton(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_BUTTON_A) ? 1 : 0)
				- (window.getJoystickButton(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_BUTTON_B) ? 1 : 0);
		camera.getProjection().setSize(size * 5);
		camera.getProjection().update();
	}

	public void update(float dTime) {
		if(world != null) {
			world.update();
		}
	}

	public void setupGame() {
		if (world != null) {
			world.cleanup();
			world = null;
		}

		world = new World(this);
		for (int x = -5; x < 5; x++) {
			for (int y = -5; y < 5; y++) {
				genChunk(new Vector2f(x * chunkSize, y * chunkSize));
			}
		}
		generatedChunks.keySet().forEach(System.out::println);
	}

	private void genChunk(Vector2f center) {
		if (world == null)
			return;

		center.sub(center.x % chunkSize + (chunkSize / 2), center.y % chunkSize + (chunkSize / 2));

		if (generatedChunks.containsKey(center)) {
			return;
		}

		GlobalUtils.pushRender(new Runnable() {
			@Override
			public void run() {
				GlobalLogger.info("Generating chunk: " + center);

				List<Entity> gens = world.genChunk(center, chunkSize);
				// gens.forEach(t -> addEntity(t));
				generatedChunks.put(center, gens);
			}
		});
	}

	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		super.addEntity("grid_xyz", new GizmoComponent(axis),
				new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(10))))
				.setActive(true);

		Mesh backgroundMesh = Mesh.newQuad("backgroundMesh",
				cache.getParent().loadOrGetMaterial(BackgroundShader.BackgroundMaterial.NAME,
						BackgroundShader.BackgroundMaterial.class,
						cache.getParent().loadOrGetSingleTexture("worldBgTexture",
								"./resources/textures/ui/defaultBG.png", TextureFilter.LINEAR)),
				new Vector2f(16, 9).div(1));
		cache.addMesh(backgroundMesh);
		background = addEntity("worldBG", new MeshComponent(backgroundMesh),
				new Transform3DComponent(new Vector3f(0, 0, -1)), new RenderComponent(10)).setActive(false);

		System.err.println("added gizmo: ");
		cache.dump(System.err);

		camera.getProjection().setPerspective(false);
		((Camera3D) camera).setUp(GameEngine.Y_POS);
		// camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().setPerspective(false);
		camera.getProjection().setSize(size);
		camera.getProjection().update(1920, 1080);

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(pos.x, pos.y, distance), new Vector3f(pos.x, pos.y, 0));
		background.getComponent(Transform3DComponent.class).getTransform()
				.setTranslation(new Vector3f(pos.x, pos.y, -1)).updateMatrix();
		camera.updateMatrix();

		// genChunk(pos);
	}

	public void placeCamera(Vector2f pos, float distance) {
		this.distance = distance;
		placeCamera(pos);
	}

	public CacheManager getCache() {
		return cache;
	}

	public World getWorld() {
		return world;
	}

	public Window getWindow() {
		return window;
	}

}
