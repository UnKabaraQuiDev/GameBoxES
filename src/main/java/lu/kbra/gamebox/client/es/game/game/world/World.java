package lu.kbra.gamebox.client.es.game.game.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Random;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pclib.pointer.prim.BooleanPointer;
import lu.pcy113.pclib.pointer.prim.IntPointer;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.Instance;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.RenderConditionComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader.CellMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlantWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.ToxinWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader;
import lu.kbra.gamebox.client.es.game.game.scenes.world.WorldScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellInstanceEmitter;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellType;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.PlantsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.ToxinsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.WorldParticleEmitter;
import lu.kbra.gamebox.client.es.game.game.utils.NoiseGenerator;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class World implements Cleanupable {

	public static final float CULLING_DISTANCE = 35;

	private static final float Y_OFFSET = 0.001f;
	public static final int GEN_CIRCLE_SIDE = 1;

	private static final float ATTRACT_DISTANCE = 3f;
	private static final float EAT_DISTANCE = 2.5f;
	private static final float CELLS_MOV_SPEED = 1f;

	private static final double SEED_OFFSET_DISTRIBUTION = 11;
	private static final double SEED_OFFSET_HOSTILITY = 10;
	private static final double SEED_OFFSET_HUMIDITY = 69;
	private static final double SEED_OFFSET_FERTILITY = 685;

	private static final Vector2f ZERO2D = new Vector2f(0);

	private static final long TOXIN_DAMAGE_DELAY = 800; // ms

	private HashMap<String, List<Entity>> generatedChunks = new HashMap<>();
	private List<String> updateTasks = new ArrayList<>(20);
	private final int chunkSize = 20;

	/**
	 * Generates the points' position
	 */
	private NoiseGenerator distributionGen;
	/**
	 * Generates more Toxins
	 */
	private NoiseGenerator hostilityGen;
	/**
	 * More cells spawn
	 */
	private NoiseGenerator fertilityGen;
	/**
	 * More plants spawn
	 */
	private NoiseGenerator humidityGen;

	private float dragForce = 0.06f;

	private Random random = new Random();

	private List<CellDescriptor> cellDescriptorPool;

	private WorldScene3D scene;
	private CacheManager cache;

	private CellEntity player;

	private int updateFrameCount;

	public World(WorldScene3D world, double seed) {
		this.scene = world;

		this.cache = new CacheManager(world.getCache().getId() + "-World", world.getCache());

		this.distributionGen = new NoiseGenerator(seed + SEED_OFFSET_DISTRIBUTION, 32);
		this.hostilityGen = new NoiseGenerator(seed + SEED_OFFSET_HOSTILITY, 32);
		this.fertilityGen = new NoiseGenerator(seed + SEED_OFFSET_FERTILITY, 32);
		this.humidityGen = new NoiseGenerator(seed + SEED_OFFSET_HUMIDITY, 64);

		/*
		 * GlobalUtils.pushWorker(() -> NoiseMain.map(distributionGen, "distribution", -5 * 20, +5 * 20), () -> NoiseMain.map(hostilityGen, "hostility", -5 * 20, +5 * 20), () -> NoiseMain.map(fertilityGen, "fertility", -5 * 20, +5 * 20), ()
		 * -> NoiseMain.map(humidityGen, "humidity", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.2 && hostilityGen.noise(point) < 0.4, "plants_normal", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.2, "humidity_less_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4, "humidity_less_0.4", -5 * 20, +5 * 20), () ->
		 * NoiseMain.map((point) -> humidityGen.noise(point) < 0.6, "humidity_less_0.6", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.8, "humidity_less_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.2, "humidity_greater_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.4, "humidity_greater_0.4", -5 * 20, +5 * 20), () ->
		 * NoiseMain.map((point) -> humidityGen.noise(point) > 0.6, "humidity_greater_0.6", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.8, "humidity_greater_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> fertilityGen.noise(point) < 0.2, "fertility_less_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> fertilityGen.noise(point) < 0.4, "fertility_less_0.4", -5 * 20, +5 * 20), () ->
		 * NoiseMain.map((point) -> fertilityGen.noise(point) < 0.6, "fertility_less_0.6", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> fertilityGen.noise(point) < 0.8, "fertility_less_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> hostilityGen.noise(point) > 0.5, "hostility_greater_0.5", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> hostilityGen.noise(point) > 0.8, "hostility_greater_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> hostilityGen.noise(point) < 0.4, "hostility_less_0.4", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> hostilityGen.noise(point) < 0.2, "hostility_less_0.2", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 && hostilityGen.noise(point) > 0.5, "toxins_normal", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 &&
		 * java.lang.Math.pow(hostilityGen.noise(point), 2) > 0.5, "toxins_squared", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 && hostilityGen.noise(point) * 2 > 0.5, "toxins_doubled", -5 * 20, +5 *
		 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 && hostilityGen.noise(point) / 2 > 0.5, "toxins_halved", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> fertilityGen.noise(point) > 0.8 && hostilityGen.noise(point) < 0.2 && humidityGen.noise(point) > 0.4, "cells_normal", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> fertilityGen.noise(point) >
		 * 0.8 && hostilityGen.noise(point) < 0.4 && humidityGen.noise(point) > 0.4, "cells_soft_hostility", -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> fertilityGen.noise(point) > 0.6 && hostilityGen.noise(point) < 0.4 &&
		 * humidityGen.noise(point) > 0.4, "cells_soft_hostility_soft_fertility", -5 * 20, +5 * 20));
		 */

		CellMaterial playerMaterial = cache.loadOrGetMaterial("playerMaterial", CellShader.CellMaterial.class, CellType.PLAYER.name(),
				cache.loadOrGetSingleTexture(CellShader.CellMaterial.PLAYER_TEXTURE_NAME, CellShader.CellMaterial.PLAYER_TEXTURE_PATH));
		Mesh playerMesh = cache.newQuadMesh("playerMesh", playerMaterial, new Vector2f(2.5f * 2));
		this.player = new CellEntity("player", cache, playerMesh, new CellDescriptor("player", CellType.PLAYER, "noname", null, null, null, 1, 0, 0, 0), new Vector3f(0, 0, 1.5f));
		player.addComponent(new RenderComponent(4));
		scene.addEntity(player);

		cellDescriptorPool = loadCellDescriptorPool();
	}

	public void input(float dTime) {
		Window window = scene.getWindow();

		player.getAcceleration().add(GlobalUtils.getDPadDirection().mul(dTime * GlobalUtils.INSTANCE.playerData.getSpeed()));
	}

	private long lastToxinDamage = 0;

	public void update(float dTime) {
		Window window = scene.getWindow();

		player.update();

		updateFrameCount++;

		System.out.println(updateFrameCount);

		for (Vector2f chunkCenter : getNeighbouringChunks(getCenterPlayerPos())) {
			List<Entity> entities = generatedChunks.get(chunkCenter.toString());
			if (entities == null)
				continue;

			synchronized (entities) {
				for (Entity e : entities) {

					if (e instanceof PlantsEntity) {
						simulatePlants(dTime, chunkCenter, (PlantsEntity) e);
					} else if (e instanceof CellsEntity) {
						simulateCells(dTime, chunkCenter, (CellsEntity) e);
					}

				}
			}
		}

		if (isToxic(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation()), ZERO2D) && System.currentTimeMillis() - lastToxinDamage > TOXIN_DAMAGE_DELAY) {
			lastToxinDamage = System.currentTimeMillis();

			GlobalUtils.INSTANCE.playerData.damage(1);
		}

		GlobalUtils.dumpThreads(Level.SEVERE);
	}

	public void render(float dTime) {

	}

	private void simulateCells(float dTime, Vector2f center, CellsEntity e) {
		if (updateTasks.contains(center.toString() + "-cells-" + e.getId())) {
			GlobalLogger.info("Already computing: " + center);
			return;
		}

		updateTasks.add(center.toString() + "-cells-" + e.getId());

		final CellInstanceEmitter inst = (CellInstanceEmitter) e.getComponent(InstanceEmitterComponent.class).getInstanceEmitter(cache);

		final Vector3f parentAbsPos = e.getComponent(Transform3DComponent.class).getTransform().getTranslation().mul(1, 1, 0, new Vector3f());
		final Vector3f playerAbsPos = player.getTransform().getTransform().getTranslation().mul(1, 1, 0, new Vector3f());

		final CellDescriptor desc = inst.getDescription();

		for (int i = 0; i < inst.getParticleCount(); i++) {
			Instance part = inst.getParticles()[i];

			Vector3f objAbsPos = ((Transform3D) part.getTransform()).getTranslation().add(parentAbsPos, new Vector3f()).mul(1, 1, 0);
			float dist = playerAbsPos.distance(objAbsPos);
			Vector3f direction = new Vector3f(playerAbsPos).sub(objAbsPos).normalize();

			if ((Math.random() < desc.getAggressivity() && dist < desc.getSoftAggressiveDistance()) || dist < desc.getHardAggressiveDistance()) { // triggers aggressive behaviour
				inst.getDirections().get(i).set(direction.mul(dTime * CELLS_MOV_SPEED));
				((Transform3D) part.getTransform()).getTranslation().add(inst.getDirections().get(i));
			} else if (dist <= EAT_DISTANCE) {
				GlobalUtils.INSTANCE.playerData.damage(1);

				inst.getDirections().get(i).set(direction.mul(-2 * dTime * CELLS_MOV_SPEED));
				((Transform3D) part.getTransform()).getTranslation().add(inst.getDirections().get(i));
			} else { // idle
				inst.getDirections().get(i).add(new Vector3f((float) Math.random() - 0.5f, (float) Math.random() - 0.5f, 0).normalize()).div(2);
				((Transform3D) part.getTransform()).getTranslation().add(inst.getDirections().get(i));
			}
			part.getTransform().updateMatrix();
		}

		if (updateFrameCount % 10 == 1) {

			System.err.println("push");
			GlobalUtils.pushRender(() -> {
				inst.updateParticlesTransforms();
				updateTasks.remove(center.toString() + "-cells-" + e.getId());
			});

		} else {

			/*for (int i = 0; i < inst.getParticleCount(); i++) {
				Instance part = inst.getParticles()[i];

				((Transform3D) part.getTransform()).getTranslation().add(inst.getDirections().get(i));
				part.getTransform().updateMatrix();
			}*/

		}
	}

	private void simulatePlants(final float dTime, final Vector2f center, PlantsEntity e) {
		if (updateTasks.contains(center.toString() + "plants")) {
			GlobalLogger.info("Already computing: " + center);
			return;
		}

		updateTasks.add(center.toString() + "plants");

		WorldParticleEmitter inst = (WorldParticleEmitter) e.getComponent(InstanceEmitterComponent.class).getInstanceEmitter(cache);

		final Vector3f parentAbsPos = e.getComponent(Transform3DComponent.class).getTransform().getTranslation().mul(1, 1, 0, new Vector3f());
		final Vector3f playerAbsPos = player.getTransform().getTransform().getTranslation().mul(1, 1, 0, new Vector3f());

		final BooleanPointer changed = new BooleanPointer(false);

		for (Instance part : inst.getParticles()) {
			Vector3f objAbsPos = ((Transform3D) part.getTransform()).getTranslation().add(parentAbsPos, new Vector3f()).mul(1, 1, 0);
			float dist = playerAbsPos.distance(objAbsPos);

			if (dist < ATTRACT_DISTANCE) {
				Vector3f direction = new Vector3f(playerAbsPos).sub(objAbsPos).normalize();
				((Transform3D) part.getTransform()).getTranslation().add(direction.mul(dTime * dist * 2));

				changed.setValue(true);
			}

			if (dist < EAT_DISTANCE && !MathUtils.compare((float) part.getBuffers()[0], 0, 0.001f)) {
				((Transform3D) part.getTransform()).setScale(0);
				part.getBuffers()[0] = 0f;

				GlobalUtils.INSTANCE.playerData.eatPlant();

				changed.setValue(true);
			}

			if (changed.getValue()) {
				part.getTransform().updateMatrix();
			}
		}

		// GlobalLogger.severe("Has changed: "+changed.getValue());

		if (changed.getValue()) {
			GlobalUtils.pushRender(() -> {
				inst.updateParticlesTransforms();
				updateTasks.remove(center.toString() + "plants");
			});
		} else {
			updateTasks.remove(center.toString() + "plants");
		}
	}

	public Vector2f getCenterPlayerPos() {
		return getChunkCenter(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation()));
	}

	private Vector2f[] getNeighbouringChunks(Vector2f center) {
		Vector2f[] list = new Vector2f[(int) java.lang.Math.pow((1 + 2 * GEN_CIRCLE_SIDE), 2)];

		int i = 0;
		for (int x = -GEN_CIRCLE_SIDE; x <= GEN_CIRCLE_SIDE; x++) {
			for (int y = -GEN_CIRCLE_SIDE; y <= GEN_CIRCLE_SIDE; y++) {
				list[i++] = new Vector2f(x * chunkSize, y * chunkSize).add(center);
			}
		}

		return list;
	}

	public void continueWorldGen(int radius) {
		Vector2f center = GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation());
		center = getChunkCenter(center);
		scene.axis.getComponent(Transform3DComponent.class).getTransform().setTranslation(GeoPlane.XY.project(center)).updateMatrix();

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {

				genChunk(new Vector2f(x, y).mul(chunkSize).add(center));

			}
		}
	}

	private void genChunk(Vector2f center) {
		center.set(getChunkCenter(center));

		if (generatedChunks.containsKey(center.toString())) {
			// GlobalLogger.info("Chunk already generated: " + center);
			return;
		}

		GlobalLogger.info("Generating chunk: " + center);

		generatedChunks.put(center.toString(), new ArrayList<Entity>(1));

		genChunk(center, chunkSize);
	}

	private Vector2f getChunkCenter(Vector2f center) {
		return new Vector2f(MathUtils.snap(center.x, chunkSize), MathUtils.snap(center.y, chunkSize));
	}

	private static List<CellDescriptor> loadCellDescriptorPool() {
		List<CellDescriptor> pool = new ArrayList<CellDescriptor>();

		try {
			JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/cells/cells.json"))));

			for (String k : obj.keySet()) {
				JSONObject sobj = obj.getJSONObject(k);
				pool.add(new CellDescriptor(k, sobj.getEnum(CellType.class, "type"), sobj.getString("scientific"), PDRUtils.loadRangeFloat(sobj, "hostility"), PDRUtils.loadRangeFloat(sobj, "fertility"),
						PDRUtils.loadRangeFloat(sobj, "humidity"), sobj.getInt("textureVariationCount"), sobj.getFloat("aggressivity"), sobj.getFloat("hardAggressiveDistance"), sobj.getFloat("softAggressiveDistance")));
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return pool;
	}

	public boolean genChunk(Vector2f center, int chunkSize) {
		return genWorld(center, chunkSize / 2, 5 * 5 * 3 * 2);
	}

	public boolean genWorld(Vector2f center, float halfSquareSize, int numPoint) {
		final List<Entity> list = Collections.synchronizedList(new ArrayList<Entity>(3));

		final IntPointer finished = new IntPointer();

		final Runnable pushList = () -> {
			long start2 = System.nanoTime();

			list.forEach(scene::addEntity);
			generatedChunks.put(center.toString(), list);

			System.err.println("Task gen chunk (render: push): " + (float) (System.nanoTime() - start2) / 1e6f + "ms for " + center);
		};

		return GlobalUtils.pushWorker(() -> {
			long start = System.currentTimeMillis();

			final List<Vector2f> plantPos = genPlantsPos(center, halfSquareSize, numPoint / 3);
			final List<Vector2f> toxinsPos = genToxinsPos(center, halfSquareSize, numPoint / 3);
			final List<Vector2f> cellsPos = genCellsPos(center, halfSquareSize, numPoint / 3);

			System.err.println("Task gen chunk (worker): " + (System.currentTimeMillis() - start) + "ms for " + center);

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> plants = genPlants_render(center, plantPos);
				list.addAll(plants);

				System.err.println("Task gen chunk (render: plants): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

				ifLast(finished.increment(), 2, pushList);
			});

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> toxins = genToxins_render(center, toxinsPos);
				list.addAll(toxins);

				System.err.println("Task gen chunk (render: toxins): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

				ifLast(finished.increment(), 2, pushList);
			});

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> cells = genCells_render(center, cellsPos);
				list.addAll(cells);

				System.err.println("Task gen chunk (render: cells): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

				ifLast(finished.increment(), 2, pushList);
			});

		});
	}

	private void ifLast(int count, int min, Runnable run) {
		if (count >= min) {
			run.run();
		}
	}

	private List<Entity> genCells_render(Vector2f ce, List<Vector2f> cells) {
		final Vector2f center = new Vector2f(ce);

		HashMap<CellDescriptor, ArrayList<Vector2f>> cellDesc = new HashMap<>(cells.size());

		for (Vector2f pos : cells) {
			CellDescriptor desc = getRandomCellDescriptor(pos);
			if (!cellDesc.containsKey(desc)) {
				cellDesc.put(desc, new ArrayList<Vector2f>());
			}
			cellDesc.get(desc).add(pos);
		}

		List<Entity> entities = new ArrayList<>();

		for (Entry<CellDescriptor, ArrayList<Vector2f>> e : cellDesc.entrySet()) {
			CellDescriptor desc = e.getKey();
			ArrayList<Vector2f> poss = e.getValue();

			CellInstanceEmitter emit = new CellInstanceEmitter(desc.getId() + "-part-" + center, poss.size(), desc.loadOrGetMaterial(cache), new Transform3D(), desc);
			cache.addMesh(emit.getParticleMesh());
			cache.addInstanceEmitter(emit);

			// ntv_genCells_render(poss, emit);

			for (int i = 0; i < poss.size(); i++) {
				Instance part = emit.getParticles()[i];
				Vector2f pos = poss.get(i);

				((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, Y_OFFSET * i);
				((Transform3D) part.getTransform()).getScale().set(Math.clamp(0.6f, 4f, (float) ((1 - hostilityGen.noise(pos)) + fertilityGen.noise(pos) * 0.3f + humidityGen.noise(pos)) * 2));
				((Transform3D) part.getTransform()).updateMatrix();
				part.getBuffers()[0] = (int) random.nextInt(desc.getTextureVariationCount());
			}

			emit.updateParticles();

			CellsEntity pe = new CellsEntity(desc.getId() + "-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0.4f)), new RenderComponent(6));
			pe.addComponent(new RenderConditionComponent(() -> pe.getTransform().getTransform().getTranslation().distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE));

			entities.add(pe);
		}

		return entities;
	}

	// private native void ntv_genCells_render(List<Vector2f> poss, CellInstanceEmitter emit);

	private List<Entity> genToxins_render(Vector2f ce, List<Vector2f> toxins) {
		final Vector2f center = new Vector2f(ce);

		WorldParticleEmitter emit = new WorldParticleEmitter("toxins-" + center, toxins.size(), (ToxinWorldParticleMaterial) cache.loadOrGetMaterial(ToxinWorldParticleMaterial.NAME, ToxinWorldParticleMaterial.class,
				cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(ToxinWorldParticleMaterial.TEXTURE_NAME, ToxinWorldParticleMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		// ntv_genToxins_render(toxins, emit);

		for (int i = 0; i < toxins.size(); i++) {
			Instance part = emit.getParticles()[i];
			Vector2f pos = toxins.get(i);

			((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, Y_OFFSET * i);
			((Transform3D) part.getTransform()).updateMatrix();
			part.getBuffers()[0] = Math.clamp(0.6f, 2f, Math.clamp(5f, 10f, (float) hostilityGen.noise(pos)));
		}

		emit.updateParticles();

		ToxinsEntity pe = new ToxinsEntity("toxins-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0)), new RenderComponent(8));
		pe.addComponent(new RenderConditionComponent(() -> pe.getTransform().getTransform().getTranslation().distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE));
		return Arrays.asList(pe);
	}

	// private native void ntv_genToxins_render(List<Vector2f> poss, WorldParticleEmitter emit);

	private List<Entity> genPlants_render(Vector2f ce, List<Vector2f> plants) {
		final Vector2f center = new Vector2f(ce);

		WorldParticleEmitter emit = new WorldParticleEmitter("plants-" + center, plants.size(), (PlantWorldParticleMaterial) cache.loadOrGetMaterial(PlantWorldParticleMaterial.NAME, PlantWorldParticleMaterial.class,
				cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(PlantWorldParticleMaterial.TEXTURE_NAME, PlantWorldParticleMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		for (int i = 0; i < plants.size(); i++) {
			Instance part = emit.getParticles()[i];
			Vector2f pos = plants.get(i);

			((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, Y_OFFSET * i);
			((Transform3D) part.getTransform()).updateMatrix();
			part.getBuffers()[0] = Math.clamp(0.6f, 2f, (float) humidityGen.noise(pos) * 5);
		}

		emit.updateParticles();

		PlantsEntity pe = new PlantsEntity("plants-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0.2f)), new RenderComponent(10));
		pe.addComponent(new RenderConditionComponent(() -> pe.getTransform().getTransform().getTranslation().distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE));
		return Arrays.asList(pe);
	}

	// private native void ntv_genPlants_render(List<Vector2f> poss, WorldParticleEmitter emit);

	private CellDescriptor getRandomCellDescriptor(Vector2f pos) {
		float hostility = (float) hostilityGen.noise(pos);
		float fertility = (float) fertilityGen.noise(pos);
		float humidity = (float) humidityGen.noise(pos);

		return cellDescriptorPool.parallelStream().filter((d) -> d.match(hostility, fertility, humidity)).findAny().orElseGet(() -> cellDescriptorPool.get(random.nextInt(cellDescriptorPool.size())));
	}

	public List<Vector2f> genPlantsPos(Vector2f center, float halfSquareSize, int numPoints) {
		List<Vector2f> points = distributePoints(center, halfSquareSize, numPoints);
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (humidityGen.noise(point, center) > 0.2 && hostilityGen.noise(point, center) < 0.4) {
				rePoints.add(point);
			}
		}
		GlobalLogger.info("Plant growth ratio: " + ((double) rePoints.size() / points.size() * 100) + "%");
		return rePoints;
	}

	public List<Vector2f> genToxinsPos(Vector2f center, float halfSquareSize, int numPoints) {
		List<Vector2f> points = distributePoints(center, halfSquareSize, numPoints);
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (isToxic(point, center)) {
				rePoints.add(point);
			}
		}
		GlobalLogger.info("Toxin growth ratio: " + ((double) rePoints.size() / points.size() * 100) + "%");
		return rePoints;
	}

	private boolean isToxic(Vector2f point, Vector2f center) {
		return humidityGen.noise(point, center) < 0.4 && hostilityGen.noise(point, center) > 0.5 && random.nextFloat() > 0.6f;
	}

	public List<Vector2f> genCellsPos(Vector2f center, float halfSquareSize, int numPoints) {
		List<Vector2f> points = distributePoints(center, halfSquareSize, numPoints);
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (fertilityGen.noise(point, center) > 0.6 && hostilityGen.noise(point, center) < 0.4 && humidityGen.noise(point, center) > 0.4 && random.nextFloat() > 0.2f) {
				rePoints.add(point);
			}
		}
		GlobalLogger.info("Cell growth ratio: " + ((double) rePoints.size() / points.size() * 100) + "%");
		return rePoints;
	}

	public List<Vector2f> distributePoints(Vector2f seed, float halfSquareSize, int numPoints) {
		List<Vector2f> points = new ArrayList<>();

		// Calculate the square boundaries
		float minX = -halfSquareSize;
		float minY = -halfSquareSize;
		float maxX = +halfSquareSize;
		float maxY = +halfSquareSize;

		for (int i = 0; i < numPoints; i++) {
			// float randomX = minX + (float) distributionGen.noise(i * GEN_FACTOR + seed.x,
			// i * GEN_FACTOR + seed.y) * (maxX - minX);
			// float randomY = minY + (float) distributionGen.noise(i * GEN_FACTOR * 2 +
			// seed.x, i * GEN_FACTOR / 3 + seed.y) * (maxY - minY);

			float randomX = minX + random.nextFloat() * (maxX - minX);
			float randomY = minY + random.nextFloat() * (maxY - minY);

			points.add(new Vector2f(randomX, randomY));
		}

		return points;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + getClass().getName());

		cache.cleanup();
	}

	public CacheManager getCache() {
		return cache;
	}

	public CellEntity getPlayer() {
		return player;
	}

	public NoiseGenerator getHostilityGen() {
		return hostilityGen;
	}

	public NoiseGenerator getFertilityGen() {
		return fertilityGen;
	}

	public NoiseGenerator getHumidityGen() {
		return humidityGen;
	}

	public float getDragForce() {
		return dragForce;
	}

	public void setDragForce(float dragForce) {
		this.dragForce = dragForce;
	}

	public void setPlayer(CellEntity player) {
		this.player = player;
	}

}
