package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;

public class Velocity2DComponent extends VelocityComponent {

	private Vector2f velocity;

	public Velocity2DComponent() {
		this(new Vector2f(0));
	}

	public Velocity2DComponent(Vector2f velocity) {
		this.velocity = velocity;
	}

	public void update() {
		Entity e = this.getParent();
		if (e == null)if (e == null) {
			throw new RuntimeException("No parent attached.");
		}

		Transform2DComponent t2De = e.getComponent(Transform2DComponent.class);
		if (t2De != null) {
			t2De.getTransform().getTranslation().add(this.velocity);
		}else {
			Transform3DComponent t3De = e.getComponent(Transform3DComponent.class);
			if (t3De != null) {
				t3De.getTransform().getTranslation().add(GeoPlane.XY.project(this.velocity));
			}else {
				throw new RuntimeException("No transform attached to parent.");
			}
		}
	}

	public Vector2f getVelocity() {
		return this.velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

}
