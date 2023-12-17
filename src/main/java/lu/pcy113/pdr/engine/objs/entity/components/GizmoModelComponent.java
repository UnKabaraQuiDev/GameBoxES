package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class GizmoModelComponent extends Component implements Renderable {

	private String gizmoModelId;

	public GizmoModelComponent(GizmoModel gizmoModel) {
		this.gizmoModelId = gizmoModel.getId();
	}

	public GizmoModelComponent(String gizmoModelId) {
		this.gizmoModelId = gizmoModelId;
	}

	public String getGizmoModelId() {
		return this.gizmoModelId;
	}

	public void setGizmoModelId(String gizmoModelId) {
		this.gizmoModelId = gizmoModelId;
	}

	public GizmoModel getGizmoModel(CacheManager cache) {
		return cache.getGizmoModel(this.gizmoModelId);
	}

	public void setGizmoModel(GizmoModel gizmoModel) {
		this.gizmoModelId = gizmoModel.getId();
	}

}
