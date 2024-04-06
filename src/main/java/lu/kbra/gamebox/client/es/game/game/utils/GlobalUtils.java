package lu.kbra.gamebox.client.es.game.game.utils;

import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.render.GizmoRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.InstanceEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.MeshRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene2DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene3DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.TextEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.geo.Ray;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;

public class GlobalUtils {
	
	public static GameBoxES INSTANCE;
	
	public static void init(GameBoxES gameBoxES) {
		INSTANCE = gameBoxES;
	}
	
	public static float joystickThreshold = 0.1f;
	
	public static float applyMinThreshold(float value) {
		return PDRUtils.applyMinThreshold(value, joystickThreshold);
	}
	
	public static void registerRenderers() {
		INSTANCE.cache.addRenderer(new MeshRenderer());
		INSTANCE.cache.addRenderer(new GizmoRenderer());
		INSTANCE.cache.addRenderer(new InstanceEmitterRenderer());
		INSTANCE.cache.addRenderer(new Scene2DRenderer());
		INSTANCE.cache.addRenderer(new Scene3DRenderer());
		INSTANCE.cache.addRenderer(new TextEmitterRenderer());
	}
	
	public static void projectWorld(Consumer<Vector3f> consumer) {
		project(GeoPlane.XY, consumer, INSTANCE.worldScene);
	}
	
	public static void projectWorld(GeoPlane plane, Consumer<Vector3f> consumer) {
		project(plane, consumer, INSTANCE.worldScene);
	}
	
	public static void projectUI(Consumer<Vector3f> consumer) {
		project(GeoPlane.XY, consumer, INSTANCE.uiScene);
	}
	
	public static void projectUI(GeoPlane plane, Consumer<Vector3f> consumer) {
		project(plane, consumer, INSTANCE.uiScene);
	}
	
	public static void project(GeoPlane plane, Consumer<Vector2f> consumer, Scene3D scene) {
		INSTANCE.createTask(GameEngine.QUEUE_RENDER)
		.exec((t) -> {
			int[] viewport = new int[4];
			GL40.glGetIntegerv(GL40.GL_VIEWPORT, viewport);
			return viewport;
		}).then((viewport) -> {
			Ray ray = scene.getCamera().projectRay(new Vector2f(INSTANCE.window.getMousePos()), (int[]) viewport);

			Vector3f pos = scene.getCamera().projectPlane(ray, plane);
			
			consumer.accept(GeoPlane.getByNormal(((Camera3D) scene.getCamera()).getRotation()).projectToPlane(pos));
			return null;
		}).push();
	}
	
	public static void project(GeoPlane plane, Consumer<Vector3f> consumer, Scene scene) {
		INSTANCE.createTask(GameEngine.QUEUE_RENDER)
		.exec((t) -> {
			int[] viewport = new int[4];
			GL40.glGetIntegerv(GL40.GL_VIEWPORT, viewport);
			return viewport;
		}).then((viewport) -> {
			Ray ray = scene.getCamera().projectRay(new Vector2f(INSTANCE.window.getMousePos()), (int[]) viewport);

			Vector3f pos = scene.getCamera().projectPlane(ray, plane);
			
			consumer.accept(pos);
			return null;
		}).push();
	}

}
