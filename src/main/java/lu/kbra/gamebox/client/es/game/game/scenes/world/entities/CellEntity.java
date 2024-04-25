package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;

public class CellEntity extends Entity {

	private CellDescriptor cellDescriptor;
	private Transform3DComponent transform;
	private MeshComponent mesh;

	public CellEntity(CacheManager cache, Mesh mesh, CellDescriptor descriptor) {
		this.cellDescriptor = descriptor;
		this.transform = new Transform3DComponent();
		this.mesh = new MeshComponent(mesh);

		addComponent(transform);
		addComponent(this.mesh);
	}

	public static CellEntity load(CacheManager cache, String name, CellDescriptor descriptor) {
		Material material = descriptor.createMaterial(cache);
		Mesh mesh = Mesh.newQuad(descriptor.getCellType().getDataPath() + material.hashCode(), material, new Vector2f(1));
		mesh.createDrawBuffer();
		cache.addMesh(mesh);
		CellEntity ce = new CellEntity(cache, mesh, descriptor);
		return ce;
	}

	public CellDescriptor getCellDescriptor() {
		return cellDescriptor;
	}

	public Transform3DComponent getTransform() {
		return transform;
	}

	public MeshComponent getMesh() {
		return mesh;
	}

}
