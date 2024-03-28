package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Gizmo;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class GizmoComponent extends Component implements Renderable {

	private String gizmoId;

	public GizmoComponent(Gizmo gizmo) {
		this.gizmoId = gizmo.getId();
	}

	public GizmoComponent(String gizmoId) {
		this.gizmoId = gizmoId;
	}

	public String getGizmoId() {
		return this.gizmoId;
	}

	public void setGizmoId(String gizmoId) {
		this.gizmoId = gizmoId;
	}

	public Gizmo getGizmo(CacheManager cache) {
		return cache.getGizmo(this.gizmoId);
	}

	public void setGizmo(Gizmo gizmo) {
		this.gizmoId = gizmo.getId();
	}

}
