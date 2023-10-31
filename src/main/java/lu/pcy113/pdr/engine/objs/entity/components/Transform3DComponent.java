package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class Transform3DComponent extends TransformComponent {
	
	private Transform3D transform;
	
	public Transform3DComponent() {
		this(new Transform3D());
	}
	public Transform3DComponent(Transform3D transform) {
		this.transform = transform;
	}
	
	@Override
	public Transform3D getTransform() {return transform;}
	public void setTransform(Transform3D transform) {this.transform = transform;}
	
}
