package lu.kbra.gamebox.client.es.game.game.utils;

import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.graph.render.GizmoRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.InstanceEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.MeshRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene3DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.TextEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.geo.Ray;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;

public class GlobalUtils {
	
	public static GameBoxES INSTANCE;
	private static GameEngine engine;
	
	public static void init(GameBoxES gameBoxES, GameEngine e) {
		INSTANCE = gameBoxES;
		engine = e;
	}
	
	public static float joystickThreshold = 0.1f;
	
	public static float applyMinThreshold(float value) {
		return PDRUtils.applyMinThreshold(value, joystickThreshold);
	}
	
	public static void registerRenderers() {
		INSTANCE.cache.addRenderer(new MeshRenderer());
		INSTANCE.cache.addRenderer(new GizmoRenderer());
		INSTANCE.cache.addRenderer(new InstanceEmitterRenderer());
		// INSTANCE.cache.addRenderer(new Scene2DRenderer());
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

	public static void requestQuit() {
		engine.getWindow().setWindowShouldClose(true);
	}

	public static MeshComponent createUIButton(CacheManager cache, String name, String btnName) {
		final String cacheName = "btn-"+btnName;
		
		SingleTexture txt = cache.loadOrGetSingleTexture(cacheName, FileUtils.RESOURCES+FileUtils.TEXTURES+"ui/btns/"+btnName, TextureFilter.NEAREST);
		
		Mesh mesh = Mesh.newQuad(name, cache.loadMaterial(UIButtonShader.UIButtonMaterial.class, txt), txt.getNormalizedSize2D());
		
		cache.addMesh(mesh);
		return new MeshComponent(mesh);
	}

	public static TextEmitterComponent createUIText(CacheManager cache, String name, int bufferSize, String txt, Alignment align) {
		TextMaterial mat = new TextMaterial("TextMaterial-" + GlobalConsts.TEXT_TEXTURE + "-" + name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GlobalConsts.TEXT_TEXTURE));
		cache.addMaterial(mat);
		
		TextEmitter text = new TextEmitter(name, mat, bufferSize, txt, new Vector2f(0.35f, 0.5f));
		text.setAlignment(align);
		text.createDrawBuffer();
		text.updateText();
		cache.addTextEmitter(text);
		return new TextEmitterComponent(text);
	}

	public static void updateText(final TextEmitter textEmitter) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			boolean res = textEmitter.updateText();
			System.err.println(res);
			return null;
		}).push();
	}

}
