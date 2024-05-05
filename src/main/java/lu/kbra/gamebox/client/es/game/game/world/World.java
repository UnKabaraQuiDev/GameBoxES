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

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.InstanceEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlantWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.ToxinWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellType;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.WorldParticleEmitter;
import lu.kbra.gamebox.client.es.game.game.utils.NoiseGenerator;

public class World implements Cleanupable {

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

	private Random random = new Random();

	private List<CellDescriptor> cellDescriptorPool;

	private CacheManager cache;

	private CellEntity player;

	public World(CacheManager cache) {
		this.cache = new CacheManager(cache.getId()+"-World", cache);

		this.hostilityGen = new NoiseGenerator();
		this.fertilityGen = new NoiseGenerator();
		this.humidityGen = new NoiseGenerator();

		this.player = CellEntity.load(cache, new CellDescriptor("player", CellType.PLAYER, "noname", null, null, null, 1));

		cellDescriptorPool = loadCellDescriptorPool();
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

	public List<Entity> genWorld(Vector2f center, float halfSquareSize, int numPoint) {
		List<Entity> entities = new ArrayList<Entity>(numPoint);

		entities.addAll(genPlants(center, halfSquareSize, numPoint));
		entities.addAll(genToxins(center, halfSquareSize, numPoint));
		entities.addAll(genCells(center, halfSquareSize, numPoint));

		return entities;
	}

	private List<Entity> genCells(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> cells = distributePoints(center, halfSquareSize, numPoint / 3);
		cells = genCells(cells);

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

			CellInstanceEmitter emit = new CellInstanceEmitter(desc.getId() + "-part", poss.size(), desc.loadOrGetMaterial(cache), new Transform3D());
			cache.addMesh(emit.getParticleMesh());
			cache.addInstanceEmitter(emit);
			
			try {
				Files.write(Paths.get("./resources/bakes/noise/sizes.txt"), poss.parallelStream().map(t -> Math.clamp(0.3f, 1.2f, (float) ((1 - hostilityGen.noise(t)*0.2f) + fertilityGen.noise(t) * 0.3f + humidityGen.noise(t)))+"").collect(Collectors.joining("\n")).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			Matrix4f[] matrices = poss.parallelStream().map(t -> new Matrix4f().setTranslation(t.x, t.y, 0).scale(Math.clamp(0.3f, 1.2f, (float) ((1 - hostilityGen.noise(t)) + fertilityGen.noise(t) * 0.3f + humidityGen.noise(t)))))
					.collect(Collectors.toList()).toArray(new Matrix4f[poss.size()]);
			Object[] states = poss.stream().map(p -> (int) random.nextInt(desc.getTextureVariationCount())).collect(Collectors.toList()).toArray();

			emit.updateDirect(matrices, new Object[][] { states });

			entities.add(new Entity(new InstanceEmitterComponent(emit), new Transform3DComponent()));
		}

		return entities;
	}

	private List<Entity> genToxins(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> toxins = distributePoints(center, halfSquareSize, numPoint / 3);
		toxins = genToxins(toxins);

		WorldParticleEmitter emit = new WorldParticleEmitter("toxins", toxins.size(), (ToxinWorldParticleMaterial) cache.loadOrGetMaterial(ToxinWorldParticleMaterial.NAME, ToxinWorldParticleMaterial.class,
				cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(ToxinWorldParticleMaterial.TEXTURE_NAME, ToxinWorldParticleMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		Matrix4f[] matrices = toxins.parallelStream().map(t -> new Matrix4f().setTranslation(t.x, t.y, 0)).collect(Collectors.toList()).toArray(new Matrix4f[toxins.size()]);
		Object[] sizes = toxins.stream().map(p -> Math.clamp(0.2f, 1f, (float) hostilityGen.noise(p))).collect(Collectors.toList()).toArray();

		emit.updateDirect(matrices, new Object[][] { sizes });

		return Arrays.asList(new Entity(new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0))));
	}

	private List<Entity> genPlants(Vector2f center, float halfSquareSize, int numPoint) {
		List<Vector2f> plants = distributePoints(center, halfSquareSize, numPoint / 3);
		plants = genToxins(plants);

		WorldParticleEmitter emit = new WorldParticleEmitter("plants", plants.size(), (PlantWorldParticleMaterial) cache.loadOrGetMaterial(PlantWorldParticleMaterial.NAME, PlantWorldParticleMaterial.class,
				cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(PlantWorldParticleMaterial.TEXTURE_NAME, PlantWorldParticleMaterial.TEXTURE_PATH, TextureFilter.NEAREST)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		Matrix4f[] matrices = plants.parallelStream().map(t -> new Matrix4f().setTranslation(t.x, t.y, random.nextFloat() * 0.1f)).collect(Collectors.toList()).toArray(new Matrix4f[plants.size()]);
		Object[] sizes = plants.stream().map(p -> Math.clamp(0.2f, 1f, (float) humidityGen.noise(p))).collect(Collectors.toList()).toArray();

		emit.updateDirect(matrices, new Object[][] { sizes });

		return Arrays.asList(new Entity(new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0))));
	}

	private CellDescriptor getRandomCellDescriptor(Vector2f pos) {
		float hostility = (float) hostilityGen.noise(pos);
		float fertility = (float) fertilityGen.noise(pos);
		float humidity = (float) humidityGen.noise(pos);

		return cellDescriptorPool.parallelStream().filter((d) -> d.match(hostility, fertility, humidity)).findAny().orElseGet(() -> cellDescriptorPool.get(random.nextInt(cellDescriptorPool.size())));
	}

	public List<Vector2f> genPlants(List<Vector2f> points) {
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (humidityGen.noise(point) > 0.5 && hostilityGen.noise(point) < 0.5) {
				rePoints.add(point);
			}
		}
		return points;
	}

	public List<Vector2f> genToxins(List<Vector2f> points) {
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (humidityGen.noise(point) > 0.4 && random.nextFloat() > 0.6f) {
				rePoints.add(point);
			}
		}
		return points;
	}

	public List<Vector2f> genCells(List<Vector2f> points) {
		List<Vector2f> rePoints = new ArrayList<Vector2f>();
		for (Vector2f point : points) {
			if (fertilityGen.noise(point) > 0.4 && hostilityGen.noise(point) > 0.2 && humidityGen.noise(point) > 0.3 && random.nextFloat() > 0.6f) {
				rePoints.add(point);
			}
		}
		return points;
	}

	public List<Vector2f> distributePoints(Vector2f center, float halfSquareSize, int numPoints) {
		List<Vector2f> points = new ArrayList<>();

		// Calculate the square boundaries
		float minX = center.x - halfSquareSize;
		float minY = center.y - halfSquareSize;
		float maxX = center.x + halfSquareSize;
		float maxY = center.y + halfSquareSize;

		for (int i = 0; i < numPoints; i++) {
			float randomX = minX + random.nextFloat() * (maxX - minX);
			float randomY = minY + random.nextFloat() * (maxY - minY);
			points.add(new Vector2f(randomX, randomY));
		}

		return points;
	}

	@Override
	public void cleanup() {
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

}
