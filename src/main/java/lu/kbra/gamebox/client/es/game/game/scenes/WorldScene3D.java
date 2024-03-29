package lu.kbra.gamebox.client.es.game.game.scenes;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;

public class WorldScene3D extends Scene3D {

	private CacheManager cache;

	public WorldScene3D(String name, CacheManager parentCache) {
		super(name);
		this.cache = new CacheManager(parentCache);
	}

	public void setupScene() {
		CellEntity ce = addCellEntity("player", CellType.PLAYER);
	}

	private CellEntity addCellEntity(String name, CellType type) {
		CellEntity ce = CellEntity.load(cache, name, type);

		return (CellEntity) addEntity(name, ce);
	}

}
