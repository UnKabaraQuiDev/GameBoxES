package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.objs.entity.Entity;

public class Velocity3DComponent extends VelocityComponent {
	
	private Vector3f velocity;
	
	public Velocity3DComponent() {
		this(new Vector3f(0));
	}
	public Velocity3DComponent(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public void update() {
		Entity e = getParent();
		if(e == null)
			return;
		
		Transform3DComponent t3de = e.getComponent(Transform3DComponent.class);
		if(t3de == null)
			return;
		
		t3de.getTransform().getTranslation().add(velocity);
	}
	
	public Vector3f getVelocity() {return velocity;}
	public void setVelocity(Vector3f velocity) {this.velocity = velocity;}
	
}
