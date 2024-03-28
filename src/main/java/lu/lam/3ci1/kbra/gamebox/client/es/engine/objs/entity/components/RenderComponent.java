package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.objs.entity.Component;

public class RenderComponent extends Component {

	private int priority;

	public RenderComponent(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
