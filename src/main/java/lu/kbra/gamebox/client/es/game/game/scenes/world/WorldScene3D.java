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
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.BackgroundShader;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UISceneGameOverlay;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;
import lu.kbra.gamebox.client.es.game.game.world.World;

public class WorldScene3D extends Scene3D {

	private static final long MIN_UPGRADE_DELAY = 250;

	private CacheManager cache;
	private Window window;

	private float cameraDistance;

	private long lastHealthUpgrade = System.currentTimeMillis();
	private long lastSpeedUpgrade = System.currentTimeMillis();

	private World world;
	private Entity background;

	public WorldScene3D(String name, CacheManager parentCache, Window window) {
		super(name);
		this.cache = new CacheManager("WorldScene3D", parentCache);
		this.window = window;
	}

	float size = 1;

	public void input(float dTime) {
		if (world != null) {
			world.input(dTime);
		}

		if (window.getJoystickButton(GLFW.GLFW_JOYSTICK_1, GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER) && System.currentTimeMillis() - lastHealthUpgrade >= MIN_UPGRADE_DELAY) {
			if (GlobalUtils.INSTANCE.playerData.canRestoreHealth()) {
				((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthRestoreAccepted();
				lastHealthUpgrade = System.currentTimeMillis();
			} else {
				((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthRestoreDenied();
			}
			GlobalUtils.INSTANCE.playerData.restoreHealth();
		}
	}

	public void update(float dTime) {
		if (world != null) {
			world.continueWorldGen(World.GEN_CIRCLE_SIZE);

			world.update(dTime);

			placeCamera(GeoPlane.XY.projectToPlane(world.getPlayer().getTransform().getTransform().getTranslation()));
		}
	}

	public void render(float dTime) {
		if (world != null) {
			world.render(dTime);
		}
	}

	public void setupGame() {
		if (world != null) {
			world.cleanup();
			world = null;
		}

		world = new World(this, Math.random());
		// world.continueWorldGen(4);

		camera.getProjection().setSize(55).update();
	}

	public Entity axis;

	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		this.axis = super.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(1)))).setActive(true);

		Mesh backgroundMesh = Mesh.newQuad("backgroundMesh", cache.getParent().loadOrGetMaterial(BackgroundShader.BackgroundMaterial.NAME, BackgroundShader.BackgroundMaterial.class,
				cache.getParent().loadOrGetSingleTexture("worldBgTexture", "./resources/textures/ui/background_water.png", TextureFilter.LINEAR)), new Vector2f(40));
		cache.addMesh(backgroundMesh);
		background = addEntity("worldBG", new MeshComponent(backgroundMesh), new Transform3DComponent(new Vector3f(0, 0, -1)), new RenderComponent(10)).setActive(true);

		camera.getProjection().setPerspective(false);
		((Camera3D) camera).setUp(GameEngine.Y_POS);
		// camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().setPerspective(false);
		camera.getProjection().setSize(size);
		GlobalUtils.setFixedRatio(camera);

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(pos.x, pos.y, cameraDistance), new Vector3f(pos.x, pos.y, 0));
		background.getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(pos.x, pos.y, -1)).updateMatrix();
		/* if (world != null) {
			world.getPlayer().getComponent(Transform3DComponent.class).getTransform().setTranslation(new Vector3f(pos.x, pos.y, 1)).updateMatrix();
		}*/ 
		camera.updateMatrix();
	}

	public void placeCamera(Vector2f pos, float distance) {
		this.cameraDistance = distance;
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
