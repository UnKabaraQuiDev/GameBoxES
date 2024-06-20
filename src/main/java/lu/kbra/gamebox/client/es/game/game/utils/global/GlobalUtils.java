package lu.kbra.gamebox.client.es.game.game.utils.global;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengles.GLES30;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.QuadMesh;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.graph.render.GizmoRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.InstanceEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.MeshRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.Scene3DRenderer;
import lu.kbra.gamebox.client.es.engine.graph.render.TextEmitterRenderer;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTask;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTaskFunction;
import lu.kbra.gamebox.client.es.engine.impl.nexttask.NextTaskWorker;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.FloatAttribArrayDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.MaterialDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.MeshDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.QuadMeshDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.UIntAttribArrayDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vec2fAttribArrayDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vec3fAttribArrayDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vector2fDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.decoder.Vector3fDecoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.FloatAttribArrayEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.MaterialEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.MeshEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.QuadMeshEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.UIntAttribArrayEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.Vec2fAttribArrayEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.Vec3fAttribArrayEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.Vector2fEncoder;
import lu.kbra.gamebox.client.es.engine.utils.codec.encoder.Vector3fEncoder;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.geo.Ray;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;
import lu.kbra.gamebox.client.es.game.game.render.shaders.UIButtonShader;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UISceneGameOverlay;
import lu.kbra.gamebox.client.es.game.game.utils.GameState;

public class GlobalUtils {

	private static final int PROJECTION_WIDTH = 1920;
	private static final int PROJECTION_HEIGHT = 1080;

	private static CodecManager cm;
	public static CacheManager currentLoadCache;

	public static GameBoxES INSTANCE;
	private static GameEngine engine;

	private static NextTaskWorker workers;

	public static void init(GameBoxES gameBoxES, GameEngine e) {
		INSTANCE = gameBoxES;
		currentLoadCache = gameBoxES.cache;
		engine = e;

		enableWorkers();
	}

	public static float joystickThreshold = 0.1f;

	public static float applyMinThreshold(float value) {
		return PDRUtils.applyMinThreshold(value, joystickThreshold);
	}

