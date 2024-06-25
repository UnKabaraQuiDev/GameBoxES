package lu.kbra.gamebox.client.es.game.game.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;
import lu.pcy113.pclib.pointer.prim.BooleanPointer;
import lu.pcy113.pclib.pointer.prim.IntPointer;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.Instance;
import lu.kbra.gamebox.client.es.engine.graph.window.Window;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
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
import lu.kbra.gamebox.client.es.game.game.data.Achievements;
import lu.kbra.gamebox.client.es.game.game.data.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.data.CellType;
import lu.kbra.gamebox.client.es.game.game.data.PlayerData;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlantWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlayerCellShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlayerCellShader.PlayerCellMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.ToxinWorldParticleMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader;
import lu.kbra.gamebox.client.es.game.game.scenes.world.WorldScene3D;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellInstanceEmitter;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.CellsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.PlantsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.PlayerEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.ToxinsEntity;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.WorldParticleEmitter;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalConsts;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalOptions;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;
import lu.kbra.gamebox.client.es.game.game.utils.noise.NoiseGenerator;

public class World implements Cleanupable {

	public static final float CULLING_DISTANCE = 35;

	private static final float Y_OFFSET = 0.01f;
	public static final int GEN_CIRCLE_SIZE = 1;
	public static final float DRAG_FORCE = 0.06f;

	private static final float ATTRACT_DISTANCE = 2.5f;
	private static final float PLANT_EAT_DISTANCE = 2f;
	private static final float CELL_EAT_DISTANCE = 2f;
	private static final float CELLS_MOV_SPEED = 1f;
	private static final float CELL_POISON_DAMAGE_DISTANCE = 5f;

	private static final double SEED_OFFSET_DISTRIBUTION = 11;
	private static final double SEED_OFFSET_HOSTILITY = 10;
	private static final double SEED_OFFSET_HUMIDITY = 69;
	private static final double SEED_OFFSET_FERTILITY = 685;

	private static final Vector2f ZERO2D = new Vector2f(0);

	private static final long TOXIN_DAMAGE_DELAY = 1000, CELL_DAMAGE_DELAY = 1000; // ms

	private HashMap<String, List<Entity>> generatedChunks = new HashMap<>();
	private Set<String> updateTasks = Collections.synchronizedSet(new HashSet<>(20));
	private final int chunkSize = 20;

	private boolean paused = false;

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

	private Random random = new Random();

	private List<CellDescriptor> cellDescriptorPool;

	private WorldScene3D scene;
	private CacheManager cache;

	private PlayerEntity player;
	private Entity poisonTrail;
	private int poisonTrailIndex = 0;

	public World(WorldScene3D world, double seed) {
		this.scene = world;

		this.cache = new CacheManager(world.getCache().getId() + "-World", world.getCache());

		this.distributionGen = new NoiseGenerator(seed + SEED_OFFSET_DISTRIBUTION, 32);
		this.hostilityGen = new NoiseGenerator(seed + SEED_OFFSET_HOSTILITY, 32);
		this.fertilityGen = new NoiseGenerator(seed + SEED_OFFSET_FERTILITY, 32);
		this.humidityGen = new NoiseGenerator(seed + SEED_OFFSET_HUMIDITY, 64);

		PlayerCellMaterial playerMaterial = cache.loadOrGetMaterial("playerMaterial", PlayerCellShader.PlayerCellMaterial.class, CellType.PLAYER.name(),
				cache.loadOrGetSingleTexture(PlayerCellShader.PlayerCellMaterial.PLAYER_TEXTURE_NAME, PlayerCellShader.PlayerCellMaterial.PLAYER_TEXTURE_PATH, TextureFilter.NEAREST),
				cache.loadOrGetSingleTexture(PlayerCellShader.PlayerCellMaterial.PLAYER_OVERLAY_TEXTURE_NAME, PlayerCellShader.PlayerCellMaterial.PLAYER_OVERLAY_TEXTURE_PATH, TextureFilter.NEAREST));
		Mesh playerMesh = cache.newQuadMesh("playerMesh", playerMaterial, new Vector2f(2.5f * 2));
		this.player = new PlayerEntity("player", cache, playerMesh, new CellDescriptor("player", CellType.PLAYER, "noname", null, null, null, 1, 0, 0, 0, null, null), new Vector3f(0, 0, GlobalConsts.PLAYER_CELL_HEIGHT));
		player.addComponent(new RenderComponent(GlobalConsts.PLAYER_CELL_HEIGHT));
		scene.addEntity(player);

		cellDescriptorPool = loadCellDescriptorPool();
		GlobalLogger.info(cellDescriptorPool.toString());
	}

