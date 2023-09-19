package lu.pcy113.pdr.engine.objs;

import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class GizmoModel implements UniqueID, Renderable {
	
	public static final String NAME = GizmoModel.class.getName();
	
	private final String name;
	private String gizmo;
	private Transform transform;
	
	protected float lineWidth = 2.5f;
	
	public GizmoModel(String name, String gizmo, Transform transform) {
		this.name = name;
		this.gizmo = gizmo;
		this.transform = transform;
	}
	
	public String getGizmo() {return gizmo;}
	public void setGizmo(String gizmo) {this.gizmo = gizmo;}
	public Transform getTransform() {return transform;}
	public void setTransform(Transform transform) {this.transform = transform;}
	public float getLineWidth() {return lineWidth;}
	public void setLineWidth(float lineWidth) {this.lineWidth = lineWidth;}
	@Override
	public String getId() {return name;}
	
}