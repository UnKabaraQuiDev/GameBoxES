package lu.kbra.gamebox.client.es.game.game.scenes;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.geom.utils.ObjLoader;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.GizmoComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene3D;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;

public class WorldScene3D extends Scene3D {

	private CacheManager cache;

	private float distance;

	public WorldScene3D(String name, CacheManager parentCache) {
		super(name);
		this.cache = new CacheManager(parentCache);
	}
	
	CellEntity ce;
	
	public void setupScene() {
		Gizmo axis = ObjLoader.loadGizmo("grid_xyz", "./resources/models/gizmos/grid_xyz.obj");
		cache.addGizmo(axis);
		super.addEntity("grid_xyz", new GizmoComponent(axis), new Transform3DComponent(new Transform3D(new Vector3f(0), new Quaternionf(), new Vector3f(10))));

		ce = addCellEntity("player", CellType.PLAYER);

		camera.getProjection().setPerspective(true);
		camera.getProjection().setFov((float) Math.toRadians(70));
		camera.getProjection().update();

		placeCamera(new Vector2f(0, 0), 5);

		cache.dump(System.out);
	}

	public void placeCamera(Vector2f pos) {
		((Camera3D) camera).lookAt(new Vector3f(distance, pos.x, pos.y), new Vector3f(0, pos.x, pos.y));
		camera.updateMatrix();
	}

	public void placeCamera(Vector2f pos, float distance) {
		this.distance = distance;
		placeCamera(pos);
	}

	private CellEntity addCellEntity(String name, CellType type) {
		CellEntity ce = CellEntity.load(cache, name, type);

		return (CellEntity) addEntity(name, ce);
	}

	public CacheManager getCache() {
		return cache;
	}

}