	public void input(float dTime) {
		Window window = scene.getWindow();

		if (paused) {
			return;
		}

		player.getAcceleration().add(GlobalUtils.getJoystickDirection().normalize().mul(dTime * Math.sqrt(GlobalUtils.INSTANCE.playerData.getSpeed())));
	}

	private long lastToxinDamage = 0, lastCellDamage = 0;
	private boolean moved = false;
	private byte frameCount = 125;

	public void update(float dTime) {
		Window window = scene.getWindow();

		if (paused) {
			return;
		}

		player.update();
		moved = player.getVelocity().getVelocity().lengthSquared() > 0;

		GlobalLogger.info(
				"Player absolute world position: " + player.getTransform().getTransform().getTranslation() + ", current chunk center: " + getChunkCenter(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation())));

		PlayerData pd = GlobalUtils.INSTANCE.playerData;

		if (Math.random() < pd.getPhotosynthesis() / 5) {
			pd.incGlucose();
		}

		if (pd.hasPoisonTrail() && poisonTrail != null) {
			if (moved && java.lang.Math.random() < 0.11f) {
				WorldParticleEmitter inst = (WorldParticleEmitter) poisonTrail.getComponent(InstanceEmitterComponent.class).getInstanceEmitter(cache);

				Instance part = inst.getParticles()[poisonTrailIndex++];

				final Vector3f playerTrans = player.getTransform().getTransform().getTranslation();
				((Transform3D) part.getTransform()).getScale().set(3f);
				((Transform3D) part.getTransform()).getRotation().set(new Quaternionf().rotateLocalZ((float) (Math.random() * Math.PI)));
				((Transform3D) part.getTransform()).setTranslation(new Vector3f(playerTrans.x, playerTrans.y, GlobalConsts.TOXINS_HEIGHT + Y_OFFSET * poisonTrailIndex));
				((Transform3D) part.getTransform()).updateMatrix();
				part.getBuffers()[0] = Math.clamp(1.2f, 2.5f, (float) (Math.random() * 2.5f));

				GlobalUtils.pushRender(inst::updateParticles);

				poisonTrailIndex %= inst.getParticleCount();
			}
		}

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

		if (frameCount == 0) {
			cleanupExtChunks();
		}
		frameCount++;

