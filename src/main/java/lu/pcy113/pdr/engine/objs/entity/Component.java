package lu.pcy113.pdr.engine.objs.entity;

public abstract class Component {

	private Entity parent;

	public boolean attach(Entity parent) {
		if (this.parent != null) return false;
		this.parent = parent;
		return true;
	}

	public Entity getParent() { return parent; }

	@Override
	public String toString() {
		return "Component@" + getClass().getSimpleName() + "[parent=" + parent + "]";
	}

}