	public static void registerCodecs() {
		cm = CodecManager.base();
		// cm.register(new AttribArrayEncoder(), (short) 13);
		cm.register(new UIntAttribArrayEncoder(), new UIntAttribArrayDecoder(), (short) 14);
		cm.register(new FloatAttribArrayEncoder(), new FloatAttribArrayDecoder(), (short) 15);
		cm.register(new Vec3fAttribArrayEncoder(), new Vec3fAttribArrayDecoder(), (short) 16);
		cm.register(new QuadMeshEncoder(), new QuadMeshDecoder(), (short) 17);
		cm.register(new MeshEncoder(), new MeshDecoder(), (short) 12);
		cm.register(new MaterialEncoder(), new MaterialDecoder(), (short) 18);
		cm.register(new Vector3fEncoder(), new Vector3fDecoder(), (short) 19);
		cm.register(new Vector2fEncoder(), new Vector2fDecoder(), (short) 20);
		cm.register(new Vec2fAttribArrayEncoder(), new Vec2fAttribArrayDecoder(), (short) 21);
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
		INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			int[] viewport = new int[4];
			GLES30.glGetIntegerv(GLES30.GL_VIEWPORT, viewport);
			return viewport;
		}).then((viewport) -> {
			Ray ray = scene.getCamera().projectRay(new Vector2f(INSTANCE.window.getMousePos()), (int[]) viewport);

			Vector3f pos = scene.getCamera().projectPlane(ray, plane);

			consumer.accept(GeoPlane.getByNormal(((Camera3D) scene.getCamera()).getRotation()).projectToPlane(pos));
			return null;
		}).push();
	}

	public static void project(GeoPlane plane, Consumer<Vector3f> consumer, Scene scene) {
		INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			int[] viewport = new int[4];
			GLES30.glGetIntegerv(GLES30.GL_VIEWPORT, viewport);
			return viewport;
		}).then((viewport) -> {
			Ray ray = scene.getCamera().projectRay(new Vector2f(INSTANCE.window.getMousePos()), (int[]) viewport);

			Vector3f pos = scene.getCamera().projectPlane(ray, plane);

			consumer.accept(pos);
			return null;
		}).push();
	}

	public static void requestQuit() {
		if (engine != null)
			if (engine.getWindow() != null)
				engine.getWindow().setWindowShouldClose(true);
		workers.closeInput();
		workers.block();
		try {
			workers.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static MeshComponent createUIButton(CacheManager cache, String name, String btnName) {
		final String cacheName = "btn-" + btnName;

		SingleTexture txt = cache.loadOrGetSingleTexture(cacheName, FileUtils.RESOURCES + FileUtils.TEXTURES + "ui/btns/" + btnName, TextureFilter.NEAREST);

		Mesh mesh = Mesh.newQuad(name, cache.loadMaterial(UIButtonShader.UIButtonMaterial.class, txt), txt.getNormalizedSize2D());

		cache.addMesh(mesh);
		return new MeshComponent(mesh);
	}

	public static TextEmitterComponent createUIText(CacheManager cache, String name, int bufferSize, String txt, Alignment align) {
		TextMaterial mat = new TextMaterial("TextMaterial-" + GlobalConsts.TEXT_TEXTURE + "-" + name.hashCode(), cache.getRenderShader(TextShader.NAME), cache.getTexture(GlobalConsts.TEXT_TEXTURE));
		cache.addMaterial(mat);

		TextEmitter text = new TextEmitter(name, mat, bufferSize, txt, new Vector2f(0.35f, 0.5f));
		text.setAlignment(align);
		text.updateText();
		cache.addTextEmitter(text);

		return new TextEmitterComponent(text);
	}

	public static TextEmitterComponent createUIText(CacheManager cache, String name, int bufferSize, String txt, Alignment align, boolean correctTransform) {
		TextEmitterComponent tec = createUIText(currentLoadCache, name, bufferSize, txt, align);
		tec.getTextEmitter(cache).setCorrectTransform(correctTransform);
		return tec;
	}

	public static void updateText(final TextEmitter textEmitter) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			return textEmitter.updateText();
		}).push();
	}

	public static void cleanup(Cleanupable state) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			state.cleanup();
			return null;
		}).push();
	}

	public static void pushRender(Runnable run) {
		GlobalUtils.INSTANCE.createTask(GameEngine.QUEUE_RENDER).exec((t) -> {
			run.run();
			return null;
		}).push();
	}

	public static boolean pushWorker(NextTask nt) {
		return workers.push(nt);
	}

	public static boolean pushWorker(Runnable run) {
		return pushWorker((a) -> {
			run.run();
			return null;
		});
	}

	public static boolean pushWorker(NextTaskFunction run) {
		return new NextTask(0, 0, workers, null).exec(run).push(workers);
	}

	public static boolean pushWorker(NextTask... nt) {
		return workers.push(nt);
	}

	public static boolean pushWorker(Runnable... run) {
		return pushWorker(Arrays.stream(run).map((r) -> {
			return (NextTaskFunction) (a) -> {
				r.run();
				return null;
			};
		}).collect(Collectors.toList()).toArray(new NextTaskFunction[run.length]));
	}

	public static boolean pushWorker(NextTaskFunction... run) {
		return Arrays.stream(run).map((r) -> (boolean) new NextTask(0, 0, workers, null).exec(r).push(workers)).reduce((a, b) -> a && b).orElse(true);
	}

	@Deprecated
	public static Vector2f getDPadDirection() {
		byte[] btns = INSTANCE.window.getJoystickButtonsArray(GLFW.GLFW_JOYSTICK_1);
		// GlobalLogger.severe(Arrays.toString(btns));
		return new Vector2f(btns[GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT] - btns[GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT], btns[GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP] - btns[GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN]);
	}

	public static Vector2f getJoystickDirection() {
		float[] btns = INSTANCE.window.getJoystickAxis(GLFW.GLFW_JOYSTICK_1);
		return new Vector2f(btns[0], -btns[1]);
	}

	public static NextTask newRenderTask() {
		return INSTANCE.createTask(GameEngine.QUEUE_RENDER);
	}

	public static <A, B, C> NextTask<A, B, C> newWorkerToRenderTask() {
		return new NextTask<A, B, C>(GameEngine.QUEUE_RENDER, 0, INSTANCE.getTaskEnvironnment(), workers);
	}

	public static void setFixedRatio(Camera camera) {
		camera.getProjection().update(PROJECTION_WIDTH, PROJECTION_HEIGHT);
	}

	public static void dumpThreads(Level lvl) {
		GlobalLogger.severe("== Thread dump ==");

		for (Entry<Integer, Queue<NextTask>> thread : workers.getQueues().entrySet()) {
			GlobalLogger.log(lvl, "Worker: " + thread.getKey() + " (" + workers.getThreads()[thread.getKey()] + ") > " + thread.getValue().size());
		}

		for (Entry<Integer, Queue<NextTask>> thread : INSTANCE.getTaskEnvironnment().getQueues().entrySet()) {
			GlobalLogger.log(lvl, "Main: " + thread.getKey() + " (" + INSTANCE.getTaskEnvironnment().getThreads()[thread.getKey()] + ") > " + thread.getValue().size());
		}

	}

	public static void time(Runnable run, Consumer<Float> callback) {
		long start = System.nanoTime();
		run.run();
		callback.accept((float) (System.nanoTime() - start) / 1e6f);
	}

	public static Mesh loadCompiledMesh(CacheManager cache, String path) {
		Path filePath = Paths.get("./resources/models/compiled/" + path + ".mesh");
		if (Files.notExists(filePath)) {
			throw new RuntimeException("Mesh at " + filePath + ", not found; compile it with `GlobalUtils.compileMesh(Mesh mesh, String path)`");
		}

		final CacheManager cc = currentLoadCache;
		currentLoadCache = cache;

		Mesh mesh = null;

		try {
			mesh = (Mesh) cm.decode(ByteBuffer.wrap(Files.readAllBytes(filePath)));

			cc.addMesh(mesh);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		currentLoadCache = cc;

		return mesh;
	}

	public static void compileMesh(Mesh mesh, String path) {
		try {
			Path filePath = Paths.get("./resources/models/compiled/" + path + ".mesh");

			ByteBuffer bb = cm.encode(mesh);

			if (Files.notExists(filePath)) {
				Files.createFile(filePath);
			}

			Files.write(filePath, PCUtils.byteBufferToArray(bb));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compileMeshes(CacheManager cache) {
		for (Mesh mesh : cache.getMeshes().values()) {
			if (mesh instanceof QuadMesh)
				continue;
			GlobalUtils.compileMesh(mesh, mesh.getName());
			GlobalLogger.info("Compiled mesh: " + mesh.getId() + " -> " + mesh.getName());
		}
	}

	public static Mesh loadCompiledMesh(CacheManager cache, String path, Supplier<Mesh> supplier) {
		try {
			return loadCompiledMesh(cache, path);
		} catch (RuntimeException e) {
			compileMesh(supplier.get(), path);
		}
		return loadCompiledMesh(cache, path);
	}

	public static void triggerGameEndDeath() {
		if (!GameState.PLAYING.equals(INSTANCE.gameState)) {
			return;
		}

		INSTANCE.playerData.stopMarkCount();
		
		INSTANCE.gameState = GameState.END;
		((UISceneGameOverlay) INSTANCE.uiScene.getState()).startGameEndActive();
	}
	
	public static boolean anyJoystickButton() {
		return Arrays.stream(PCUtils.toIntArray(INSTANCE.window.getJoystickButtonsArray(0))).anyMatch(i -> i > joystickThreshold);
	}

	public static void triggerNewStartMenu() {
		if (!GameState.END.equals(INSTANCE.gameState)) {
			return;
		}

		Optional.ofNullable(INSTANCE.worldScene.getWorld()).ifPresent((w) -> w.setPaused(true));
		
		pushWorker(() -> pushRender(() -> {
			INSTANCE.gameState = GameState.START_MENU;
			INSTANCE.worldScene.cleanup();
			INSTANCE.uiScene.clearOverlay();
			INSTANCE.uiScene.setupStartMenu();
		}));
		
		workers.clearQueues();
	}

	public static void enableWorkers() {
		if(workers != null && !workers.isActive()) {
			workers = null;
		}
		if(workers == null) {
			workers = new NextTaskWorker("workers", 5);
		}
		if(workers.isInputClosed()) {
			workers.openInput();
		}
	}

}
