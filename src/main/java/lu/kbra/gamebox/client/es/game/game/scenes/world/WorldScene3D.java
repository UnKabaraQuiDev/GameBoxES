package lu.kbra.gamebox.client.es.game.game.scenes.world;

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
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.BackgroundShader;
import lu.kbra.gamebox.client.es.game.game.world.World;

public class WorldScene3D extends Scene3D {

	private CacheManager cache;
	private Window window;

	private float distance;
	
	private World world;
	private Entity background;

	public WorldScene3D(String name, CacheManager parentCache, Window window) {
		super(name);
		this.cache = new CacheManager("WorldScene3D", parentCache);
		this.window = window;
	}

	public void input(float dTime) {
		Camera3D cam = ((Camera3D) super.getCamera());
		cam.getPosition()
				.add(new Vector3f((window.isCharPress('r') ? 1 : 0) - (window.isCharPress('f') ? 1 : 0), (window.isCharPress('z') ? 1 : 0) - (window.isCharPress('s') ? 1 : 0), (window.isCharPress('d') ? 1 : 0) - (window.isCharPress('q') ? 1 : 0)));

		if (window.isJoystickPresent() && world != null && world.getPlayer() != null) {
			world.getPlayer().getTransform().getTransform().translateAdd(new Vector3f(window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_X), -window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y), 0));
			world.getPlayer().getTransform().getTransform().updateMatrix();
			
			placeCamera(GeoPlane.XY.projectToPlane(world.getPlayer().getTransform().getTransform().getTranslation()));
		}
		cam.updateMatrix();
	}

	public void setupGame() {
		if (world != null) {
			world.cleanup();
			world = null;
		}

		world = new World(cache);
		world.genWorld(new Vector2f(0), 10, 5 * 5 * 3 * 10).forEach(e -> this.addEntity(e.getClass().getSimpleName() + "-" + e.hashCode(), e));
	}

	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		super.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(10))));
		
		Mesh backgroundMesh = Mesh.newQuad("backgroundMesh", cache.getParent().loadOrGetMaterial(BackgroundShader.BackgroundMaterial.NAME, BackgroundShader.BackgroundMaterial.class,
				cache.getParent().loadOrGetSingleTexture("worldBgTexture", "./resources/textures/ui/defaultBG.png", TextureFilter.LINEAR)), new Vector2f(10));
		cache.addMesh(backgroundMesh);
		background = addEntity("worldBG", new MeshComponent(backgroundMesh), new Transform3DComponent());
		
		System.err.println("added gizmo: ");
		cache.dump(System.err);

		camera.getProjection().setPerspective(true);
		((Camera3D) camera).setUp(GameEngine.Y_POS);
		camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().update();

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(pos.x, pos.y, distance), new Vector3f(pos.x, pos.y, 0));
		background.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(pos.x, pos.y, -1)).updateMatrix();
		camera.updateMatrix();
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

}
