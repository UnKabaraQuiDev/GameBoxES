package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class GizmoComponent extends Component implements Renderable {
	
	private String gizmoId;
	
	public GizmoComponent(Gizmo gizmo) {
		this.gizmoId = gizmo.getId();
	}
	public GizmoComponent(String gizmoId) {
		this.gizmoId = gizmoId;
	}
	
	public String getGizmoId() {return gizmoId;}
	public void setGizmoId(String gizmoId) {this.gizmoId = gizmoId;}
	
	public Gizmo getGizmo(CacheManager cache) {return cache.getGizmo(gizmoId);}
	public void setGizmo(Gizmo gizmo) {this.gizmoId = gizmo.getId();}
	
}
