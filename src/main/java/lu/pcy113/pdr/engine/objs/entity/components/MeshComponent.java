package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class MeshComponent
		extends
		Component
		implements
		Renderable {

	private String meshId;

	public MeshComponent(Mesh mesh) {
		this.meshId = mesh.getId();
	}

	public MeshComponent(String meshId) {
		this.meshId = meshId;
	}

	public String getMeshId() { return this.meshId; }

	public void setMeshId(String meshId) { this.meshId = meshId; }

	public Mesh getMesh(CacheManager cache) {
		return cache.getMesh(this.meshId);
	}

	public void setMesh(Mesh mesh) { this.meshId = mesh.getId(); }

}
