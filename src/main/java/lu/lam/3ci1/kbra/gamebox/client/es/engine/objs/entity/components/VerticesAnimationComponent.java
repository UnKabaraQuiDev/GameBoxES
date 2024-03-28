package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.objs.entity.Component;

public class VerticesAnimationComponent extends Component {

	// TODO implement

	private Vector3f[] positions;

	public VerticesAnimationComponent(Vector3f[] positions) {
		this.positions = positions;
	}

	public Vector3f[] getPositions() {
		return this.positions;
	}

}
