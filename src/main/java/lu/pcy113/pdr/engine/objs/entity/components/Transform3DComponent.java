package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class Transform3DComponent extends TransformComponent {

	private Transform3D transform;

	public Transform3DComponent() {
		this(new Transform3D());
	}
	
	public Transform3DComponent(Vector3f trans) {
		this(new Transform3D(trans));
	}

	public Transform3DComponent(Transform3D transform) {
		this.transform = transform;
	}

	public Transform3DComponent(Vector3f trans, Quaternionf rot, Vector3f scale) {
		this(new Transform3D(trans, rot, scale));
	}

	@Override
	public Transform3D getTransform() {
		return this.transform;
	}

	public void setTransform(Transform3D transform) {
		this.transform = transform;
	}

}
