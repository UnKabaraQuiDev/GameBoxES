package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Velocity2DComponent;
import lu.kbra.gamebox.client.es.game.game.data.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.scenes.world.entities.components.Acceleration2DComponent;
import lu.kbra.gamebox.client.es.game.game.world.World;

public class CellEntity extends Entity {

	private CellDescriptor cellDescriptor;
	private Transform3DComponent transform;
	private MeshComponent mesh;
	private Velocity2DComponent velocity;
	private Acceleration2DComponent acceleration;

	public CellEntity(String name, CacheManager cache, Mesh mesh, CellDescriptor descriptor, Vector3f position) {
		super(name);

		this.cellDescriptor = descriptor;

		this.transform = new Transform3DComponent(position);
		this.mesh = new MeshComponent(mesh);
		this.velocity = new Velocity2DComponent();
		this.acceleration = new Acceleration2DComponent();

		addComponent(transform);
		addComponent(this.mesh);
		addComponent(acceleration);
		addComponent(velocity);
	}

	public void update(/* Vector2f... forces */) {
		// https://en.wikipedia.org/wiki/Rigid_body

		// Arrays.stream(forces).forEach(acceleration::add);

		// apply drag
		acceleration.add(velocity.getVelocity().mul(-World.DRAG_FORCE, new Vector2f()));

		acceleration.update();
		velocity.update();

		transform.getTransform().updateMatrix();

		acceleration.zero();
	}

	@Deprecated
	public static CellEntity load(CacheManager cache, CellDescriptor descriptor) {
		Material material = descriptor.createMaterial(cache);
		Mesh mesh = Mesh.newQuad(descriptor.getCellType().getDataPath() + material.hashCode(), material, new Vector2f(1));
		cache.addMesh(mesh);
		CellEntity ce = new CellEntity(descriptor.getId() + mesh.hashCode(), cache, mesh, descriptor, new Vector3f(0));
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

	public Velocity2DComponent getVelocity() {
		return velocity;
	}

	public Acceleration2DComponent getAcceleration() {
		return acceleration;
	}

	@Override
	public String toString() {
		return super.toString() + "{" + cellDescriptor.getCellType() + " & " + cellDescriptor.getId() + "}";
	}

}
