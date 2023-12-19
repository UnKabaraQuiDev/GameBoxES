package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector2f;

import lu.pcy113.pdr.engine.objs.entity.Entity;

public class Velocity2DComponent
		extends
		VelocityComponent {

	private Vector2f velocity;

	public Velocity2DComponent() {
		this(new Vector2f(0));
	}

	public Velocity2DComponent(Vector2f velocity) {
		this.velocity = velocity;
	}

	public void update() {
		Entity e = this.getParent();
		if (e == null) return;

		Transform2DComponent t2De = e.getComponent(Transform2DComponent.class);
		if (t2De == null) return;

		t2De.getTransform().getTranslation().add(this.velocity);
	}

	public Vector2f getVelocity() { return this.velocity; }

	public void setVelocity(Vector2f velocity) { this.velocity = velocity; }

}
