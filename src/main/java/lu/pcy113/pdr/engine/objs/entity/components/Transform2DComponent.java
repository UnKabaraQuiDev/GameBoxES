package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.utils.transform.Transform2D;

public class Transform2DComponent extends TransformComponent {

	private Transform2D transform;

	public Transform2DComponent() {
		this(new Transform2D());
	}

	public Transform2DComponent(Transform2D transform) {
		this.transform = transform;
	}

	@Override
	public Transform2D getTransform() {
		return this.transform;
	}

	public void setTransform(Transform2D transform) {
		this.transform = transform;
	}

}
