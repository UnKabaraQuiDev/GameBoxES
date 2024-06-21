package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class RenderComponent extends Component {

	private float priority;

	public RenderComponent(float priority) {
		this.priority = priority;
	}

	public float getPriority() {
		return priority;
	}

	public void setPriority(float priority) {
		this.priority = priority;
	}

}
