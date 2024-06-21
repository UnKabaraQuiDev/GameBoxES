package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.game.game.data.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.render.shaders.PlayerCellShader.PlayerCellMaterial;

public class PlayerEntity extends CellEntity {

	public PlayerEntity(String name, CacheManager cache, Mesh mesh, CellDescriptor descriptor, Vector3f position) {
		super(name, cache, mesh, descriptor, position);
	}
	
	public PlayerCellMaterial getPlayerMaterial(CacheManager cache) {
		return (PlayerCellMaterial) getMesh().getMesh(cache).getMaterial();
	}

}
