package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class GizmoModel
		implements
		UniqueID,
		Renderable {

	public static final String NAME = GizmoModel.class.getName();

	private final String name;
	private String gizmo;

	protected float lineWidth = Gizmo.LINE_WIDTH;

	public GizmoModel(String name, String gizmo) {
		this.name = name;
		this.gizmo = gizmo;
	}

	public String getGizmo() { return gizmo; }

	public void setGizmo(String gizmo) { this.gizmo = gizmo; }

	public float getLineWidth() { return lineWidth; }

	public void setLineWidth(float lineWidth) { this.lineWidth = lineWidth; }

	@Override
	public String getId() { return name; }

}