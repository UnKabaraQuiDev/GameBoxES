package lu.kbra.gamebox.client.es.game.game.scenes.world.entities.components;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Velocity2DComponent;

public class Acceleration2DComponent extends Component {

	private Vector2f acceleration;

	public Acceleration2DComponent() {
		this(new Vector2f(0));
	}

	public Acceleration2DComponent(Vector2f acc) {
		this.acceleration = acc;
	}

	public void update() {
		Entity e = this.getParent();
		if (e == null) {
			throw new RuntimeException("No parent attached.");
		}

		Velocity2DComponent t2De = e.getComponent(Velocity2DComponent.class);
		if (t2De == null)
			return;

		t2De.getVelocity().add(acceleration);
	}

	public Acceleration2DComponent add(Vector2f vector2f1) {
		this.acceleration.add(vector2f1);
		return this;
	}
	
	public Acceleration2DComponent zero() {
		acceleration = new Vector2f(0);
		return this;
	}
	
	public Vector2f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2f acceleration) {
		this.acceleration = acceleration;
	}

}
