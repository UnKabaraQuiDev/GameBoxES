package lu.kbra.gamebox.client.es.game.game.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.GlobalLogger;

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
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellType;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.PlantsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.ToxinsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.WorldParticleEmitter;
import lu.kbra.gamebox.client.es.game.game.utils.NoiseGenerator;
import lu.kbra.gamebox.client.es.game.game.utils.bake.NoiseMain;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class World implements Cleanupable {

	public static final float CULLING_DISTANCE = 35;

	private static final double GEN_FACTOR = 100;

	private static final float Y_OFFSET = 0.001f;
	private static int GEN_CIRCLE_SIDE = 1;

	private static final double SEED_OFFSET_DISTRIBUTION = 11;
	private static final double SEED_OFFSET_HOSTILITY = 10;
	private static final double SEED_OFFSET_HUMIDITY = 69;
	private static final double SEED_OFFSET_FERTILITY = 685;

	private static final float ATTRACT_DISTANCE = 3f;
	private static final float EAT_DISTANCE = 2.5f;

	private HashMap<Vector2f, List<Entity>> generatedChunks = new HashMap<>();
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

	public World(WorldScene3D world, double seed) {
		this.scene = world;

		this.cache = new CacheManager(world.getCache().getId() + "-World", world.getCache());

		this.distributionGen = new NoiseGenerator(seed + SEED_OFFSET_DISTRIBUTION, 32);
		this.hostilityGen = new NoiseGenerator(seed + SEED_OFFSET_HOSTILITY, 32);
		this.fertilityGen = new NoiseGenerator(seed + SEED_OFFSET_FERTILITY, 32);
		this.humidityGen = new NoiseGenerator(seed + SEED_OFFSET_HUMIDITY, 64);

		/*
		 * GlobalUtils.pushWorker(() -> NoiseMain.map(distributionGen, "distribution",
		 * -5 * 20, +5 * 20), () -> NoiseMain.map(hostilityGen, "hostility", -5 * 20, +5
		 * * 20), () -> NoiseMain.map(fertilityGen, "fertility", -5 * 20, +5 * 20), ()
		 * -> NoiseMain.map(humidityGen, "humidity", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.2 &&
		 * hostilityGen.noise(point) < 0.4, "plants_normal", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.2,
		 * "humidity_less_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * humidityGen.noise(point) < 0.4, "humidity_less_0.4", -5 * 20, +5 * 20), () ->
		 * NoiseMain.map((point) -> humidityGen.noise(point) < 0.6, "humidity_less_0.6",
		 * -5 * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) <
		 * 0.8, "humidity_less_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.2,
		 * "humidity_greater_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * humidityGen.noise(point) > 0.4, "humidity_greater_0.4", -5 * 20, +5 * 20), ()
		 * -> NoiseMain.map((point) -> humidityGen.noise(point) > 0.6,
		 * "humidity_greater_0.6", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * humidityGen.noise(point) > 0.8, "humidity_greater_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> fertilityGen.noise(point) < 0.2,
		 * "fertility_less_0.2", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * fertilityGen.noise(point) < 0.4, "fertility_less_0.4", -5 * 20, +5 * 20), ()
		 * -> NoiseMain.map((point) -> fertilityGen.noise(point) < 0.6,
		 * "fertility_less_0.6", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * fertilityGen.noise(point) < 0.8, "fertility_less_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> hostilityGen.noise(point) > 0.5,
		 * "hostility_greater_0.5", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * hostilityGen.noise(point) > 0.8, "hostility_greater_0.8", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> hostilityGen.noise(point) < 0.4,
		 * "hostility_less_0.4", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * hostilityGen.noise(point) < 0.2, "hostility_less_0.2", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 &&
		 * hostilityGen.noise(point) > 0.5, "toxins_normal", -5 * 20, +5 * 20), () ->
		 * NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 &&
		 * java.lang.Math.pow(hostilityGen.noise(point), 2) > 0.5, "toxins_squared", -5
		 * * 20, +5 * 20), () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4
		 * && hostilityGen.noise(point) * 2 > 0.5, "toxins_doubled", -5 * 20, +5 * 20),
		 * () -> NoiseMain.map((point) -> humidityGen.noise(point) < 0.4 &&
		 * hostilityGen.noise(point) / 2 > 0.5, "toxins_halved", -5 * 20, +5 * 20),
		 * 
		 * () -> NoiseMain.map((point) -> fertilityGen.noise(point) > 0.8 &&
		 * hostilityGen.noise(point) < 0.2 && humidityGen.noise(point) > 0.4,
		 * "cells_normal", -5 * 20, +5 * 20), () -> NoiseMain.map((point) ->
		 * fertilityGen.noise(point) > 0.8 && hostilityGen.noise(point) < 0.4 &&
		 * humidityGen.noise(point) > 0.4, "cells_soft_hostility", -5 * 20, +5 * 20), ()
		 * -> NoiseMain.map((point) -> fertilityGen.noise(point) > 0.6 &&
		 * hostilityGen.noise(point) < 0.4 && humidityGen.noise(point) > 0.4,
		 * "cells_soft_hostility_soft_fertility", -5 * 20, +5 * 20));
		 */

		CellMaterial playerMaterial = cache.loadOrGetMaterial("playerMaterial", CellShader.CellMaterial.class, CellType.PLAYER.name(),
				cache.loadOrGetSingleTexture(CellShader.CellMaterial.PLAYER_TEXTURE_NAME, CellShader.CellMaterial.PLAYER_TEXTURE_PATH));
		Mesh playerMesh = cache.newQuadMesh("playerMesh", playerMaterial, new Vector2f(2.5f * 2));
		this.player = new CellEntity("player", cache, playerMesh, new CellDescriptor("player", CellType.PLAYER, "noname", null, null, null, 1), new Vector3f(0, 0, 1.5f));
		player.addComponent(new RenderComponent(4));
		scene.addEntity(player);

		cellDescriptorPool = loadCellDescriptorPool();
	}

	public void input(float dTime) {
		Window window = scene.getWindow();

		player.getAcceleration().add(GlobalUtils.getDPadDirection().mul(dTime * GlobalUtils.INSTANCE.playerData.getSpeed()));
	}

	public void update(float dTime) {
		Window window = scene.getWindow();
		
		player.update();

		for (Vector2f chunkCenter : getNeighbouringChunks(getCenterPlayerPos())) {
			List<Entity> entities = generatedChunks.get(chunkCenter);
			if (entities == null)
				continue;

			for (Entity e : entities) {
				if (e instanceof PlantsEntity) {
					simulatePlants(dTime, (PlantsEntity) e);
				}
			}
		}

	}

	private void simulatePlants(final float dTime, PlantsEntity e) {
		WorldParticleEmitter inst = (WorldParticleEmitter) e.getComponent(InstanceEmitterComponent.class).getInstanceEmitter(cache);

		final Vector3f parentAbsPos = e.getComponent(Transform3DComponent.class).getTransform().getTranslation().mul(1, 1, 0, new Vector3f());
		final Vector3f playerAbsPos = player.getTransform().getTransform().getTranslation().mul(1, 1, 0, new Vector3f());

		GlobalUtils.<Void, Boolean, Void>newWorkerToRenderTask()
			.exec((a) -> {
				boolean changed = false;
				for (Instance part : inst.getParticles()) {
					Vector3f objAbsPos = ((Transform3D) part.getTransform()).getTranslation().add(parentAbsPos, new Vector3f()).mul(1, 1, 0);
					float dist = playerAbsPos.distance(objAbsPos);
	
					if (dist < ATTRACT_DISTANCE) {
						Vector3f direction = new Vector3f(playerAbsPos).sub(objAbsPos).normalize();
						((Transform3D) part.getTransform()).getTranslation().add(direction.mul(dTime * dist * 2));
						part.getTransform().updateMatrix();
	
						changed = true;
					}
	
					if (dist < EAT_DISTANCE) {
						part.getBuffers()[0] = 0f;
						
						GlobalUtils.INSTANCE.playerData.eatPlant();
						
						changed = true;
					}
				}
				return changed;
			}).then((Boolean changed) -> {
				if (changed) {
					inst.updateParticles();
				}
				return null;
			}).push();

		/*
		 * inst.update((part) -> { Vector3f objPos = ((Transform3D)
		 * part.getTransform()).getTranslation(); float dist =
		 * playerPos.distance(objPos);
		 * 
		 * if (dist < 10) { Vector3f direction = new
		 * Vector3f(playerPos).sub(objPos).normalize(); objPos.add(direction.mul(0.1f *
		 * dist)); part.getTransform().updateMatrix(); } });
		 */
	}

	public Vector2f getCenterPlayerPos() {
		return getChunkCenter(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation()));
	}

	private Vector2f[] getNeighbouringChunks(Vector2f center) {
		Vector2f[] list = new Vector2f[9];

		int i = 0;
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				list[i++] = new Vector2f(x * chunkSize, y * chunkSize).add(center);
			}
		}

		return list;
	}

	public void continueWorldGen() {
		Vector2f center = GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation());
		center = getChunkCenter(center);
		scene.axis.getComponent(Transform3DComponent.class).getTransform().setTranslation(GeoPlane.XY.project(center)).updateMatrix();

		for (int x = -GEN_CIRCLE_SIDE; x <= GEN_CIRCLE_SIDE; x++) {
			for (int y = -GEN_CIRCLE_SIDE; y <= GEN_CIRCLE_SIDE; y++) {

				genChunk(new Vector2f(x, y).mul(chunkSize).add(center));

			}
		}
	}

	private void genChunk(Vector2f center) {
		center.set(getChunkCenter(center));

		if (generatedChunks.containsKey(center)) {
			GlobalLogger.info("Chunk already generated: " + center);
			return;
		}

		GlobalUtils.pushRender(new Runnable() {
			@Override
			public void run() {
				GlobalLogger.info("Generating chunk: " + center);

				if (generatedChunks.containsKey(center)) {
					GlobalLogger.info("Chunk already generated: " + center);
					return;
				}

				generatedChunks.put(center, new ArrayList<Entity>(1));

				List<Entity> gens = genChunk(center, chunkSize);

				generatedChunks.put(center, gens);
			}
		});
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
				pool.add(new CellDescriptor(k, sobj.getEnum(CellType.class, "type"), sobj.getString("scientific"), PDRUtils.loadRangeFloat(sobj, "hostility"), PDRUtils.loadRangeFloat(sobj, "fertility"), PDRUtils.loadRangeFloat(sobj, "humidity"),
						sobj.getInt("textureVariationCount")));
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return pool;
	}

	public List<Entity> genChunk(Vector2f center, int chunkSize) {
		return genWorld(center, chunkSize / 2, 5 * 5 * 3 * 10);
	}

	public List<Entity> genWorld(Vector2f center, float halfSquareSize, int numPoint) {
		List<Entity> entities = new ArrayList<Entity>(numPoint);

		List<Entity> plants = genPlants(center, halfSquareSize, numPoint / 3);
		plants.forEach(scene::addEntity);

		List<Entity> toxins = genToxins(center, halfSquareSize, numPoint / 3);
		toxins.forEach(scene::addEntity);

		List<Entity> cells = genCells(center, halfSquareSize, numPoint / 10);
		cells.forEach(scene::addEntity);

		entities.addAll(plants);
		entities.addAll(toxins);
		entities.addAll(cells);

		return entities;
	}

	private List<Entity> genCells(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> cells = distributePoints(center, halfSquareSize, numPoint);
		cells = genCells(center, cells);

		HashMap<CellDescriptor, ArrayList<Vector2f>> cellDesc = new HashMap<>(numPoint);

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

			CellInstanceEmitter emit = new CellInstanceEmitter(desc.getId() + "-part-" + center, poss.size(), desc.loadOrGetMaterial(cache), new Transform3D());
			cache.addMesh(emit.getParticleMesh());
			cache.addInstanceEmitter(emit);

			Matrix4f[] matrices = poss.stream().map(t -> new Matrix4f().setTranslation(t.x, t.y, 0).scale(Math.clamp(0.6f, 1.2f, (float) ((1 - hostilityGen.noise(t)) + fertilityGen.noise(t) * 0.3f + humidityGen.noise(t)))))
					.collect(Collectors.toList()).toArray(new Matrix4f[poss.size()]);
			Object[] states = poss.stream().map(p -> (int) random.nextInt(desc.getTextureVariationCount())).collect(Collectors.toList()).toArray();

			for (int i = 0; i < matrices.length; i++) {
				matrices[i].translate(0, 0, Y_OFFSET * i);
			}

			emit.updateDirect(matrices, new Object[][] { states });

			entities.add(new CellsEntity(desc.getId() + "-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0.4f)), new RenderComponent(4), new RenderComponent(10),
					new RenderConditionComponent(() -> new Vector3f(center.x, center.y, 0).distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE)));
		}

		return entities;
	}

	private List<Entity> genToxins(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> toxins = distributePoints(center, halfSquareSize, numPoint);
		toxins = genToxins(center, toxins);

		WorldParticleEmitter emit = new WorldParticleEmitter(
				"toxins-" + center, toxins.size(), (ToxinWorldParticleMaterial) cache.loadOrGetMaterial(ToxinWorldParticleMaterial.NAME, ToxinWorldParticleMaterial.class,
						cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(ToxinWorldParticleMaterial.TEXTURE_NAME, ToxinWorldParticleMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		Matrix4f[] matrices = toxins.parallelStream().map(t -> new Matrix4f().setTranslation(t.x, t.y, 0)).collect(Collectors.toList()).toArray(new Matrix4f[toxins.size()]);
		Object[] sizes = toxins.stream().map(p -> Math.clamp(5f, 10f, (float) hostilityGen.noise(p))).collect(Collectors.toList()).toArray();

		for (int i = 0; i < matrices.length; i++) {
			matrices[i].translate(0, 0, Y_OFFSET * i);
		}

		emit.updateDirect(matrices, new Object[][] { sizes });

		return Arrays.asList(new ToxinsEntity("toxins-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0)), new RenderComponent(8),
				new RenderConditionComponent(() -> new Vector3f(center.x, center.y, 0).distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE)));
	}

	private List<Entity> genPlants(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> plants = distributePoints(center, halfSquareSize, numPoint);
		plants = genPlants(center, plants);

		WorldParticleEmitter emit = new WorldParticleEmitter(
				"plants-" + center, plants.size(), (PlantWorldParticleMaterial) cache.loadOrGetMaterial(PlantWorldParticleMaterial.NAME, PlantWorldParticleMaterial.class,
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

		// Matrix4f[] matrices = plants.parallelStream().map(t -> new
		// Matrix4f().setTranslation(t.x, t.y,
		// 0)).collect(Collectors.toList()).toArray(new Matrix4f[plants.size()]);
		// Object[] sizes = plants.stream().map(p -> Math.clamp(0.6f, 2f, (float)
		// humidityGen.noise(p) * 5)).collect(Collectors.toList()).toArray();

		emit.updateParticles();

		return Arrays.asList(new PlantsEntity("plants-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0.2f)), new RenderComponent(6),
				new RenderConditionComponent(() -> new Vector3f(center.x, center.y, 0).distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE)));
	}

	private CellDescriptor getRandomCellDescriptor(Vector2f pos) {
		float hostility = (float) hostilityGen.noise(pos);
		float fertility = (float) fertilityGen.noise(pos);
		float humidity = (float) humidityGen.noise(pos);

		return cellDescriptorPool.parallelStream().filter((d) -> d.match(hostility, fertility, humidity)).findAny().orElseGet(() -> cellDescriptorPool.get(random.nextInt(cellDescriptorPool.size())));
	}

	public List<Vector2f> genPlants(Vector2f center, List<Vector2f> points) {
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (humidityGen.noise(point, center) > 0.2 && hostilityGen.noise(point, center) < 0.4) {
				rePoints.add(point);
			}
		}
		GlobalLogger.info("Plant growth ratio: " + ((double) rePoints.size() / points.size() * 100) + "%");
		return rePoints;
	}

	public List<Vector2f> genToxins(Vector2f center, List<Vector2f> points) {
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (humidityGen.noise(point, center) < 0.4 && hostilityGen.noise(point, center) > 0.5 && random.nextFloat() > 0.6f) {
				rePoints.add(point);
			}
		}
		GlobalLogger.info("Toxin growth ratio: " + ((double) rePoints.size() / points.size() * 100) + "%");
		return rePoints;
	}

	public List<Vector2f> genCells(Vector2f center, List<Vector2f> points) {
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
