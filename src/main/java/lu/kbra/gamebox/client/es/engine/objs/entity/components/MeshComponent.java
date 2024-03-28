package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class MeshComponent extends Component implements Renderable {

	private String meshId;

	public MeshComponent(Mesh mesh) {
		this.meshId = mesh.getId();
	}

	public MeshComponent(String meshId) {
		this.meshId = meshId;
	}

	public String getMeshId() {
		return this.meshId;
	}

	public void setMeshId(String meshId) {
		this.meshId = meshId;
	}

	public Mesh getMesh(CacheManager cache) {
		return cache.getMesh(this.meshId);
	}

	public void setMesh(Mesh mesh) {
		this.meshId = mesh.getId();
	}

}