		if (!pd.isToxinResistant() && isToxic(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation()), ZERO2D) && System.currentTimeMillis() - lastToxinDamage > TOXIN_DAMAGE_DELAY) {
			lastToxinDamage = System.currentTimeMillis();

			GlobalUtils.INSTANCE.playerData.damage(1);

			GlobalUtils.INSTANCE.playerData.unlockAchievement(Achievements.FOG_DAMAGE);
		}

		/**
		 * GlobalUtils.dumpThreads(Level.SEVERE); if (updateTasks.size() > 0) { GlobalLogger.info(Arrays.toString(updateTasks.toArray())); }
		 */
	}

	private void cleanupExtChunks() {
		List<Cleanupable> toBeRemoved = new ArrayList<Cleanupable>(50);
		GlobalUtils.pushRender(() -> {
			int entityCount = scene.getEntities().size();
			scene.getEntities().entrySet().removeIf((e) -> {
				if (!e.getValue().hasComponent(Transform3DComponent.class)) {
					return false;
				}
				if (e.getValue().getComponent(Transform3DComponent.class).getTransform().getTranslation().distance(player.getTransform().getTransform().getTranslation()) > chunkSize * 3) {
					for (Component c : e.getValue().getComponents().values()) {
						if (c instanceof Cleanupable) {
							toBeRemoved.add((Cleanupable) c);
							((Cleanupable) c).cleanup();
						}
					}
					return true;
				}
				return false;
			});

			GlobalLogger.info("Cleaned up " + toBeRemoved.size() + " components");
			GlobalLogger.info("Removed " + (entityCount - scene.getEntities().size()) + " entities");
		});
	}

	public void render(float dTime) {
		PlayerData pd = GlobalUtils.INSTANCE.playerData;

		if (pd.hasPoisonTrail() && poisonTrail == null) {
			WorldParticleEmitter emit = new WorldParticleEmitter("playerToxins", 24, (ToxinWorldParticleMaterial) cache.loadOrGetMaterial(ToxinWorldParticleMaterial.NAME, ToxinWorldParticleMaterial.class,
					cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(ToxinWorldParticleMaterial.TEXTURE_NAME, ToxinWorldParticleMaterial.TEXTURE_PATH, TextureFilter.LINEAR)),
					new Transform3D(new Vector3f(), new Quaternionf(), new Vector3f(0)));
			cache.addMesh(emit.getParticleMesh());
			cache.addInstanceEmitter(emit);

			poisonTrail = scene.addEntity("poisonTrail", new InstanceEmitterComponent(emit));

			GlobalLogger.info("Init PoisonTrail !");
		}

		if (paused) {
			return;
		}
	}

	private void simulateCells(float dTime, Vector2f center, CellsEntity e) {
		final String taskId = center.x + "-" + center.y + "-cells-" + e.getId();

		if (updateTasks.contains(taskId)) {
			GlobalLogger.info("Already computing (cells): " + center);
			return;
		}

		updateTasks.add(taskId);

		final PlayerData pd = GlobalUtils.INSTANCE.playerData;

		final CellInstanceEmitter inst = (CellInstanceEmitter) e.getComponent(InstanceEmitterComponent.class).getInstanceEmitter(cache);

		final Vector3f parentAbsPos = e.getComponent(Transform3DComponent.class).getTransform().getTranslation().mul(1, 1, 0, new Vector3f());
		final Vector3f playerAbsPos = player.getTransform().getTransform().getTranslation().mul(1, 1, 0, new Vector3f());

		final CellDescriptor desc = inst.getDescription();

		for (int i = 0; i < inst.getParticleCount(); i++) {
			Instance part = inst.getParticles()[i];

			final Vector3f partPos = ((Transform3D) part.getTransform()).getTranslation();

			if (((Transform3D) part.getTransform()).getScale().lengthSquared() == 0) {
				pd.unlockAchievement(Achievements.KILL_AN_ENNEMY);

				GlobalUtils.showPlayerNote(desc);
				continue;
			}

			final Vector3f objAbsPos = partPos.add(parentAbsPos, new Vector3f()).mul(1, 1, 0);
			final float dist = playerAbsPos.distance(objAbsPos);
			final Vector3f direction = new Vector3f(playerAbsPos).sub(objAbsPos).normalize();

			if (pd.isPredatorRepulsion() && dist <= desc.getHardAggressiveDistance()) {
				partPos.add(direction.mul(-1 * CELLS_MOV_SPEED));
				inst.getDirections().get(i).set(direction.mul(5));

				part.getTransform().updateMatrix();
				continue;
			}
			if ((Math.random() < desc.getAggressivity() && dist < desc.getSoftAggressiveDistance()) || dist < desc.getHardAggressiveDistance()) { // triggers aggressive behaviour
				inst.getDirections().get(i).lerp(direction.mul(dTime * CELLS_MOV_SPEED), 0.5f);
				partPos.add(inst.getDirections().get(i).mul(1, 1, 0));

				part.getTransform().updateMatrix();
			}
			if (pd.isPoisonDamage() && dist <= CELL_POISON_DAMAGE_DISTANCE) {
				((Transform3D) part.getTransform()).getScale().sub(new Vector3f(0.5f));
				PDRUtils.clampPositive(((Transform3D) part.getTransform()).getScale());
				continue;
			}
			if (dist <= CELL_EAT_DISTANCE) {
				if (pd.getDamage() > 0 && Math.random() < pd.getDamage() / 5) {
					// reflect damage

					((Transform3D) part.getTransform()).getScale().sub(new Vector3f(0.5f));
					PDRUtils.clampPositive(((Transform3D) part.getTransform()).getScale());

					pd.eatCell();
					
					if (((Transform3D) part.getTransform()).getScale().lengthSquared() == 0) {
						pd.unlockAchievement(Achievements.KILL_AN_ENNEMY);

						pd.incKillCount();
					}
				} else if (System.currentTimeMillis() - lastCellDamage > CELL_DAMAGE_DELAY) {
					GlobalUtils.INSTANCE.playerData.damage(1);
					if (pd.getHealth() <= 0) {
						// player died
						GlobalUtils.showPlayerNote(desc);
					}

					GlobalUtils.INSTANCE.playerData.unlockAchievement(Achievements.ENNEMY_DAMAGE);

					lastCellDamage = System.currentTimeMillis();
				}

				inst.getDirections().get(i).set(direction.mul(-25 * CELLS_MOV_SPEED));
				// partPos.add(inst.getDirections().get(i).mul(1, 1, 0));

				part.getTransform().updateMatrix();
			}
		}

		GlobalUtils.pushRender(() -> {
			inst.updateParticlesTransforms();
			updateTasks.remove(taskId);
		});

	}

	private void simulatePlants(final float dTime, final Vector2f center, PlantsEntity e) {
		final String taskId = center.x + "-" + center.y + "-plants-";

		if (updateTasks.contains(taskId)) {
			GlobalLogger.info("Already computing (plants): " + center);
			return;
		}

		updateTasks.add(taskId);

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

			if (dist < PLANT_EAT_DISTANCE && !MathUtils.compare((float) part.getBuffers()[0], 0, 0.001f)) {
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
				updateTasks.remove(taskId);
			});
		} else {
			updateTasks.remove(taskId);
		}
	}

	public Vector2f getCenterPlayerPos() {
		return getChunkCenter(GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation()));
	}

	private Vector2f[] getNeighbouringChunks(Vector2f center) {
		Vector2f[] list = new Vector2f[(int) java.lang.Math.pow((1 + 2 * GEN_CIRCLE_SIZE), 2)];

		int i = 0;
		for (int x = -GEN_CIRCLE_SIZE; x <= GEN_CIRCLE_SIZE; x++) {
			for (int y = -GEN_CIRCLE_SIZE; y <= GEN_CIRCLE_SIZE; y++) {
				list[i++] = new Vector2f(x * chunkSize, y * chunkSize).add(center);
			}
		}

		return list;
	}

	public void continueWorldGen(int radius) {
		Vector2f center = GeoPlane.XY.projectToPlane(player.getTransform().getTransform().getTranslation());
		center = getChunkCenter(center);
		if (GlobalOptions.DEBUG) {
			scene.axis.getComponent(Transform3DComponent.class).getTransform().setTranslation(GeoPlane.XY.project(center)).updateMatrix();
		}
		
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
		LinkedList<CellDescriptor> pool = new LinkedList<CellDescriptor>();

		try {
			JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/cells/cells.json"))));

			for (String k : obj.keySet()) {
				JSONObject sobj = obj.getJSONObject(k);
				pool.add(new CellDescriptor(k, sobj.getEnum(CellType.class, "type"), sobj.getString("scientific"), PDRUtils.loadRangeFloat(sobj, "hostility"), PDRUtils.loadRangeFloat(sobj, "fertility"),
						PDRUtils.loadRangeFloat(sobj, "humidity"), sobj.getInt("textureVariationCount"), sobj.getFloat("aggressivity"), sobj.getFloat("hardAggressiveDistance"), sobj.getFloat("softAggressiveDistance"),
						sobj.optString("deathDesc." + GlobalLang.getCURRENT_LANG(), "deathDesc." + GlobalLang.getCURRENT_LANG()).replace("<br>", "\n"),
						sobj.optString("killDesc." + GlobalLang.getCURRENT_LANG(), "killDesc." + GlobalLang.getCURRENT_LANG()).replace("<br>", "\n")));

				GlobalLogger.info("Added cell type: " + pool.getLast());
			}
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return pool.stream().collect(PCUtils.toShuffledList());
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

			GlobalLogger.info("Task gen chunk (render: push): " + (float) (System.nanoTime() - start2) / 1e6f + "ms for " + center);
		};

		return GlobalUtils.pushWorker(() -> {
			long start = System.currentTimeMillis();

			final List<Vector2f> plantPos = genPlantsPos(center, halfSquareSize, numPoint / 3 / 3);
			final List<Vector2f> toxinsPos = genToxinsPos(center, halfSquareSize, numPoint / 3);
			final List<Vector2f> cellsPos = genCellsPos(center, halfSquareSize, numPoint / 9);

			GlobalLogger.info("Task gen chunk (worker): " + (System.currentTimeMillis() - start) + "ms for " + center);

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> plants = genPlants_render(center, plantPos);
				list.addAll(plants);

				GlobalLogger.info("Task gen chunk (render: plants): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

				ifLast(finished.increment(), 2, pushList);
			});

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> toxins = genToxins_render(center, toxinsPos);
				list.addAll(toxins);

				GlobalLogger.info("Task gen chunk (render: toxins): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

				ifLast(finished.increment(), 2, pushList);
			});

			GlobalUtils.pushRender(() -> {
				long start1 = System.nanoTime();

				final List<Entity> cells = genCells_render(center, cellsPos);
				list.addAll(cells);

				GlobalLogger.info("Task gen chunk (render: cells): " + (float) (System.nanoTime() - start1) / 1e6f + "ms for " + center);

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

		float ennemyCellHeight = GlobalConsts.ENNEMY_CELLS_HEIGHT;

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

				((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, ennemyCellHeight + Y_OFFSET * i);
				((Transform3D) part.getTransform()).getScale().set(Math.lerp(3f, 4.5f, Math.random()));
				((Transform3D) part.getTransform()).getRotation().set(new Quaternionf().rotateLocalZ((float) (Math.random() * Math.PI)));
				((Transform3D) part.getTransform()).updateMatrix();
				part.getBuffers()[0] = (int) random.nextInt(desc.getTextureVariationCount());
			}

			emit.updateParticles();

			CellsEntity pe = new CellsEntity(desc.getId() + "-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0)), new RenderComponent(ennemyCellHeight));
			pe.addComponent(new RenderConditionComponent(() -> pe.getTransform().getTransform().getTranslation().distance(((Camera3D) scene.getCamera()).getPosition()) < CULLING_DISTANCE));

			entities.add(pe);

			ennemyCellHeight += Y_OFFSET * poss.size();
		}

		return entities;
	}

	// private native void ntv_genCells_render(List<Vector2f> poss, CellInstanceEmitter emit);

	private List<Entity> genToxins_render(Vector2f ce, List<Vector2f> toxins) {
		final Vector2f center = new Vector2f(ce);

		WorldParticleEmitter emit = new WorldParticleEmitter(
				"toxins-" + center, toxins.size(), (ToxinWorldParticleMaterial) cache.loadOrGetMaterial(ToxinWorldParticleMaterial.NAME, ToxinWorldParticleMaterial.class,
						cache.loadOrGetRenderShader(WorldParticleShader.NAME, WorldParticleShader.class), cache.loadOrGetSingleTexture(ToxinWorldParticleMaterial.TEXTURE_NAME, ToxinWorldParticleMaterial.TEXTURE_PATH, TextureFilter.LINEAR)),
				new Transform3D());
		cache.addMesh(emit.getParticleMesh());
		cache.addInstanceEmitter(emit);

		// ntv_genToxins_render(toxins, emit);

		for (int i = 0; i < toxins.size(); i++) {
			Instance part = emit.getParticles()[i];
			Vector2f pos = toxins.get(i);

			((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, GlobalConsts.TOXINS_HEIGHT + Y_OFFSET * i);
			((Transform3D) part.getTransform()).updateMatrix();

			part.getBuffers()[0] = Math.clamp(0.6f, 10f, hostilityGen.noise(pos) * 10f);
		}

		emit.updateParticles();

		ToxinsEntity pe = new ToxinsEntity("toxins-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0)), new RenderComponent(GlobalConsts.TOXINS_HEIGHT));
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

			((Transform3D) part.getTransform()).getTranslation().set(pos.x, pos.y, GlobalConsts.PLANTS_HEIGHT + Y_OFFSET * i);
			((Transform3D) part.getTransform()).updateMatrix();
			part.getBuffers()[0] = Math.clamp(0.6f, 2f, (float) humidityGen.noise(pos) * 5);
		}

		emit.updateParticles();

		PlantsEntity pe = new PlantsEntity("plants-" + center, new InstanceEmitterComponent(emit), new Transform3DComponent(new Vector3f(center.x, center.y, 0)), new RenderComponent(GlobalConsts.PLANTS_HEIGHT));
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

	public PlayerEntity getPlayer() {
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

	public void setPlayer(PlayerEntity player) {
		this.player = player;
	}

	public void setPaused(boolean b) {
		this.paused = b;
	}

}
